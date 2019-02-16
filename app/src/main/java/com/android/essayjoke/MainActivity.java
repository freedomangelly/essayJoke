package com.android.essayjoke;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.baselibrary.ExceptionCrashHandle;
import com.android.baselibrary.dialog.AlertDialog;
import com.android.baselibrary.fixBug.FixDexManager;
import com.android.baselibrary.http.HttpUtils;
import com.android.baselibrary.ioc.OnClick;
import com.android.baselibrary.ioc.ViewById;
import com.android.essayjoke.mode.RetrofitResult;
import com.android.fragmentlibrary.BaseSkipActivity;
import com.android.fragmentlibrary.DefaultNavigationBar;
import com.android.fragmentlibrary.HttpCallback;
import com.android.fragmentlibrary.db.DaoSupportFactory;
import com.android.fragmentlibrary.db.IDaoSoupport;
import com.android.fragmentlibrary.db.Person;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends BaseSkipActivity {

    private static final String TAG = "MainActivity";
    private int mPage = 0;
    /****Hello World!****/
    @ViewById(R.id.test_tv)
    private TextView mTestTv;
    @ViewById(R.id.test_iv)
    private ImageView mTestIv;
    /****换肤****/
    @ViewById(R.id.btn_test)
    private Button mBtnTest;


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
                        Toast.makeText(MainActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
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
//        int j=1/0;
//        hotModify();
//        netEngine();
        //为什么用factory 目前的数据是在内存卡中 有时候我们需要放到data/data/xxx/databases
        //获取的factory不一样，位置是可以不一样的

        //2.面向接口编程，获取IDaoSoupport那么不需要关系实现，目前的实现是我们自己写的，方便以后使用第三方的

        //3.就是为了高扩展

//        insertdb();
        //与litepal对比比它快
//        HttpUtils.with(this).url("https://api.github.com/repos/square/retrofit/contributors")
//                .cache(false)//读取缓存
//                .execute(new HttpCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.i(TAG, "result=" + result);
//                        Gson gson = new Gson();
//                        RetrofitResult retrofitResults = gson.fromJson(result, RetrofitResult.class);
//                        Log.i(TAG, retrofitResults.getAvatar_url());
//                        //成功回调这个方法
//                        //目前是没有缓存，现在有了数据库，还有了网络引擎
//
//                        //思路 某些接口如果需要缓存，请自己带标识
//                    }
//
//                    @Override
//                    public void onError(Object e) {
//
//                    }
//                });

        startActivity(new Intent(MainActivity.this,MainActivity2.class));
        this.finish();
    }

    private void insertdb() {
        IDaoSoupport<Person> daoSoupport = DaoSupportFactory
                .getFactory()
                .getDao(Person.class);
        //最少的支持原则
//        List<Person> persons=new ArrayList<>();
//        for (int i = 0 ;i<10;i++){
//            persons.add(new Person("darren",22+i));
//        }
//        daoSoupport.insert(persons);
        List<Person> persons = daoSoupport.querySupport().selection("age = ?").selectionArgs("23").query();
        Toast.makeText(this, "count ->" + persons.size(), Toast.LENGTH_SHORT).show();
    }

    private void netEngine() {
        String url = "https://api.github.com/repos/square/retrofit/contributors";
        HttpUtils
                .with(this)
                .url(url)
                .cache(false)
//                .addParam("iid", "61525517598")
//                .exchangeEngine(new OkHttpEngine())//切换引擎
                .get()
                .execute(new HttpCallback<String>() {

                    @Override
                    public void onError(Object e) {

                    }

                    @Override
                    public void onSuccess(String result) {
//            Gson gson = new Gson();
//            DiscoverListResult discoverListResult=gson.fromJson(result,DiscoverListResult.class);
//            //显示列表
//
//            Log.e("TAG",result);
                        Log.i(TAG, "name -- >" + result);
                    }
                });
    }

    private void hotModify() {
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
    }

    private void fixDexBug() {
//        File fixFile= ExceptionCrashHandle.getInstance().getCrashFile();
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");
        if (fixFile != null & fixFile.exists()) {
            FixDexManager fixDexManager = new FixDexManager(getApplicationContext());
            try {
                fixDexManager.fixDex(fixFile.getAbsolutePath());
                Log.i("info", "success==================");
                Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "修复失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void aliPayHotModify() {
        File crashFile = ExceptionCrashHandle.getInstance().getCrashFile();
        if (crashFile != null & crashFile.exists()) {
            try {
                InputStreamReader fileReader = new InputStreamReader(new FileInputStream(crashFile));
                char[] buffer = new char[1024];
                int len = 0;
                while ((len = fileReader.read(buffer)) != -1) {
                    String str = new String(buffer, 0, len);
                    Log.e("info", str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");
        if (fixFile.exists()) {
            //修复bug
            try {
                BaseApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "修复失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void initListener() {
        mTestIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
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
                final EditText commentEt = dialog.getView(R.id.comment_editor);
                dialog.setOnclickListener(R.id.submit_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, commentEt.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void aliFixBug() {

    }
}
