package com.github.thinwonton.hybriddemo;

import android.os.Bundle;

import org.apache.cordova.CordovaActivity;

/**
 * Created by Administrator on 2016/3/8.
 */
public class CordovaHybridActivity extends CordovaActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.init();

        loadUrl("file:///android_asset/www/index.html");
	}
}
