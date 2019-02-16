package com.android.fragmentlibrary.http;

import com.android.fragmentlibrary.db.DaoSupportFactory;
import com.android.fragmentlibrary.db.IDaoSoupport;

import java.util.List;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/13.
 *         Date:2019/2/13
 * @description
 */

public class CacheDataUtil {

    /**
     * 获取数据
     * @param finalUrl
     * @return
     */
    public static String getCacheResultJson(String finalUrl) {
        final IDaoSoupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);

        List<CacheData> cacheDatas = dataDaoSupport.querySupport().selection("mUrlKey=?").selectionArgs(OkHttpEngine.MD5Utils.stringToMD5(finalUrl)).query();
        if (cacheDatas.size() != 0) {
            CacheData cacheData = cacheDatas.get(0);
            String request = cacheData.getmResultJson();
            return request;
        }
        return null;
    }

    /**
     * 缓存数据
     * @param finalUrl
     * @param resultJson
     * @return
     */
    public static long cacheData(String finalUrl,String resultJson){
        final IDaoSoupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        dataDaoSupport.delete("mUrlKey=?",new String[]{OkHttpEngine.MD5Utils.stringToMD5(finalUrl)});
        long number=dataDaoSupport.insert(new CacheData(OkHttpEngine.MD5Utils.stringToMD5(finalUrl),resultJson));
        return number;
    }

}
