package com.android.baselibrary.http;

import android.content.Context;
import android.util.ArrayMap;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.android.baselibrary.http.EngineCallBack.Defualt_call_back;

/**@author liuy
 * Created by freed on 2019/2/12.
 *
 * 一套自己的http实现
 */

public class HttpUtils{
    //直接带参数 httpUtils,链式调用

    private String mUrl;
    private int mType=GET_TYPE;
    private static final int POST_TYPE = 0x0012;
    private static final int GET_TYPE = 0x0011;

    private Context mContext;

    private Map<String,Object> mParams;

    private HttpUtils(Context context){
        mContext=context;
        mParams=new HashMap<>();
    }

    public static HttpUtils with(Context context){
        return new HttpUtils(context);
    }
    public HttpUtils url(String mUrl){
        this.mUrl=mUrl;
        return this;
    }
    //请求的方式
    public HttpUtils post(){
        mType=POST_TYPE;
        return this;
    }
    //请求的方式
    public HttpUtils get(){
        mType=GET_TYPE;
        return this;
    }

    //添加参数
    public HttpUtils addParam(String key,Object value){
        mParams.put(key,value);
        return this;
    }
    public HttpUtils addParam(Map<String,Object> params){
        mParams.putAll(params);
        return this;
    }

    //添加回调
    public void execute(EngineCallBack callBack){
//        mParams.put("","");//不能这个做
        //每次执行会进入这个方法:这里添加是行不通的
        //1.baseLibrary里面这里不要包含业务逻辑
        //2.如果每一个项目，多条业务线
        //3让callBack回调
        callBack.onPreExecute(mContext,mParams);
        if(callBack==null){
            callBack=Defualt_call_back;
        }
        //判断执行方法
        if(mType == POST_TYPE){
            post(mUrl,mParams,callBack);
        }else {
            get(mUrl,mParams,callBack);
        }
    }
    public void execute(){
        execute(null);
    }

    //默认OkHttpEngine
    private static IHttpEngine mHttpEngine=new OkHttpEngine();

    public static void init(IHttpEngine httpEngine){
        mHttpEngine=httpEngine;
    }

    /**
     *
     * @param httpEngine
     */
    public void exchangeEnine(IHttpEngine httpEngine){

    }
    private void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(mContext,url,params,callBack);
    }

    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(mContext,url,params,callBack);
    }

    public HttpUtils exchangeEngine(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
        return this;
    }

    /**
     * 拼接参数
     */
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }

    /**
     * 解析一个类上面的class信息
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }
}
