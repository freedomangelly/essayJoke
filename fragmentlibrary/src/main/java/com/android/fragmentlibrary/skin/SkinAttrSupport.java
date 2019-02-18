package com.android.fragmentlibrary.skin;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.android.fragmentlibrary.skin.attr.SkinAttr;
import com.android.fragmentlibrary.skin.attr.SkinType;
import com.android.fragmentlibrary.skin.attr.SkinView;

import java.util.ArrayList;
import java.util.List;

/** 获取skinAttr的属性
 * @author Created by freed
 *         Created by freed on 2019/2/16.
 *         Date:2019/2/16
 * @description 皮肤属性的一些支持
 */

public class SkinAttrSupport {
    public static final String TAG = "SkinAttrSupport";
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        // background src textColor
        List<SkinAttr> skinAttrs=new ArrayList<>();
        int attrLength=attrs.getAttributeCount();
        for(int i=0;i<attrLength;i++){
            //获取名称
            String attrName=attrs.getAttributeName(i);
            String attrValue=attrs.getAttributeValue(i);
//            Log.e(TAG,"attrname->"+attrName+",attrValue->"+attrValue);
            //只获取重要的
            SkinType skinType = getSkinType(attrName);
            if(skinType!=null){
                //资源名称 目前只有一个value 是一个n@int类型
                String resName=getResName(context,attrValue);
                if(TextUtils.isEmpty(resName)){
                    continue;
                }
                SkinAttr skinAttr=new SkinAttr(resName,skinType);
                skinAttrs.add(skinAttr);
            }
        }
        return skinAttrs;
    }

    /**
     *获取资源的名称
     * @param context
     * @param attrValue
     * @return
     */
    private static String getResName(Context context, String attrValue) {
        if(attrValue.startsWith("@")){
            attrValue=attrValue.substring(1);
            int resId=Integer.parseInt(attrValue);
            return context.getResources().getResourceEntryName(resId);
        }
        return null;
    }

    /**
     * 通过名称获取SkinTye
     * @param attrName
     * @return
     */
    private static SkinType getSkinType(String attrName) {
        SkinType[] skinTypes = SkinType.values();
        for (SkinType skinType : skinTypes) {
            if(skinType.getResName().equals(attrName)){
                return skinType;
            }
        }
        return null;
    }
}
