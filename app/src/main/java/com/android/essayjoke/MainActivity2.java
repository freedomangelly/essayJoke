package com.android.essayjoke;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.fragmentlibrary.BaseSkipActivity;
import com.android.fragmentlibrary.skin.SkinManager;
import com.android.fragmentlibrary.skin.SkinResource;

import java.io.File;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/16.
 *         Date:2019/2/16
 * @description
 */

public class MainActivity2 extends BaseSkipActivity {
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main2);
    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    public void skin1(View view){
        //恢复默认
        int result=SkinManager.getInstance().restort();
//        String skinPath= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"red.skin2";
//        int result = SkinManager.getInstance().loadSkin(skinPath);
    }

    public void skin(View view){
        //换肤
        String skinPath= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"red.skin";
        int result = SkinManager.getInstance().loadSkin(skinPath);
    }
    public void skin2(View view){
        //跳转
        Intent intent=new Intent(this,MainActivity2.class);
        startActivity(intent);
    }

    @Override
    public void changeSkin(SkinResource skinResource) {
        super.changeSkin(skinResource);
        Toast.makeText(MainActivity2.this,"换肤："+skinResource,Toast.LENGTH_SHORT).show();
    }
}
