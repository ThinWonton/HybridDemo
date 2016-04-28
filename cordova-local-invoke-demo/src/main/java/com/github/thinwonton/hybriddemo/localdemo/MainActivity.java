package com.github.thinwonton.hybriddemo.localdemo;

import android.os.Bundle;

import org.apache.cordova.CordovaActivity;

public class MainActivity extends CordovaActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.init();
		loadUrl("file:///android_asset/www/index.html");
		// loadUrl("http://m.jd.com");
	}
}
