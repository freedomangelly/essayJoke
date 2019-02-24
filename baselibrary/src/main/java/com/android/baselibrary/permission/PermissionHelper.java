package com.android.baselibrary.permission;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;

/**
 * @author Created by freed
 * Created by freed on 2019/2/24.
 * Date:2019/2/24
 * @description
 */
public class PermissionHelper {
    //Object Fragment or Activity 2. int request code 3.需要请求的权限String[]
    private Object mObject;
    private int mRequestCode;
    private String[] mRequstPermission;
    //以什么样的方式传参数 以什么样的方式参数


    public PermissionHelper(Object mObject) {
        this.mObject = mObject;
    }

    //以什么样的方式传参数
    //2.1直接传参数
    public static void  requestPermission(Activity activity,int requestCode,String[] permission){
        PermissionHelper.with(activity).requestCode(requestCode).requestermission(permission).request();
    }

    //2.2链式的方式传Activity
    public static PermissionHelper with(Activity activity){
        return new PermissionHelper(activity);
    }
    //2.2链式的方式传fragment
    public static PermissionHelper with(Fragment fragment){
        return new PermissionHelper(fragment);
    }
    //2.3链式的方式传requestcode 请求码
    public PermissionHelper requestCode(int requestcode){
        this.mRequestCode=requestcode;
        return this;
    }
    //2.4链式调用方式传请求权限数组
    public PermissionHelper requestermission(String... permission){
        this.mRequstPermission=permission;
        return this;
    }

    /**
     * 3.0真正判断和发起请求权限
     */
    public void request() {
        Log.i("info","request="+PermissionUtils.isOverMarshllow());
        //3.2首先判断当前的版本是不是6.0以上
        if(!PermissionUtils.isOverMarshllow()){
            //如果不是6.0以上 那么直接执行方法 反射获取执行方法
            //执行什么方法并不确定 那么我们只能采用注解的方式给方法打一个标记
            //然后通过反射去执行
            PermissionUtils.executeSuccessMethod(mObject.getClass(),mRequestCode);
            return;
        }



        List<String> deniedPermissions=PermissionUtils.getDeniedPermissions(mObject,mRequstPermission);
        //3.3如果不是6.0以上 那么直接执行方法 反射获取执行方法

        //3.4如果是6.0以上，那么首先需要判断权限是否授予
        if(deniedPermissions.size() == 0){
            //全部都是授予过的
            PermissionUtils.executeSuccessMethod(mObject.getClass(),mRequestCode);
        }else {
            //3.3.2如果没有授予 那么我们就申请权限 申请权限
            ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject),deniedPermissions.toArray(new String[deniedPermissions.size()]),mRequestCode);
        }
        //如果授予了 那么我们直接执行方法 反射获取执行方法

        //如果没有授予 那么我们就申请权限
    }

    /**
     * 申请权限的回调
     * @param requestCode 请求码
     * @param permissions 请求权限
     * @param grantResults 结果
     */
    public static void requestPermissionsResult(Object object,int requestCode, String[] permissions, int[] grantResults) {
        Log.i("info","requestPermissionsResult");
        //再次获取没有授予的权限
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(object,permissions);

        if(deniedPermissions.size() == 0){
            //这些权限用户都同意了授予了
            Log.i("info","requestPermissionsResult success");
            PermissionUtils.executeSuccessMethod(object,requestCode);
        }else {
            //用户不同意你申请的权限中，有用户不同意的
            Log.i("info","requestPermissionsResult fail");
            PermissionUtils.executeFailMethod(object,requestCode);
        }

    }
}
