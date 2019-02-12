package com.android.baselibrary.fixBug;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by freed on 2019/2/11.
 */

public class FixDexManager {
    private static final String TAG ="FixDexManager" ;
    private Context context;
    private File mDexDir;

    public FixDexManager(Context context) {
        this.context=context;
        //获取应用可以访问的目录
        this.mDexDir = context.getDir("odex",Context.MODE_PRIVATE);
    }

    public void fixDex(String fixDexPath) throws Exception {
        Log.i(TAG,"enter fixDex");
        //已经运行的DexElement
        ClassLoader applicationClassLoader=context.getClassLoader();
        Log.i(TAG,"enter fixDex 2");
        Object applicationDexElements=getDexElementsByClassLoader(applicationClassLoader);
        //获取下载好的补丁 dexElement
        Log.i(TAG,"enter fixDex 3");
        //移动到系统能能够访问的 dex目录下 ClassLoder
        File srcFile=new File(fixDexPath);

        if(srcFile==null||!srcFile.exists()){
            Log.i(TAG,"no such srcFile");
            throw new FileNotFoundException(fixDexPath);
        }

        File destFile=new File(mDexDir,srcFile.getName());
        if(!destFile.exists()){
            Log.i(TAG,"patch["+fixDexPath+"] has be loaded");
            return;
        }

        Log.i(TAG,"srcFile="+srcFile+",destFile="+destFile);
        copyFile(srcFile,destFile);

        //2.2lassloader 读取fixDex路径 为什么加入到集合？启动就可能要修复
        List<File> fixDexFiles=new ArrayList<File>();
        fixDexFiles.add(destFile);

        fixDexFiles(fixDexFiles);
    }

    /**
     * 把dexEmelents注入到classLoader中
     * @param classLoader
     * @param elements
     */
    private void injectDexElements(ClassLoader classLoader, Object elements) throws Exception {
        Log.i(TAG,"enter injectDexElements");
        //先获取 pathlist
        Field pathListFiled=BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListFiled.setAccessible(true);
        Object opathList=pathListFiled.get(classLoader);

        //pathList里面的dexElement
        Field dexElementsField=opathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);

        dexElementsField.set(opathList,elements);
    }

    /**
     * 从classLoader中获取 dexElements
     * @param classLoader
     * @return
     */
    private Object getDexElementsByClassLoader(ClassLoader classLoader) throws Exception {
        //先获取 pathlist
        Field pathListFiled=BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListFiled.setAccessible(true);
        Object opathList=pathListFiled.get(classLoader);

        //pathList里面的dexElement
        Field dexElementsField=opathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);

        return dexElementsField.get(opathList);
    }

    public static void copyFile(File src,File dest) throws IOException{
        FileChannel inChannel=null;
        FileChannel outChannel=null;

        try {
            if(!dest.exists()){
                dest.createNewFile();
            }
            inChannel=new FileInputStream(src).getChannel();
            outChannel=new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0,inChannel.size(),outChannel);
        }finally {
            if(inChannel!=null){
                inChannel.close();
            }
            if(outChannel!=null){
                outChannel.close();
            }
        }
    }

    /**
     * 合并两个数组
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs,Object arrayRhs){
        Class<?> localClass=arrayLhs.getClass().getComponentType();
        int i= Array.getLength(arrayLhs);
        int j = i+Array.getLength(arrayRhs);
        Object result=Array.newInstance(localClass,j);
        for(int k = 0;k<j;++k){
            if(k<i){
                Array.set(result,k,Array.get(arrayLhs,k));
            }else {
                Array.set(result,k,Array.get(arrayRhs,k-i));
            }
        }
        return result;
    }


    public void loadFixDex()throws Exception {
        File[] fixDex=mDexDir.listFiles();

        List<File> fixDexFiles = new ArrayList<File>();
        for (File dexFile:fixDex){
            if(dexFile.getName().endsWith(".dex")){
                fixDexFiles.add(dexFile);
            }
        }

        fixDexFiles(fixDexFiles);

    }

    private void fixDexFiles(List<File> fixDexFiles) throws Exception {
        ClassLoader applicationClassLoader=context.getClassLoader();

        Object applicationDexElements=getDexElementsByClassLoader(applicationClassLoader);

        File optimizedDirectory=new File(mDexDir,"odex");
        Log.i(TAG,"optimizedDirectory="+optimizedDirectory.getAbsolutePath());
        if(optimizedDirectory==null||!optimizedDirectory.exists()){
            optimizedDirectory.mkdirs();
        }
        //修复
        for (File fixDexFile : fixDexFiles){
            ClassLoader fixDexlassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(),//dexPath dex路径
                    optimizedDirectory, //optimizedDirectory 解压路径
                    null,//libraySearchPath So文件目录
                    applicationClassLoader//父ClassLoader
            );
            Object fixDexElements=getDexElementsByClassLoader(fixDexlassLoader);
            //3.把补丁的dexElement 插到已经
            //合并完成
            applicationDexElements=combineArray(fixDexElements,applicationDexElements);
        }
        //把合并的数组注入到原来的类中 applicationClassLoader
        injectDexElements(applicationClassLoader,applicationDexElements);
    }
}
