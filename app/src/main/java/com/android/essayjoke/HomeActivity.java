package com.android.essayjoke;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;

import com.android.baselibrary.http.HttpUtils;
import com.android.baselibrary.ioc.OnClick;
import com.android.essayjoke.fragment.FindFragment;
import com.android.essayjoke.fragment.FragmentManagerHelper;
import com.android.essayjoke.fragment.HomeFragment;
import com.android.essayjoke.fragment.MessageFragment;
import com.android.essayjoke.fragment.NewFragment;
import com.android.fragmentlibrary.BaseSkipActivity;
import com.android.fragmentlibrary.DefaultNavigationBar;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseSkipActivity {

    private HomeFragment mHomeFragment;
    private FindFragment mFindFragment;
    private NewFragment mNewFragment;
    private MessageFragment mMessageFragment;

    private FragmentManagerHelper mFragmentHelper;

    @Override
    public void initTitle() {
        ViewGroup parent = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this, parent)
                .setTitle("首页")
                .hideLeftIcon()
                .builer();
    }


    @Override
    public void initData() {
        mFragmentHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.main_tab_fl);
        mHomeFragment = new HomeFragment();
        mFragmentHelper.add(mHomeFragment);
    }

    @Override
    public void initView() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    public void initListener() {

    }

    @OnClick(R.id.home_rb)
    private void homeRbClick() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        mFragmentHelper.switchFragment(mHomeFragment);
    }

    @OnClick(R.id.find_rb)
    private void findRbClick() {
        if (mFindFragment == null) {
            mFindFragment = new FindFragment();
        }
        mFragmentHelper.switchFragment(mFindFragment);
    }

    @OnClick(R.id.new_rb)
    private void newRbClick() {
        if (mNewFragment == null) {
            mNewFragment = new NewFragment();
        }
        mFragmentHelper.switchFragment(mNewFragment);
    }

    @OnClick(R.id.message_rb)
    private void messageRbClick() {
        if (mMessageFragment == null) {
            mMessageFragment = new MessageFragment();
        }
        mFragmentHelper.switchFragment(mMessageFragment);
    }
}
