package com.example.test.mytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.test.mytest.permission.PermissionActivity;
import com.example.test.mytest.widget.activity.MyTestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_btn_permission) Button mainBtnPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.main_btn_permission)
    public void onClickMainBtnPermission() {
        startActivity(new Intent(this, PermissionActivity.class));
    }
}
