package com.android.essayjoke;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.baselibrary.ExceptionCrashHandle;
import com.android.baselibrary.dialog.AlertDialog;
import com.android.baselibrary.http.EngineCallBack;
import com.android.baselibrary.http.HttpUtils;
import com.android.baselibrary.http.OkHttpEngine;
import com.android.baselibrary.ioc.OnClick;
import com.android.baselibrary.ioc.ViewById;
import com.android.baselibrary.fixBug.FixDexManager;
import com.android.essayjoke.mode.DiscoverListResult;
import com.android.fragmentlibrary.BaseSkipActivity;
import com.android.fragmentlibrary.DefaultNavigationBar;
import com.android.fragmentlibrary.HttpCallback;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Target;
import java.util.Map;

public class MainActivity extends BaseSkipActivity {

    private static final String TAG ="MainActivity" ;
    private int mPage = 0;
    /****Hello World!****/
    @ViewById(R.id.test_tv)
    private TextView mTestTv;
    @ViewById(R.id.test_iv)
    private ImageView mTestIv;




    @OnClick(R.id.test_tv)
    private void testTvClick(TextView testTv) {
    }

    @OnClick(R.id.test_iv)
    private void testIvClick(ImageView testIv) {
    }

    @Override
    public void setContentView() {

        setContentView(R.layout.activity_main);
    }

    @Override
    public void initTitle() {
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this, (ViewGroup) findViewById(R.id.activity_main))
                        .setTitle("标题")
                        .setRightText("发布")
                        .setRightListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                            }
                        })
//                        .setRightIcon(R.id.account_icon_weibo)
                        .builer();


    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
//        aliPayHotModify();
//        fixDexBug();
//        HttpUtils httpUtils=new HttpUtils(this);
//        httpUtils.exchangeEnine(new OkHttpEngine());
//        new HttpUtils().get("", null, new EngineCallBack<BaseModel>() {
//            @Override
//            public void onError(Exception e) {
//
//            }
//
//            @Override
//            public void onSuccess(BaseModel result) {
//
//            }
//        });
        String uuu="http://is.snssdk.com/2/essay/discovery/v3/?iid=61525517598&channel360&aid=7&app_name=joke_essay&version_name=5.7.0&ac=wifi&device_id=30036118478&device_brand=Xiaomi&update_version_code=5701&manifest_version_code=570&longitude=133.000366&latitude=28.171377&device_platform=android";
        HttpUtils
                .with(this)
                .url("http://is.snssdk.com/2/essay/discovery/v3/?")
                .addParam("iid","61525517598")
//                .exchangeEngine(new OkHttpEngine())//切换引擎
                .get()
                .execute(new HttpCallback<DiscoverListResult>() {
        //路径 apk 都需要放到jni

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(DiscoverListResult result) {
//            Gson gson = new Gson();
//            DiscoverListResult discoverListResult=gson.fromJson(result,DiscoverListResult.class);
//            //显示列表
//
//            Log.e("TAG",result);
            Log.i(TAG,"name -- >"+result.getData().getCategories().getName());
        }
    });
    }

    private void fixDexBug() {
//        File fixFile= ExceptionCrashHandle.getInstance().getCrashFile();
        File fixFile= new File(Environment.getExternalStorageDirectory(),"fix.dex");
        if(fixFile!=null&fixFile.exists()){
            FixDexManager fixDexManager=new FixDexManager(getApplicationContext());
            try {
                fixDexManager.fixDex(fixFile.getAbsolutePath());
                Log.i("info","success==================");
                Toast.makeText(this,"修复成功",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this,"修复失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void aliPayHotModify() {
        File crashFile= ExceptionCrashHandle.getInstance().getCrashFile();
        if(crashFile!=null&crashFile.exists()){
            try {
                InputStreamReader fileReader=new InputStreamReader(new FileInputStream(crashFile));
                char[] buffer=new char[1024];
                int len=0;
                while ((len = fileReader.read(buffer)) != -1){
                    String str=new String(buffer,0,len);
                    Log.e("info",str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File fixFile=new File(Environment.getExternalStorageDirectory(),"fix.apatch");
        if(fixFile.exists()){
            //修复bug
            try {
                BaseApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(this,"修复成功",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"修复失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void initListener() {
        mTestIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog=new AlertDialog.Builder(MainActivity.this)
                        .setContentView(R.layout.detail_comment_dialog)
//                        .setText(R.id.submit_btn,"接收")
//                        .setOnClickListener(R.id.account_icon_weibo, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Toast.makeText(MainActivity.this,"微博分享",Toast.LENGTH_LONG).show();
//                            }
//                        })
                        .formBottom(true)
                        .fullWidth()
                        .show();
                final EditText commentEt=dialog.getView(R.id.comment_editor);
                dialog.setOnclickListener(R.id.submit_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this,commentEt.getText().toString().trim(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void aliFixBug(){

    }
}
