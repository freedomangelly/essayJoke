package com.android.fragmentlibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/16.
 *         Date:2019/2/16
 * @description
 */

public class SkinAttr {
    private String mresNamne;
    private SkinType mType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mresNamne = resName;
        this.mType=skinType;
    }

    public void skin(View mView) {
        mType.skin(mView,mresNamne);
    }

}
