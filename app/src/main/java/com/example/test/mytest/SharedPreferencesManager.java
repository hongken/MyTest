package com.example.test.mytest;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Harry on 2017. 4. 4..
 */

public class SharedPreferencesManager {

    private static final String KEY_SHAREDPREFER_DIALOG_GOTO_SETTING_REQUEST_PERMISSION = "REQUEST_PERMISSION_SETTING";

    public static boolean getRequestPermissionGotoSetting(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(KEY_SHAREDPREFER_DIALOG_GOTO_SETTING_REQUEST_PERMISSION, 0);
        return sharedPreferences.getBoolean(KEY_SHAREDPREFER_DIALOG_GOTO_SETTING_REQUEST_PERMISSION, false);
    }

    public static void RequestPermissionGotoSetting(Context mContext, boolean value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(KEY_SHAREDPREFER_DIALOG_GOTO_SETTING_REQUEST_PERMISSION, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_SHAREDPREFER_DIALOG_GOTO_SETTING_REQUEST_PERMISSION, value);
        editor.commit();
    }
}
