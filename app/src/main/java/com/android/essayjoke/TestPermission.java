package com.android.essayjoke;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.baselibrary.ioc.PermissionFail;
import com.android.baselibrary.ioc.PermissionSuccess;
import com.android.baselibrary.permission.PermissionHelper;
import com.android.baselibrary.permission.PermissionUtils;

/**
 * @author Created by freed
 * Created by freed on 2019/2/24.
 * Date:2019/2/24
 * @description
 */
public class TestPermission extends Activity {

    //打电话请求申请权限的请求码
    private static final int CALL_PHONE_REQUEST_CODE = 0x0011;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialPhonePermission();
    }

    public void dialPhonePermission() {
        Log.i("info","enter dialPhonePermission");
        //检测权限的字符串
        int permissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);//检测是否有电话的权限
        //返回值只有两个 授予PERMISSION_GRANTED 拒绝 PackageManager.PERMISSION_DENIED
        if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            //1.4.2如果有直接拨打
            callPhone();
        } else {
            //4.3如果没有那么去申请
            //申请的权限 ，请求码
            //系统方法
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_REQUEST_CODE);

//            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},CALL_PHONE_REQUEST_CODE);
            //自定义方法
            //        PermissionHelper.requestPermission(this,CALL_PHONE_REQUEST_CODE,new String[]{Manifest.permission.CALL_PHONE});

            PermissionHelper.with(this)
                    .requestCode(CALL_PHONE_REQUEST_CODE)
                    .requestermission(Manifest.permission.CALL_PHONE).request();
        }
    }

    @PermissionSuccess(requestCode = CALL_PHONE_REQUEST_CODE)
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "1234");
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);



    }
    @PermissionFail(requestCode = CALL_PHONE_REQUEST_CODE)
    private void callPhoneFail(){
        Toast.makeText(this,"您拒绝了拨打电话",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode==CALL_PHONE_REQUEST_CODE){
//            if(grantResults != null&&grantResults.length>0){
//                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    callPhone();
//                }else{
//                    Toast.makeText(this,"用户拒绝了拨打电话权限",Toast.LENGTH_SHORT).show();
//                }
//            }
//        }

        PermissionHelper.requestPermissionsResult(this,requestCode,permissions,grantResults);
    }
}
