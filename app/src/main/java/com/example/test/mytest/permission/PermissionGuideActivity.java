package com.example.test.mytest.permission;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.test.mytest.R;
import com.example.test.mytest.util.ConstDef;
import com.example.test.mytest.util.UtilPermission;
import com.example.test.mytest.widget.activity.MyTestActivity;
import com.example.test.mytest.widget.permission.CheckPermission;
import com.example.test.mytest.widget.permission.Dlog;
import com.example.test.mytest.widget.permission.PermissionListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Harry on 2017. 4. 4..
 */

public class PermissionGuideActivity extends AppCompatActivity {
    private static final int REQ_CODE_REQUEST_SETTING = 20;

    @BindView(R.id.permission_guide_text)   TextView permissionGuideText;

    String[] permissions;
    String packageName;

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_guide_activity);
        ButterKnife.bind(this);
        setupFromSavedInstanceState(savedInstanceState);
    }

    private void setupFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            permissions = savedInstanceState.getStringArray(ConstDef.EXTRA_PERMISSIONS);
            packageName = savedInstanceState.getString(ConstDef.EXTRA_PACKAGE_NAME);
        } else {
            Intent intent = getIntent();
            permissions = intent.getStringArrayExtra(ConstDef.EXTRA_PERMISSIONS);
//            packageName = intent.getStringExtra(ConstDef.EXTRA_PACKAGE_NAME);
            packageName = this.getPackageName();
        }
        StringBuilder sb = new StringBuilder();
        for (String permission : permissions) {
            sb.append(permission + "\n");
        }
        permissionGuideText.setText(sb.toString());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArray(ConstDef.EXTRA_PERMISSIONS, permissions);
        outState.putString(ConstDef.EXTRA_PACKAGE_NAME, packageName);

        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.permission_guide_btn)
    public void onClickPermissionBtn() {
        checkPermission();
//        super.checkPermission(permissions);
    }

    private void checkPermission() {
        new CheckPermission(this, permissions, new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Dlog.d("Granted");
                setResult(RESULT_OK);
                PermissionGuideActivity.this.finish();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // nothing to do
                Dlog.d("Denied");
            }

            @Override
            public void onPermissionDeniedNeedShowDialogSetting(ArrayList<String> deniedPermissions) {
//                showPermissionDenyDialog();
                Dlog.d("Denied - regardless request Setting");
                if (UtilPermission.checkSelfPermissions(PermissionGuideActivity.this, permissions).isEmpty()) {
                    setResult(RESULT_OK);
                } else {
//                    setResult(RESULT_CANCELED);
                    showPermissionDenyDialog();
                }
//                PermissionGuideActivity.this.finish();
            }
        }).check();
    }

    public void showPermissionDenyDialog() {
        Dlog.d("");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.permission_need_setting))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nothing to do
                    }
                })
                .setPositiveButton(getString(R.string.setting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + packageName));
                            startActivityForResult(intent, REQ_CODE_REQUEST_SETTING);

                        } catch (ActivityNotFoundException e) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                            startActivityForResult(intent, REQ_CODE_REQUEST_SETTING);
                        }

                    }
                });

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_REQUEST_SETTING:
//                checkPermission();
                if (UtilPermission.checkSelfPermissions(this, permissions).isEmpty()) {
                    setResult(RESULT_OK);
                    PermissionGuideActivity.this.finish();
//                } else {
//                    setResult(RESULT_CANCELED);
//                    PermissionGuideActivity.this.finish();
                }
                break;
        }
    }
}
