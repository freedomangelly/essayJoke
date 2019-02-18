package com.android.fragmentlibrary.selectimage;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.fragmentlibrary.R;
import com.android.fragmentlibrary.recyclerview.adapter.CommonRecyclerAdapter;
import com.android.fragmentlibrary.recyclerview.adapter.ViewHolder;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Email 240336124@qq.com
 * Created by Darren on 2017/4/9.
 * Version 1.0
 * Description:
 */
public class SelectImageListAdapter extends CommonRecyclerAdapter<String> {
    /**
     * 选择图片的集合
     */
    ArrayList<String> mResultImageList;
    private int mMaxCount;
    public SelectImageListAdapter(Context context, List<String> data, ArrayList<String> imageList,int maxCount) {
        super(context, data, R.layout.media_chooser_item);
        mResultImageList=imageList;
        this.mMaxCount=maxCount;
    }

    @Override
    public void convert(ViewHolder holder, final String item) {
        if(TextUtils.isEmpty(item)){
            // 显示拍照
            holder.setViewVisibility(R.id.camera_ll, View.VISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator, View.INVISIBLE);
            holder.setViewVisibility(R.id.image, View.INVISIBLE);
            holder.setOnIntemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //调用拍照.权限很重要 6.0
                    //TODO 查询google开源权限框架 http://www.jianshu.com/p/823360bb183f

                }
            });
        }else{
            // 显示图片
            holder.setViewVisibility(R.id.camera_ll, View.INVISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator, View.VISIBLE);
            holder.setViewVisibility(R.id.image, View.VISIBLE);

            // 显示图片利用Glide
            ImageView imageView = holder.getView(R.id.image);
            Glide.with(mContext).load(item)
                    .centerCrop().into(imageView);
            ImageView selectIndicatorIv=holder.getView(R.id.media_selected_indicator);
            if(mResultImageList.contains(item)){
                //点了选择的图片
                selectIndicatorIv.setSelected(true);
            }else{
                selectIndicatorIv.setSelected(false);
            }
            //给条目增加点击事件
            holder.setOnIntemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //没有就加入集合，有就移除集合
                    if(!mResultImageList.contains(item)){
                        Log.i("info","mResultImageList.size="+mResultImageList.size()+",mMaxCount="+mMaxCount);
                        if(mResultImageList.size()>=mMaxCount){
                            return;
                        }
                        mResultImageList.add(item);
                    }else {
                        mResultImageList.remove(item);
                    }
                    notifyDataSetChanged();
                    //通知显示布局
                    if(mListener!=null){
                        mListener.select();
                    }
                }
            });
        }
    }

    private SelectImageListener mListener;

    public void setOnSelectImageListener(SelectImageListener mListener) {
        this.mListener = mListener;
    }
}
