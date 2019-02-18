package com.android.essayjoke;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.android.fragmentlibrary.BaseSkipActivity;
import com.android.fragmentlibrary.selectimage.ImageSelector;
import com.android.fragmentlibrary.selectimage.SelectImageActivity;

import java.util.ArrayList;

/**
 * Email 240336124@qq.com
 * Created by Darren on 2017/4/9.
 * Version 1.0
 * Description:
 */
public class TestImageActivity extends BaseSkipActivity {
    private ArrayList<String> mImageList;
    private int SELECT_IMAGE_REQUEST=0x0011;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initTitle() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_test_image);
    }

    // 选择图片
    public void selectImage(View view){
//        Intent intent = new Intent(this,SelectImageActivity.class);
//        intent.putExtra(SelectImageActivity.EXTRA_SELECT_COUNT,9);
//        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MODE,SelectImageActivity.MODE_MULTI);
//        intent.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST, mImageList);
//        intent.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA, true);
//        startActivityForResult(intent,SELECT_IMAGE_REQUEST);
        //第一个只关注想要什么，良好的封装性，//不要暴露太多
        ImageSelector.create().count(9).multi().origin(mImageList).showCamera(true).start(this,SELECT_IMAGE_REQUEST);
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==RESULT_OK){
            if(requestCode==SELECT_IMAGE_REQUEST&&data!=null){
               mImageList= data.getStringArrayListExtra(SelectImageActivity.EXTRA_RESULT);

               //做一下显示
                Log.i("info",mImageList.toString());
            }
        }
    }
}
