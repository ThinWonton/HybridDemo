package com.github.thinwonton.hybriddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.setWebViewClient(new MyWebViewClient());

		mWebView.addJavascriptInterface(new JSInterface(), "JSObject");
		mWebView.loadUrl("file:///android_asset/main/index.html");
		// mWebView.loadUrl("http://www.baidu.com");

		Button androidButton = (Button) findViewById(R.id.button);
		androidButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 调用html里面的js方法
				mWebView.loadUrl("javascript:sayHello()");
			}
		});
	}

	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
		        JsPromptResult result) {
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}

		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			Log.d("调试", consoleMessage.message() + " -- From line " + consoleMessage.lineNumber()
			        + " of " + consoleMessage.sourceId());
			return true;
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
			result.confirm();
			return true;
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
		}
	}

	private class JSInterface {
		// 如果target 大于等于API 17，则需要加上如下注解
		@JavascriptInterface
		public void showToast(String toast) {
			Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
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