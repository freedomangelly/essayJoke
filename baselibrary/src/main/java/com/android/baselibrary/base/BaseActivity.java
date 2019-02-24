package com.android.baselibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.baselibrary.ioc.ViewUtils;
import com.android.baselibrary.permission.PermissionHelper;

/**
 * Created by freed on 2019/2/10.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        ViewUtils.inject(this);

        initTitle();

        initView();

        initData();

        initListener();
    }

    public abstract void setContentView();

    public abstract void initTitle();

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();

    public void startActivity(Class clazz){
        Intent intent=new Intent(this.getApplicationContext(),clazz);
        startActivity(intent);
    }

    public <T extends View>T viewById(int viewId){
        return (T)findViewById(viewId);
    }

    public void setViewVisble(View view,int visble){
        if(view.getVisibility()!=visble){
            view.setVisibility(visble);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.requestPermissionsResult(this,requestCode,permissions,grantResults);
    }

}
