package com.android.essayjoke;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.android.essayjoke.utils.ImageUtil;
import com.android.essayjoke.utils.PatchUtils;
import com.android.fragmentlibrary.BaseSkipActivity;
import com.android.fragmentlibrary.selectimage.ImageSelector;
import com.android.fragmentlibrary.selectimage.SelectImageActivity;

import java.io.File;
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

    String patch_path=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"version_1.0_2.0.patch";
    String mNewApkPath=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"version2.0.apk";
    @Override
    public void initData() {
        //1.访问后台接口，需不需要更新版本

        //2.需要更新版本，那么提示用户需要下载，直接下载，然后提示用户更新
        //耗时处理 Handler  AsyncTask RxJava
        //本地apk路径怎么来，已经安装了1.0
        PatchUtils.combine(getPackageResourcePath(),mNewApkPath,patch_path);

        //下载完差分包后，调用我们的方法合并生成新apk
        //https://github.com/curasystems/bsdiff-win生成差分包

        //需要校验签名

        //5安装最新版本(网上搜索)
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(mNewApkPath)),"application/vnd.android.package-archive");
        startActivity(intent);
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

    public void compressImg(View view){
        for (String path : mImageList){
            //做优化 第一个decodeFile有可能会内存溢出
            //一般后台会规定尺寸 800
//            Bitmap bitmap= BitmapFactory.decodeFile(path);
            Bitmap bitmap= ImageUtil.decodeFile(path);
            //调用native方法
            ImageUtil.compressImage(bitmap,30, Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+new File(path).getName());
        }
    }
}
