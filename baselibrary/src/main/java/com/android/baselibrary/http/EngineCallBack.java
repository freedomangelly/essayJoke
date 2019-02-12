package com.android.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by freed on 2019/2/12.
 */

public interface EngineCallBack<T> {
    public void onPreExecute(Context context, Map<String,Object> params);
    //错误
    public void onError(Exception e);
    //成功
    public void onSuccess(String result);

    public final EngineCallBack Defualt_call_back=new EngineCallBack() {
        @Override
        public void onPreExecute(Context context, Map params) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };

}
