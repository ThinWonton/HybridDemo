package com.github.thinwonton.hybriddemo.localdemo.plugin;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by hugo on 2016/4/28.
 */
public class SharePlugin extends CordovaPlugin {
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
	        throws JSONException {
        Log.e("调试", args.toString());
        if ("send".equals(action)) {
            callbackContext.success("成功了");
        }
		return super.execute(action, args, callbackContext);
	}
}
