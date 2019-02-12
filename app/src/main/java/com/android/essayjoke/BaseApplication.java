package com.android.essayjoke;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;

import com.alipay.euler.andfix.patch.PatchManager;
import com.android.baselibrary.fixBug.FixDexManager;
import com.android.baselibrary.http.HttpUtils;
import com.android.baselibrary.http.OkHttpEngine;

/**
 * Created by freed on 2019/2/10.
 */

public class BaseApplication extends Application {
    public static PatchManager mPatchManager;
    @Override
    public void onCreate() {
        super.onCreate();
//        ExceptionCrashHandle.getInstance().init(this);
//        mPatchManager=new PatchManager(this);
//        mPatchManager.init(getLocalVersionName(this));
//        //加载之前的path包
//        mPatchManager.loadPatch();
        FixDexManager fixDexManager=new FixDexManager(this);
        try {
            fixDexManager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpUtils.init(new OkHttpEngine());
    }

    /**
     * 获取本地软件版本号
     */
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVersion;
    }

}
