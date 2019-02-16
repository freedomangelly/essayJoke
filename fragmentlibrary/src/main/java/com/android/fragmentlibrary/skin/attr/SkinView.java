package com.android.fragmentlibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/16.
 *         Date:2019/2/16
 * @description
 */

public class SkinView {

    private View mView;

    private List<SkinAttr> mSkinAttrs;

    public SkinView(View view, List<SkinAttr> skinArrAttrs) {

        this.mView=view;
        this.mSkinAttrs=skinArrAttrs;
    }

    public void skin(){
        for (SkinAttr mAttr : mSkinAttrs) {
            mAttr.skin(mView);
        }
    }
}
