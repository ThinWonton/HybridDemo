package com.github.thinwonton.hybriddemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HybridActivity extends AppCompatActivity {

	private static final String TAG = "Bridge";
	private static final String CALL_BY_NATIVE = "javascript:(function(){ Bridge.callByNative(%s) })()";

	private final String KEY_TOKEN = "token";
	private final String KEY_NAME = "name";
	private final String KEY_SCRIPT = "script";

	private WebView mWebView;

	private Map<String, Handler.Callback> mCallbacks = new HashMap<>();

	private int guid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hybrid);

		mWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.setWebViewClient(new MyWebViewClient());

		mWebView.loadUrl("file:///android_asset/hybrid/index.html");

		Button androidButton = (Button) findViewById(R.id.button);
		androidButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sayHello("huangh");
			}
		});
	}

	private void sayHello(String name) {
		handleNative(String.format("sayHello('%s')", name), new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {

				String ret = msg.getData().getString("ret");
				Log.i(TAG, "native callback: " + ret);

				new AlertDialog.Builder(HybridActivity.this).setTitle("Native 对话框")
		                .setMessage("返回的数据 = " + ret)
		                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();

				return true;
			}
		});
	}

	/**
	 * 处理native
	 *
	 * @param script
	 * @param callback
	 */
	private void handleNative(String script, Handler.Callback callback) {
		Log.i(TAG, "handleNative script: " + script);

		try {
			// 组装jsbridge的参数
			JSONObject input = new JSONObject();
			String token = "" + ++guid;
			input.put(KEY_TOKEN, token);
			input.put(KEY_SCRIPT, script);

			mCallbacks.put(token, callback);

			// 调用jsBridge
			mWebView.loadUrl(String.format(CALL_BY_NATIVE, input.toString()));
		}
		catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
	}

	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
		        JsPromptResult result) {
			handleWeb(message);
			result.confirm(""); // 返回给web回调参数
			return true;
		}

	}

	private void handleWeb(String message) {
		Log.i(TAG, "handleWeb input: " + message);

		try {
			JSONObject input = new JSONObject(message);
			String token = input.getString(KEY_TOKEN);
			String name = input.optString(KEY_NAME, "");

			if ("".equals(name)) { // Native 调用 Web

				String ret = input.getString("ret");// 获取回调参数

				Message msg = Message.obtain();
				Bundle data = new Bundle();
				data.putString("ret", ret);
				msg.setData(data);
				mCallbacks.remove(token).handleMessage(msg);
			}
			else {// Web 调用 Native

				JSONObject param = input.getJSONObject("param");

				// 根据方法名不同进行不同的处理
				JSONObject output = new JSONObject();
				JSONObject ret = new JSONObject();
				output.put("token", token);
				output.put("ret", ret);
				if (name.equals("model")) {
					ret.put("result", Build.MODEL);
				}
				else if (name.equals("add")) {
					ret.put("result", param.getInt("num1") + param.getInt("num2"));
				}

				// 结果返回Bridge
				mWebView.loadUrl(String.format(CALL_BY_NATIVE, output.toString()));
			}
		}
		catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		@Override
		public void onReceivedError(WebView view, WebResourceRequest request,
		        WebResourceError error) {
			super.onReceivedError(view, request, error);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}
	}
}