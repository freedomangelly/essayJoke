package com.android.baselibrary.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 头部的基类对标题栏进行再次的封装
 * 使用泛型的目的是必须传入AbsNavigationParams的基类
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private P mParams;

    private View mNavigationView;
    public AbsNavigationBar(P mParams){
        this.mParams=mParams;
        createAndBindView();
    }

    public P getmParams() {
        return mParams;
    }

    public void setText(int viewId,String text){
        TextView tv=findViewById(viewId);
        if(!TextUtils.isEmpty(text)){
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    public <T extends View> T findViewById(int viewId){
        return (T)mNavigationView.findViewById(viewId);
    }

    public void setOnClickListener(int viewId,View.OnClickListener listener){
        findViewById(viewId).setOnClickListener(listener);
    }

    /**
     * 绑定标题栏布局并将它放在跟布局的顶部
     */
    private void createAndBindView() {
        if(mParams.mParent == null){
            //获取activity的根布局
            ViewGroup activityRoot = (ViewGroup) ((Activity)(mParams.mContext))
                    .getWindow().getDecorView();
//                    .findViewById(android.R.id.content);
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }

        //处理activity
        if(mParams.mParent == null){
            return;
        }
        //创建view
        mNavigationView=LayoutInflater.from(mParams.mContext).inflate(bindLayoutId(),mParams.mParent,false);
        //添加
        mParams.mParent.addView(mNavigationView,0);
        applyView();
    }

    //builder 仿照系统写的 ，套路，活 A

    public abstract static class Builder{
        AbsNavigationParams P;
        public Builder(Context context, ViewGroup parent) {
            this.P=new AbsNavigationParams(context,parent);
        }

        public abstract AbsNavigationBar builer();
        public static class AbsNavigationParams{
            public Context mContext;
            public ViewGroup mParent;

            public AbsNavigationParams(Context context,ViewGroup parent){
                this.mContext=context;
                this.mParent=parent;
            }
        }
    }
}
