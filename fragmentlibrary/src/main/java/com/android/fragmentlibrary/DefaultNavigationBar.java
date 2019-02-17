package com.android.fragmentlibrary;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.android.baselibrary.navigationbar.AbsNavigationBar;

/**
 * Created by freed on 2019/2/11.
 *
 * @author liuy
 */

public class DefaultNavigationBar<D extends DefaultNavigationBar.Builder.DefaultNavigationParame>
        extends AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationParame> {

    public DefaultNavigationBar(DefaultNavigationBar.Builder.DefaultNavigationParame mParams) {
        super(mParams);
    }


    @Override
    public int bindLayoutId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        //绑定效果
        setText(R.id.title, getmParams().mTitle);
        setText(R.id.right_text, getmParams().mRightText);

        setOnClickListener(R.id.right_text, getmParams().mRightClickListener);
        //左边要写一个默认的 finishActivity
        setOnClickListener(R.id.back, getmParams().mLeftClickListener);

        setViewVisibility(R.id.back,getmParams().leftIconVisiable);
    }



    public static class Builder extends AbsNavigationBar.Builder {

        DefaultNavigationParame P;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationParame(context, parent);
        }

        @Override
        public DefaultNavigationBar builer() {
            DefaultNavigationBar navigationBar = new DefaultNavigationBar(P);
            return navigationBar;
        }

        //设置所有效果
        public DefaultNavigationBar.Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        public DefaultNavigationBar.Builder setRightText(String rightText) {
            P.mRightText = rightText;
            return this;
        }

        public DefaultNavigationBar.Builder setRightIcon(int rightRes) {
            P.mrightRes = rightRes;
            return this;
        }

        public DefaultNavigationBar.Builder setRightListener(View.OnClickListener rightLis) {
            P.mRightClickListener = rightLis;
            return this;
        }

        public DefaultNavigationBar.Builder setLeftListener(View.OnClickListener liftLis) {
            P.mLeftClickListener = liftLis;
            return this;
        }

        public DefaultNavigationBar.Builder hideLeftIcon() {
            P.leftIconVisiable = View.INVISIBLE;
            return this;
        }


        public static class DefaultNavigationParame extends AbsNavigationBar.Builder.AbsNavigationParams {


            //所有的效果的放置
            public String mTitle;
            public String mRightText;
            public int mrightRes;
            public int leftIconVisiable = View.VISIBLE;

            public View.OnClickListener mRightClickListener;
            public View.OnClickListener mLeftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity) mContext).finish();
                }
            };

            public DefaultNavigationParame(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }

}
