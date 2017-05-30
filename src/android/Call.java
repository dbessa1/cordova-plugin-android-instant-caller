package it.engbms.cordova.plugins.instantcaller;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import android.Manifest;
import android.content.pm.PackageManager;

public class Call extends CordovaPlugin {

  public final String ACTION_CALL_DIAL = "dial";
  public static final int CALL_REQ_CODE = 0;
  public static final int PERMISSION_DENIED_ERROR = 20;
  public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;

  private CallbackContext callbackContext;
  private JSONArray executeArgs;

  private void callPhone(JSONArray args) throws JSONException {
      String phonenumber = args.getJSONObject(0).getString("number");
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+Uri.encode(phonenumber)));
			this.cordova.getActivity().startActivity(intent);
			this.callbackContext.success();
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    this.executeArgs = args;
    this.callbackContext = callbackContext;
    if (ACTION_CALL_DIAL.equals(action)) {
      try {
		  boolean canPhoneCall = PermissionHelper.hasPermission(this, Manifest.permission.CALL_PHONE);

		  if (canPhoneCall) {
			callPhone(executeArgs);
			return true;
		  }
		  else {
			  PermissionHelper.requestPermission(this, CALL_REQ_CODE, Manifest.permission.CALL_PHONE);
			   return false;
		  }
        
      }
      catch(Exception e) {
        System.err.println("Exception: " + e.getMessage());
        this.callbackContext.error(e.getMessage());
        return false;
      }
    }
    this.callbackContext.error("Invalid action");
    return false;
  }
  
  public void onRequestPermissionResult(int requestCode, String[] permissions,
                                        int[] grantResults) throws JSONException {
    for (int r : grantResults) {
      if (r == PackageManager.PERMISSION_DENIED) {
        this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, PERMISSION_DENIED_ERROR));
        return;
      }
    }
    switch (requestCode) {
      case CALL_REQ_CODE:
        callPhone(executeArgs);
        break;
    }
  }
}
