package com.example.test.mytest.widget.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.test.mytest.R;
import com.example.test.mytest.permission.PermissionGuideActivity;
import com.example.test.mytest.util.UtilPermission;
import com.example.test.mytest.widget.permission.CheckPermission;
import com.example.test.mytest.widget.permission.Dlog;
import com.example.test.mytest.widget.permission.PermissionListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

import static com.example.test.mytest.R.string.permissions;

/**
 * Created by Harry on 2017. 4. 3..
 */

public class MyTestActivity extends AppCompatActivity {

    private static final int REQ_CODE_REQUEST_SETTING = 20;
    private String[] permissions;

    public MyTestActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dlog.d("");
    }


}
