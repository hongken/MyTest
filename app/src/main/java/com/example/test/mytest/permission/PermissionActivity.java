package com.example.test.mytest.permission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.example.test.mytest.R;
import com.example.test.mytest.util.ConstDef;
import com.example.test.mytest.util.UtilPermission;
import com.example.test.mytest.widget.activity.MyTestActivity;
import com.example.test.mytest.widget.permission.Dlog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Harry on 2017. 4. 3..
 */

public class PermissionActivity extends MyTestActivity {
    public static final int REQ_CODE_PERMISSION_REQUEST = 10;
    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.CAMERA
            , Manifest.permission.READ_CONTACTS
            , Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.RECEIVE_SMS};

//            Manifest.permission.READ_CALENDAR
//            , Manifest.permission.WRITE_CALENDAR
//            , Manifest.permission.CAMERA
//            , Manifest.permission.GET_ACCOUNTS
//            , Manifest.permission.ACCESS_FINE_LOCATION
//            , Manifest.permission.ACCESS_COARSE_LOCATION
//            , Manifest.permission.RECORD_AUDIO
//            , Manifest.permission.READ_PHONE_STATE
//            , Manifest.permission.CALL_PHONE
//            , Manifest.permission.READ_CALL_LOG
//            , Manifest.permission.WRITE_CALL_LOG
//            , Manifest.permission.ADD_VOICEMAIL
//            , Manifest.permission.USE_SIP
//            , Manifest.permission.PROCESS_OUTGOING_CALLS
//            , Manifest.permission.BODY_SENSORS
//            , Manifest.permission.SEND_SMS
//            , Manifest.permission.RECEIVE_SMS
//            , Manifest.permission.READ_SMS
//            , Manifest.permission.RECEIVE_WAP_PUSH
//            , Manifest.permission.RECEIVE_MMS
//            , Manifest.permission.READ_EXTERNAL_STORAGE
//            , Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @BindView(R.id.permission_btn_permission)           Button permissionBtnPermission;
    @BindView(R.id.permission_btn_permission_overlap)   Button permissionBtnPermissionOverlap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dlog.d("");
        setContentView(R.layout.permission_activity);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.permission_btn_permission)
    public void onClickPermission() {

        Toast.makeText(this, "msg", Toast.LENGTH_SHORT).show();
        Dlog.d("onClickPermission() - clicked");
    }

    @OnClick(R.id.permission_btn_permission_overlap)
    public void onClickPermissionOverlab() {
        ArrayList<String> deniedPermissions = UtilPermission.checkSelfPermissions(this, permissions);

        if (deniedPermissions.isEmpty()) {
            // next
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    permissionBtnPermission.performClick();
                }
            }, 50);

            Dlog.d("isEmpty()");

        } else {
            Dlog.d("isEmpty() Not");
            Intent intent = new Intent(PermissionActivity.this, PermissionGuideActivity.class);
            intent.putExtra(ConstDef.EXTRA_PERMISSIONS, permissions);
            intent.putExtra(ConstDef.EXTRA_PACKAGE_NAME, PermissionActivity.this.getPackageName());

            PermissionActivity.this.startActivityForResult(intent, REQ_CODE_PERMISSION_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_CODE_PERMISSION_REQUEST == requestCode) {
            if (RESULT_OK == resultCode) {
                permissionBtnPermission.performClick();
            }
        }
    }
}
