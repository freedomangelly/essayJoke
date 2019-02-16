package com.android.fragmentlibrary.http;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/13.
 *         Date:2019/2/13
 * @description
 */

public class CacheData {
    //请求链接
    private String mUrlKey;
    //返回数据
    private String mResultJson;

    public CacheData() {
    }

    public CacheData(String mUrlKey, String mResultJson) {
        this.mUrlKey = mUrlKey;
        this.mResultJson = mResultJson;
    }

    public String getmUrlKey() {
        return mUrlKey;
    }

    public void setmUrlKey(String mUrlKey) {
        this.mUrlKey = mUrlKey;
    }

    public String getmResultJson() {
        return mResultJson;
    }

    public void setmResultJson(String mResultJson) {
        this.mResultJson = mResultJson;
    }
}
