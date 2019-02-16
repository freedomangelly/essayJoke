package com.android.baselibrary.navigationbar;

/**@author liuy
 * Created by freed on 2019/2/11.
 * @deprecated 创建标题栏的基类
 */

public interface INavigationBar {

    /**
     * 绑定标题栏的布局文件
     * @return
     */
    public int bindLayoutId();

    /**
     * 实例化标题栏布局布局的参数
     */
    public void applyView();
}
