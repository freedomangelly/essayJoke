package com.android.fragmentlibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/16.
 *         Date:2019/2/16
 * @description 皮肤资源的支持
 */

public class SkinResource {
    //资源通过这个对象获取
    private Resources mSkinResource;
    private String mPackageName;

    public SkinResource(Context context,String skinPath) {
        try {
            Resources superRes= context.getResources();

            AssetManager asset=AssetManager.class.newInstance();
            Method method=AssetManager.class.getDeclaredMethod("addAssetPath",String.class);

            method.invoke(asset, Environment.getExternalStorageDirectory().getAbsoluteFile()+ File.separator+"Red.skin");
            mSkinResource=new Resources(asset,superRes.getDisplayMetrics(),superRes.getConfiguration());

            //获取包名
            mPackageName=context.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过名字获取Drawable
     * @param resName
     * @return
     */
    public Drawable getDrawableByName(String resName){
        try {
            int resId= mSkinResource.getIdentifier(resName,"drawable",mPackageName);
            Drawable drawable=mSkinResource.getDrawable(resId);
            return drawable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过名字获取color
     * @param resName
     * @return
     */
    public ColorStateList getClolorByName(String resName){
        try {
            int resId= mSkinResource.getIdentifier(resName,"color",mPackageName);
            ColorStateList color=mSkinResource.getColorStateList(resId);
            return color;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
