package com.android.fragmentlibrary.skin.config;

import android.content.Context;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/16.
 *         Date:2019/2/16
 * @description
 */

public class SkinPreUtils {
    private static SkinPreUtils mInstance;
    private Context mContext;

    public SkinPreUtils(Context context){
        this.mContext=context.getApplicationContext();
    }

    public static SkinPreUtils getInstance(Context context){
        if(mInstance==null){
            synchronized (SkinPreUtils.class){
                if(mInstance==null){
                    mInstance = new SkinPreUtils(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存当前皮肤路径
     * @param skinPath
     */
    public void saveSkinPath(String skinPath){
        mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME,Context.MODE_PRIVATE)
                .edit().putString(SkinConfig.SKIN_PATH_NAME,skinPath).commit();
    }

    public String getSkinPath(){
        return mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME,Context.MODE_PRIVATE)
                .getString(SkinConfig.SKIN_PATH_NAME,"");
    }

    //清空皮肤路径
    public void clearSkinInfo() {
        saveSkinPath("");
    }
}
