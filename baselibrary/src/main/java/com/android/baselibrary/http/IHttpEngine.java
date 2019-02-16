package com.android.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**@author liuy
 * Created by freed on 2019/2/12.
 */

public interface IHttpEngine {

    //get 请求
    void get(boolean isCache,Context context,String url, Map<String,Object> params, EngineCallBack callBack);
    //post请求
    void post(boolean isCache,Context context,String url,Map<String,Object> params,EngineCallBack callBack);
    //下载文件

    //上传文件

    //https添加证书
}
