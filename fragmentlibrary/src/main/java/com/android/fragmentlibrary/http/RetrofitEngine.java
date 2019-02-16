package com.android.fragmentlibrary.http;

import android.content.Context;
import android.text.TextUtils;

import com.android.baselibrary.http.EngineCallBack;
import com.android.baselibrary.http.IHttpEngine;
import com.android.baselibrary.http.RetrofitInterface;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/14.
 *         Date:2019/2/14
 * @description
 */

public class RetrofitEngine implements IHttpEngine {

    @Override
    public void get(boolean isCache, Context context, String url, Map<String, Object> params, EngineCallBack callBack) {
        getRetrofitEngine(isCache,context,url,params,callBack,true);

    }



    @Override
    public void post(boolean isCache, Context context, String url, Map<String, Object> params, EngineCallBack callBack) {
        getRetrofitEngine(isCache,context,url,params,callBack,false);
    }

    private void getRetrofitEngine(final boolean isCache, Context context, final String url, Map<String, Object> params, final EngineCallBack callBack, boolean isGet) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();

        RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
        Call<String> call;
        if(isGet==false){
            call=retrofitInterface.getRetrofitPost(params);
        }else {
            call=retrofitInterface.getRetrofitGet(params);
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result=response.body().toString();
                if(isCache){
                    String cacheResultJson=CacheDataUtil.getCacheResultJson(url);
                    if(!TextUtils.isEmpty(result)&&result.equals(cacheResultJson)){
                        return;
                    }
                }
                callBack.onSuccess(result);
                if(isCache){
                    CacheDataUtil.cacheData(url,result);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callBack.onError((Exception) t);
            }
        });
    }
}
