package com.example.test.mytest.widget.permission;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.test.mytest.R;
import com.example.test.mytest.util.ConstDef;
import com.example.test.mytest.util.UtilPermission;

import java.util.ArrayList;

/**
 * Created by Harry on 2017. 4. 7..
 */

public class PermissionTransparentActivity extends AppCompatActivity {
    private static final int REQ_CODE_PERMISSION_REQUEST = 10;
    private static final int REQ_CODE_PERMISSION_REQUEST_ACTIVITY_RESULT_SETTING = 11;
    private static final int REQ_CODE_REQUEST_SETTING = 20;

    private String[] permissions;
    private String packageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Dlog.d("");
        setContentView(R.layout.permission_transparent_activity);
        setupFromSavedInstanceState(savedInstanceState);
        checkPermissions(false);
    }

    private void setupFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            permissions = savedInstanceState.getStringArray(ConstDef.EXTRA_PERMISSIONS);
            packageName = savedInstanceState.getString(ConstDef.EXTRA_PACKAGE_NAME);

        } else {
            Intent intent = getIntent();
            permissions = intent.getStringArrayExtra(ConstDef.EXTRA_PERMISSIONS);
            packageName = intent.getStringExtra(ConstDef.EXTRA_PACKAGE_NAME);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArray(ConstDef.EXTRA_PERMISSIONS, permissions);
        outState.putString(ConstDef.EXTRA_PACKAGE_NAME, packageName);

        super.onSaveInstanceState(outState);
    }

    private void checkPermissions(boolean onActivityResult) {

        ArrayList<String> deniedPermissions = UtilPermission.checkSelfPermissions(this, permissions);

        if (deniedPermissions.isEmpty()) {
            permissionGranted();

        } else if (onActivityResult) {
            permissionDenied(deniedPermissions);
        } else {
            ActivityCompat.requestPermissions(this, deniedPermissions.toArray(new String[deniedPermissions.size()]), REQ_CODE_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Dlog.d("");
        ArrayList<String> deniedPermissions = new ArrayList<>();

        boolean shouldShowRequestPermissionRationale = false;

        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                deniedPermissions.add(permission);

                if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionTransparentActivity.this, permission)) {
                    shouldShowRequestPermissionRationale = true;
                }
            }
        }

        if (deniedPermissions.isEmpty()) {
            permissionGranted();

        } else if (shouldShowRequestPermissionRationale) {
            // 거부하거나 다시 묻지 않음 checkbox 를 체크하지 않고 거부했을 경우
            permissionDenied(deniedPermissions);

        } else {
            // 거부한 모든 권한이 다시 묻지 않음 checkbox를 체크하고 거부한 경우 설정에서 수동 변경 안내
            permissionDeniedNeedDialogSetting(deniedPermissions);
//            showPermissionDenyDialog();
        }
    }

    private void permissionGranted() {
        BusProvider.getInstance().post(new PermissionEvent(PermissionEvent.PermissionResultType.GRANTED, null));
        finish();
        overridePendingTransition(0, 0);
    }

    private void permissionDenied(ArrayList<String> deniedpermissions) {
        BusProvider.getInstance().post(new PermissionEvent(PermissionEvent.PermissionResultType.DENIED, deniedpermissions));
        finish();
        overridePendingTransition(0, 0);
    }

    private void permissionDeniedNeedDialogSetting(ArrayList<String> deniedpermissions) {
        BusProvider.getInstance().post(new PermissionEvent(PermissionEvent.PermissionResultType.DENIED_NEED_DIALOG_SETTING, deniedpermissions));
        finish();
        overridePendingTransition(0, 0);
    }

    private void permissionNeedCheckPermissions(ArrayList<String> deniedpermissions) {
        BusProvider.getInstance().post(new PermissionEvent(PermissionEvent.PermissionResultType.DENIED_NEED_DIALOG_SETTING, deniedpermissions));
        finish();
        overridePendingTransition(0, 0);
    }


    /*
    설정 클릭 > 설정 창 권한 > 권한 허용이 아니라, 거부를 추가하고 백버튼 누르면 다이얼로그 창 뒤로 검게 변함
    listener 로 넘겨서 호출
     */

    public void showPermissionDenyDialog() {
        Dlog.d("");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.permission_need_setting))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkPermissions(true);
                    }
                })
                .setPositiveButton(getString(R.string.setting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = null;
                        try {
                            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + packageName));
                        } catch (ActivityNotFoundException e) {
                            intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        } finally {
                            //startActivityForResult 호출 후 설정 > 권한 >
                            // 1.1. 허용할 경우, 정상적으로 onActivityResult 에서 처리하지만,
                            // 1.2. 허용이 아닌 거부할 경우, onCreate 호출 이후 onActivityResult 호출하고
                            // Activity를 onCreate 부터 다시 그린다.
//                            startActivityForResult(intent, REQ_CODE_REQUEST_SETTING);
                            startActivity(intent);
                            permissionDeniedNeedDialogSetting(null);
                        }

                    }
                });

        builder.show();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQ_CODE_REQUEST_SETTING:
//                Dlog.d("");
//                checkPermissions(true);
//                break;
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Dlog.d("");
        overridePendingTransition(0, 0);
    }



}
