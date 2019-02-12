package com.android.baselibrary.http;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by freed on 2019/2/12.
 * okHttp默认引擎
 */

public class OkHttpEngine implements IHttpEngine {
    private String TAG="OkHttpEngine";
    private static OkHttpClient mOkHttpClient=new OkHttpClient();

    @Override
    public void post(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        final String jointUrl=HttpUtils.jointParams(url,params);
        Log.i(TAG,jointUrl);
        RequestBody requestBody=appentBody(params);
        final Request request =
//                cache?new Request.Builder()
//                .url(url)
//                .tag(context)
//                .cacheControl(CacheControl.FORCE_CACHE)
//                .post(requestBody)
//                .build():
                new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=request.body().toString();
                callBack.onSuccess(result);
            }
        });

    }

    private RequestBody appentBody(Map<String, Object> params) {
        MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM);
        appParms(builder,params);
        return builder.build();
    }

    private void appParms(MultipartBody.Builder builder, Map<String, Object> params) {
        if(params !=null && !params.isEmpty()){
            for (String key : params.keySet()){
                builder.addFormDataPart(key,params.get(key)+"");
                Object value=params.get(key);
                if(value instanceof File){
                    //处理文件 -- >>object file
                    File file = (File) value;
                    builder.addFormDataPart(key,file.getName(),RequestBody.create(MediaType.parse(guessMineType(file.getAbsolutePath())),file));

                }else if(value instanceof List){
                    List<File> listFiles = (List<File>) value;
                    for (int i=0;i<listFiles.size();i++){
                        File file=listFiles.get(i);
                        builder.addFormDataPart(key+i,file.getName(),RequestBody.create(MediaType.parse(guessMineType(file.getAbsolutePath())),file));
                    }
                }
            }
        }
    }

    private String guessMineType(String absolutePath) {
        FileNameMap fileNameMap= URLConnection.getFileNameMap();
        String contentTypeFor=fileNameMap.getContentTypeFor(absolutePath);
        if(contentTypeFor==null){
            contentTypeFor="application/octet-stream";
        }
        return contentTypeFor;
    }

    @Override
    public void get(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
            url=HttpUtils.jointParams(url,params);
            Log.i(TAG,url);
            Request.Builder requestBuilder=new Request.Builder().url(url).tag(context);

            requestBuilder.method("GET",null);
            Request request=requestBuilder.build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callBack.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result=response.body().toString();
                    callBack.onSuccess(result);
                }
            });
    }
}
