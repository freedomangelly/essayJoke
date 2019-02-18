package com.android.fragmentlibrary.http;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.android.baselibrary.http.EngineCallBack;
import com.android.baselibrary.http.HttpUtils;
import com.android.baselibrary.http.HttpsUtils;
import com.android.baselibrary.http.IHttpEngine;
import com.android.baselibrary.http.LoggingInterceptor;
import com.android.baselibrary.http.Tls12SocketFactory;
import com.android.fragmentlibrary.db.DaoSupport;
import com.android.fragmentlibrary.db.DaoSupportFactory;
import com.android.fragmentlibrary.db.IDaoSoupport;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

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
    private String TAG = "OkHttpEngine";
    private static OkHttpClient mOkHttpClient = initHttpsClient();

    private static Handler mHandle = new Handler();

    @Override
    public void post(boolean cache, Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        final String jointUrl = HttpUtils.jointParams(url, params);
        Log.i(TAG, jointUrl);
        RequestBody requestBody = appentBody(params);
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
            public void onFailure(Call call, final IOException e) {
                mHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "error");
                        //执行成功方法
                        callBack.onError(e);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = request.body().toString();

                //获取数据之后会执行成功方法

                //2.每次获取到的数据，先比对上一次内容
                mHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "error");
                        //执行成功方法
                        callBack.onSuccess(result);
                    }
                });
            }
        });

    }

    private RequestBody appentBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        appParms(builder, params);
        return builder.build();
    }

    private void appParms(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    //处理文件 -- >>object file
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(guessMineType(file.getAbsolutePath())), file));

                } else if (value instanceof List) {
                    List<File> listFiles = (List<File>) value;
                    for (int i = 0; i < listFiles.size(); i++) {
                        File file = listFiles.get(i);
                        builder.addFormDataPart(key + i, file.getName(), RequestBody.create(MediaType.parse(guessMineType(file.getAbsolutePath())), file));
                    }
                }
            }
        }
    }

    private String guessMineType(String absolutePath) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(absolutePath);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    @Override
    public void get(final boolean cache, Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        url = HttpUtils.jointParams(url, params);
        final String finalUrl = url;

        final IDaoSoupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        //1.判断需不需要缓存，然后判断有没有
        if (cache) {
            //需要缓存,那缓存，问题又来了，OkHttpEngine BaseLibrary
            //数据库缓存在FrameLibrary
            String request = CacheDataUtil.getCacheResultJson(finalUrl);

//                List<CacheData> cacheDatas=dataDaoSupport.querySupport().selection("mUrlKey=?").selectionArgs(MD5Utils.stringToMD5(finalUrl)).query();
//                if(cacheDatas.size()!=0){
//                    CacheData cacheData=cacheDatas.get(0);
//                    String request=cacheData.getmResultJson();
            if (!TextUtils.isEmpty(request)) {
                //代表数据库有缓存
                Log.i(TAG, "读到缓存");
                callBack.onSuccess(request);
                //TODO 此处是不是应该return
                return;
            }
//                }
        }


        Log.i(TAG, url);
        Request.Builder requestBuilder = new Request.Builder().url(url).tag(context);

        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "error");
                        e.printStackTrace();
                        callBack.onError(e);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.i(TAG, "result=" + result);
                Log.i(TAG, "result=" + response.code());
                if (cache) {
//                        List<CacheData> cacheDatas=dataDaoSupport.querySupport().selection("mUrlKey=?").selectionArgs(MD5Utils.stringToMD5(finalUrl)).query();
//                        if(cacheDatas.size()!=0){
//                            CacheData cacheData=cacheDatas.get(0);
//                            String cacheResult=cacheData.getmResultJson();
                    String cacheResultJson = CacheDataUtil.getCacheResultJson(finalUrl);
                    Log.i(TAG, "读到结果");
                    if (!TextUtils.isEmpty(result)) {
                        //代表数据库有缓存
                        if (result.equals(cacheResultJson)) {
                            Log.i(TAG, "数据和缓存一致");
                            return;
                        }
                        //TODO 此处是不是应该return
                    }
//                        }
                }
                mHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "error");
                        //执行成功方法
                        callBack.onSuccess(result);
                    }
                });

                //缓存数据
                if (cache) {
                    //缓存数据
//                        dataDaoSupport.delete("mUrlKey=?",new String[]{MD5Utils.stringToMD5(finalUrl)});
//                        dataDaoSupport.insert(new CacheData(MD5Utils.stringToMD5(finalUrl),result));
                    CacheDataUtil.cacheData(finalUrl, result);
                }
            }
        });
    }

    /**
     * 解析一个类上面的class信息
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

    public static class MD5Utils {
        /**
         * 将字符串转成MD5值
         *
         * @param string
         * @return
         */
        public static String stringToMD5(String string) {
            byte[] hash;

            try {
                hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }

            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10)
                    hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        }
    }

    private static OkHttpClient initHttpsClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(30000L, TimeUnit.MILLISECONDS)
                .readTimeout(30000L, TimeUnit.MILLISECONDS)
//                .addInterceptor(new LoggingInterceptor("OkHttpClient"))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
        if (false) {
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        } else {
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");
                try {
                    sslContext.init(null, null, null);
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            SSLSocketFactory socketFactory = new Tls12SocketFactory(sslContext.getSocketFactory());
            builder.sslSocketFactory(socketFactory, new HttpsUtils.UnSafeTrustManager());
        }
        OkHttpClient okHttpClient = builder
                .build();
//        OkHttpUtils.initClient(okHttpClient);
        return okHttpClient;
    }
}
