package com.android.fragmentlibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import com.android.baselibrary.base.BaseActivity;
import com.android.fragmentlibrary.skin.SkinAttrSupport;
import com.android.fragmentlibrary.skin.SkinManager;
import com.android.fragmentlibrary.skin.attr.SkinAttr;
import com.android.fragmentlibrary.skin.attr.SkinView;
import com.android.fragmentlibrary.skin.support.SkinAppCompatViewInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freed on 2019/2/10.
 */

public abstract class BaseSkipActivity extends BaseActivity {
    private static final String TAG ="BaseSkipActivity" ;
    private SkinAppCompatViewInflater mAppCompatViewInflater;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater=LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory2(layoutInflater,this);
//        LayoutInflaterCompat.setFactory(layoutInflater, new LayoutInflaterFactory() {
//            @Override
//            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
//                //拦截到View的创建
//                Log.e("TAG","拦截到view的创建");
//                if(name.equals("Button")){
//                    TextView tv=new TextView(BaseSkipActivity.this);
//                    tv.setText("拦截");
//                    return tv;
//                }
//                return null;
//            }


//        });
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //1.创建view
        View view=createView(parent, name, context, attrs);

        //2.解析属性 src textColor background 自定义属性
        Log.e(TAG,view+"");
        //2.1解析属性 src textColor background 自定义属性
        if(view!=null){
            List<SkinAttr> skinArrAttrs=SkinAttrSupport.getSkinAttrs(context, attrs);
            SkinView skinView = new SkinView(view,skinArrAttrs);

            managerSkinView(skinView);
        }

        //3.统一讲给skinmanager管理

        return view;
    }

    /**
     * 统一管理skinView
     * @param skinView
     */
    private void managerSkinView(SkinView skinView) {
        List<SkinView> skinViews=SkinManager.getInstance().getSkinViews(this);
        if(skinViews==null){
            skinViews=new ArrayList<>();
            SkinManager.getInstance().register(this,skinViews);
        }
        skinViews.add(skinView);
    }

    public View createView(View parent, String name, Context context, AttributeSet attrs){
        final boolean isPre21= Build.VERSION.SDK_INT <21;
        if(mAppCompatViewInflater == null){
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        final boolean inheritContext=isPre21 && true && shouldInheritContext((ViewParent)parent);
        return mAppCompatViewInflater.createView(parent,name,context,attrs,inheritContext,isPre21,true);
    }


    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }
}
