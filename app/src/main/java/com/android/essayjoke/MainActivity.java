package com.android.essayjoke;

import android.widget.ImageView;
import android.widget.TextView;

import com.android.baselibrary.base.BaseActivity;
import com.android.baselibrary.ioc.OnClick;
import com.android.baselibrary.ioc.ViewById;
import com.android.fragmentlibrary.BaseSkipActivity;

public class MainActivity extends BaseSkipActivity {

    private int mPage = 0;
    /****Hello World!****/
    @ViewById(R.id.test_tv)
    private TextView mTestTv;
    @ViewById(R.id.test_iv)
    private ImageView mTestIv;




    @OnClick(R.id.test_tv)
    private void testTvClick(TextView testTv) {
    }

    @OnClick(R.id.test_iv)
    private void testIvClick(ImageView testIv) {
    }

    @Override
    public void setContentView() {

    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
