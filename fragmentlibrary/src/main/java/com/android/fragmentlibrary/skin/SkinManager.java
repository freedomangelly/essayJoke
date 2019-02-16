package com.android.fragmentlibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.android.fragmentlibrary.BaseSkipActivity;
import com.android.fragmentlibrary.skin.attr.SkinView;
import com.android.fragmentlibrary.skin.callback.ISkinChangeListener;
import com.android.fragmentlibrary.skin.config.SkinConfig;
import com.android.fragmentlibrary.skin.config.SkinPreUtils;

import java.io.File;
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
    private Map<ISkinChangeListener,List<SkinView>> mSkinViews=new HashMap<>();
    private SkinResource mSkinResource;

    static{
            instance=new SkinManager();
    }

    public static SkinManager getInstance() {
        return instance;
    }

    public void init(Context context){
        this.context=context;
        //每一次打开应用，都会到这里来，防止皮肤被恶意删除，做一些措施
        String currentSkinPath=SkinPreUtils.getInstance(context).getSkinPath();
        File file=new File(currentSkinPath);
        if(!file.exists()){
            SkinPreUtils.getInstance(context).clearSkinInfo();
            return;
        }

        //最好做一下 能不能获取到包名
        String packageName=context.getPackageManager().getPackageArchiveInfo(currentSkinPath, PackageManager.GET_ACTIVITIES).packageName;
        if(TextUtils.isEmpty(packageName)){
            SkinPreUtils.getInstance(context).clearSkinInfo();
        }
        //最好校验一下签名 增量更新再说

        //做一些初始化的操作
        mSkinResource=new SkinResource(context,currentSkinPath);
    }

    /**
     * 加载皮肤
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {
        //校验签名 增量更新
        //初始化资源管理
        //将要文件
        File file=new File(skinPath);
        if(!file.exists()){
            return SkinConfig.SKIN_FILE_NOEXIST;
        }

        String packageName = context.getPackageManager().getPackageArchiveInfo(skinPath,PackageManager.GET_ACTIVITIES).packageName;
        if(TextUtils.isEmpty(packageName)){
            return SkinConfig.SKIN_FILE_ERROR;
        }
        //当前皮肤如果一样不要换
        String currentSkinPath=SkinPreUtils.getInstance(context).getSkinPath();
        if(currentSkinPath.equals(skinPath)){
            return SkinConfig.SKIN_CHANGE_NOTING;
        }


        mSkinResource=new SkinResource(context,skinPath);
        changeSkin();

        // 保存皮肤的状态
        saveSkinStatus(skinPath);
        //改变皮肤
        return 0;
    }

    private void saveSkinStatus(String skinPath) {
        //如果用上次写好的数据库，不了解，还有致命一点，不要嵌套
        SkinPreUtils.getInstance(context).saveSkinPath(skinPath);
    }

    /**
     * 恢复默认
     * @return
     */
    public int restort() {
        //判断当前有没有皮肤，没有执行就不要执行方法
        String currentSkinPath = SkinPreUtils.getInstance(context).getSkinPath();
        Log.i("info","currentSkinPath="+currentSkinPath);
        if(TextUtils.isEmpty(currentSkinPath)){
            return SkinConfig.SKIN_CHANGE_NOTING;
        }
        //当前运行的app路径
        String skinPath=context.getPackageResourcePath();
        Log.i("info",skinPath);
        mSkinResource = new SkinResource(context,skinPath);
        changeSkin();
        //把皮肤信息清空
        SkinPreUtils.getInstance(context).clearSkinInfo();
        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }
    /**改变皮肤
     */
    private void changeSkin() {
        Set<ISkinChangeListener> keys=mSkinViews.keySet();
        for (ISkinChangeListener key : keys) {
            List<SkinView> skinViews=mSkinViews.get(key);
            for (SkinView skinView : skinViews){
                skinView.skin();
            }
            //通知activity
            key.changeSkin(mSkinResource);
        }
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
     * @param skinChangeListener
     * @param skinViews
     */
    public void register(ISkinChangeListener skinChangeListener, List<SkinView> skinViews) {
        mSkinViews.put(skinChangeListener,skinViews);
    }

    /**
     * 获取当前的皮肤资源
     * @return
     */
    public SkinResource getSkinResource(){
        return mSkinResource;
    }

    public void checkChangeSkin(SkinView skinView) {
        //检测要不要换肤

        //如果当前有皮肤，也就是保存了皮肤路径，就换一下皮肤
        String currentSkinPath=SkinPreUtils.getInstance(context).getSkinPath();
        if(!TextUtils.isEmpty(currentSkinPath)){
            skinView.skin();
        }
    }
}
