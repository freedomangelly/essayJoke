package com.android.essayjoke.utils;

/**
 * @author Created by freed
 * Created by freed on 2019/2/23.
 * Date:2019/2/23
 * @description
 */
public class PatchUtils {
    /**
     *
     * @param oldpath 原来的apk 1.0
     * @param newApkPath 新的apk 2.0
     * @param patchPath 差分包路径 从服务器上下下来
     */
    public static native void combine(String oldpath,String newApkPath,String patchPath);


}
