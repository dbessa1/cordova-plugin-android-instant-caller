package it.engbms.cordova.plugins.instantcaller;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class Call extends CordovaPlugin {

  public final String ACTION_CALL_DIAL = "dial";

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (ACTION_CALL_DIAL.equals(action)) {
      try {
        String phonenumber = args.getJSONObject(0).getString("number");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+Uri.encode(phonenumber)));
        this.cordova.getActivity().startActivity(intent);
        callbackContext.success();
        return true;
      }
      catch(Exception e) {
        System.err.println("Exception: " + e.getMessage());
        callbackContext.error(e.getMessage());
        return false;
      }
    }
    callbackContext.error("Invalid action");
    return false;
  }
}
