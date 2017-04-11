package com.example.test.mytest.widget.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.StringRes;

import com.example.test.mytest.R;
import com.example.test.mytest.permission.PermissionGuideActivity;
import com.example.test.mytest.util.ConstDef;
import com.example.test.mytest.util.ObjectUtils;
import com.squareup.otto.Subscribe;

/**
 * Created by Harry on 2017. 4. 3..
 */


public class CheckPermission {

    private Context context;
    private PermissionListener listener;
    private String[] permissions;

    public CheckPermission(Context context, String[] permissions, PermissionListener listener) {
        this.context = context;
        this.listener = listener;
        this.permissions = permissions;
        BusProvider.getInstance().register(this);
    }

    public void check() {
        if (listener == null) {
            throw new NullPointerException("You must setPermissionListener() on CheckPermission");
        } else if (ObjectUtils.isEmpty(permissions)) {
            throw new NullPointerException("You must setPermissions() on CheckPermission");
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Dlog.d("preMarshmallow");
            listener.onPermissionGranted();

        } else {
            Dlog.d("Marshmallow");
            Intent intent = new Intent(context, PermissionTransparentActivity.class);
            intent.putExtra(ConstDef.EXTRA_PERMISSIONS, permissions);
            intent.putExtra(ConstDef.EXTRA_PACKAGE_NAME, context.getPackageName());

            context.startActivity(intent);
        }
    }

    @Subscribe
    public void onPermissionResult(PermissionEvent event) {
        Dlog.d("");
        switch (event.getGrantedPermission()) {
            case GRANTED:
                listener.onPermissionGranted();
                break;
            case DENIED:
                listener.onPermissionDenied(event.getDeniedPermissions());
                break;
            case DENIED_NEED_DIALOG_SETTING:
                listener.onPermissionDeniedNeedShowDialogSetting(event.getDeniedPermissions());
                break;
        }

        BusProvider.getInstance().unregister(this);

    }

    /*
    private PermissionInstance instance;

    public CheckPermission(Context context) {
        instance = new PermissionInstance(context);
    }

    public CheckPermission setPermissionListener(PermissionListener listener) {
        instance.setListener(listener);
        return this;
    }

    public CheckPermission setPermissions(String... permissions) {
        instance.setPermissions(permissions);
        return this;
    }

    public void check() {
        if (instance.getListener() == null) {
            throw new NullPointerException("You must setPermissionListener() on CheckPermission");
        } else if (ObjectUtils.isEmpty(instance.getPermissions())) {
            throw new NullPointerException("You must setPermissions() on CheckPermission");
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Dlog.d("preMarshmallow");
            instance.getListener().onPermissionGranted();

        } else {
            Dlog.d("Marshmallow");
            instance.checkPermissions();
        }
    }
    */

}
