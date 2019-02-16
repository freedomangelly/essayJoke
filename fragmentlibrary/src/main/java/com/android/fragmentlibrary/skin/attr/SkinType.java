package com.android.fragmentlibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fragmentlibrary.skin.SkinManager;
import com.android.fragmentlibrary.skin.SkinResource;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/16.
 *         Date:2019/2/16
 * @description
 */

public enum  SkinType {


    TEXT_CLOLR ("textColor"){
        @Override
        public void skin(View mView, String mresNamne) {
            SkinResource skinResource=getSkinResource();

            ColorStateList color=skinResource.getClolorByName(mresNamne);
            if(color==null){
                return;
            }
            TextView textView = (TextView)mView;
            textView.setTextColor(color);
        }
    },BACKGROUND("background"){
        @Override
        public void skin(View mView, String mresNamne) {
            SkinResource skinResource=getSkinResource();
            Drawable drawable=skinResource.getDrawableByName(mresNamne);
            if(drawable!=null){
                ImageView imageView = (ImageView) mView;
                imageView.setBackgroundDrawable(drawable);
                return;
            }

            ColorStateList color=skinResource.getClolorByName(mresNamne);
            if(color!=null){
                mView.setBackgroundColor(color.getDefaultColor());
            }

        }
    },SRC("src"){
        @Override
        public void skin(View mView, String mresNamne) {
            SkinResource skinResource=getSkinResource();

            Drawable drawable=skinResource.getDrawableByName(mresNamne);
            if(drawable!=null){
                ImageView imageView = (ImageView) mView;
                imageView.setImageDrawable(drawable);
                return;
            }
        }
    };

    //把skin做成抽象的会根据名字调对应的方法
    private String mResName;
    SkinType(String resName) {
        this.mResName=resName;
    }

    public abstract void skin(View mView, String mresNamne);

    public String getResName() {
        return mResName;
    }

    public SkinResource getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }
}
