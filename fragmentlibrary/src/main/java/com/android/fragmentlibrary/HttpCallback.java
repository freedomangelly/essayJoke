package com.android.fragmentlibrary;

import android.content.Context;
import android.util.Log;

import com.android.baselibrary.http.EngineCallBack;
import com.android.baselibrary.http.HttpUtils;
import com.google.gson.Gson;

import java.util.Map;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/12.
 *         Date:2019/2/12
 * @description
 */

public abstract class HttpCallback<T> implements EngineCallBack {
    @Override
    public void onPreExecute(Context context, Map params) {
        //大大方方的添加公众参数 与项目业务逻辑有关的
        params.put("app_name","joke_essay");
        params.put("version_name","5.7.0");
        params.put("ac","wifi");
        params.put("device_id","30036118478");
        params.put("device_brand","Xiaomi");
        params.put("update_version_code","5701");
        params.put("manifest_version_code","570");
        params.put("longitude","113.000366");
        params.put("latitude","28.171377");
        params.put("devect_platform","android");
        onPreExecute();
    }
    //开始执行了
    public void onPreExecute(){

    }

    public void onSuccess(String result) {
        Log.i("info","result="+result);
        //TODO 此处有问题 result返回对象不是String
//        Gson gson = new Gson();
//        T discoverListResult= (T) gson.fromJson(result, HttpUtils.analysisClazzInfo(this));
//        onSuccess(discoverListResult);
    }

    //返回可以直接操作的对象
    public abstract void onSuccess(T result);
}
