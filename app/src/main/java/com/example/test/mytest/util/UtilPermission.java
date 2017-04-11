package com.example.test.mytest.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.example.test.mytest.widget.permission.Dlog;

import java.util.ArrayList;

/**
 * Created by Harry on 2017. 4. 6..
 */

public class UtilPermission {

    public static ArrayList<String> checkSelfPermissions(Context context, String[] permissions) {

        ArrayList<String> needPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                needPermissions.add(permission);
            }
        }
        return needPermissions;

    }
}
