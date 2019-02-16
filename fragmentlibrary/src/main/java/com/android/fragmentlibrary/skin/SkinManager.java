package com.android.fragmentlibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.util.ArrayMap;

import com.android.fragmentlibrary.BaseSkipActivity;
import com.android.fragmentlibrary.skin.attr.SkinView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/16.
 *         Date:2019/2/16
 * @description 皮肤的资源管理
 */

public class SkinManager {
    private static SkinManager instance;
    private Context context;
    private Map<Activity,List<SkinView>> mSkinViews=new HashMap<>();
    private SkinResource mSkinResource;

    static{
            instance=new SkinManager();
    }

    public static SkinManager getInstance() {
        return instance;
    }

    public void init(Context context){
        this.context=context;
    }

    /**
     * 加载皮肤
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {
        //校验签名 增量更新
        //初始化资源管理
        SkinResource skinResource=new SkinResource(context,skinPath);
        Set<Activity> keys=mSkinViews.keySet();
        for (Activity key : keys) {
            List<SkinView> skinViews=mSkinViews.get(key);
            for (SkinView skinView : skinViews){
                skinView.skin();
            }
        }
        //改变皮肤
        return 0;
    }

    /**
     * 恢复默认
     * @return
     */
    public int restort() {
        return 0;
    }

    /**
     * 获取activity
     * @param activity
     * @return
     */
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    /**
     * 注册
     * @param activity
     * @param skinViews
     */
    public void register(BaseSkipActivity activity, List<SkinView> skinViews) {
        mSkinViews.put(activity,skinViews);
    }

    /**
     * 获取当前的皮肤资源
     * @return
     */
    public SkinResource getSkinResource(){
        return mSkinResource;
    }
}
