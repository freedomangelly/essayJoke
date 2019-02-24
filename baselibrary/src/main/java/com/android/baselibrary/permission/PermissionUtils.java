package com.android.baselibrary.permission;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.baselibrary.ioc.PermissionFail;
import com.android.baselibrary.ioc.PermissionSuccess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by freed
 * Created by freed on 2019/2/24.
 * Date:2019/2/24
 * @description 处理权限请求的工具类
 */
public class PermissionUtils {
    //这个类都是静态方法 ，不能让别人去new对象
    private PermissionUtils(){
        throw  new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断是不是6.0以上的版本
     * @return
     */
    public static boolean isOverMarshllow(){
        return Build.VERSION.SDK_INT >=Build.VERSION_CODES.M;
    }

    /**
     * 执行成功的方法
     * @param reflectObject 反射的队形
     * @param requestCode 获取传入的权限请求值
     */
    public static void executeSuccessMethod(Object reflectObject, int requestCode) {
        //获取class中所有的方法
        Method[] methods = reflectObject.getClass().getDeclaredMethods();

        // 去遍历找我们打了标记的方法
        for(Method method : methods){
            Log.i("info","executeSuccessMethod="+method.getName());
            //获取方法上有没有打这个成功的标记
            PermissionSuccess successMethod=method.getAnnotation(PermissionSuccess.class);

            if(successMethod!=null){
                Log.i("info","executeSuccessMethod != null");
                //代表该方法打了标记
                //并且我们的请求码必须requestCode一样
                int methodCode=successMethod.requestCode();
                if(methodCode == requestCode){
                    //这个就是我们要找的成功方法
                    executeMethod(reflectObject,method);
                }
            }else {
                Log.i("info","executeSuccessMethod == null");
            }
        }

        //并且我们的请求码，必须跟requestCode一样

        //反射执行该方法
    }

    private static void executeMethod(Object reflectObject, Method method) {
        //反射执行方法 第一个
        Log.i("info","executeMethod reflectObject="+reflectObject.getClass().toString()+",method="+method.getName());
        try {
            method.setAccessible(true);
            method.invoke(reflectObject,new Object[]{});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取没有授予的权限
     * @param mObject
     * @param mRequstPermission
     * @return 没有授予过的权限
     */
    public static List<String> getDeniedPermissions(Object mObject, String[] mRequstPermission) {
        List<String> denidePermissions=new ArrayList<String>();
        for (String requestPermission : mRequstPermission){
            //把拒绝的权限加入到集合
            if(ContextCompat.checkSelfPermission(getActivity(mObject),requestPermission)== PackageManager.PERMISSION_DENIED){
                denidePermissions.add(requestPermission);
            }
        }

        return denidePermissions;
    }

    public static Activity getActivity(Object mObject) {
        if(mObject instanceof Activity){
            return (Activity) mObject;
        }
        if(mObject instanceof Fragment){
            return ((Fragment)mObject).getActivity();
        }
        return null;
    }

    /**
     * 执行失败的方法
     * @param object
     * @param requestCode
     */
    public static void executeFailMethod(Object object, int requestCode) {
        Method[] methods = object.getClass().getDeclaredMethods();

        // 去遍历找我们打了标记的方法
        for(Method method : methods){
            //获取方法上有没有打这个失败的标记
            PermissionFail failMethod=method.getAnnotation(PermissionFail.class);
            if(failMethod!=null){
                //代表该方法打了标记
                //并且我们的请求码必须requestCode一样
                int methodCode=failMethod.requestCode();
                if(methodCode == requestCode){
                    //这个就是我们要找的成功方法
                    executeMethod(object,method);
                }
            }
        }
    }


}
