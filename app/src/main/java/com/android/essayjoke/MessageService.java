package com.android.essayjoke;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.essayjoke.aidl.ProcessConnection;

/**
 * @author Created by freed
 * Created by freed on 2019/2/16.
 * Date:2019/2/16
 * @description QQ聊天通讯 需要轻
 */
public class MessageService extends Service {

    private static final int MessageId = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(2000);
                        Log.e("TAG","等待接受消息");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //提高优先级
        //前台进程
        startForeground(MessageId,new Notification());

        bindService(new Intent(this,GuardService.class),mServiceConnection, Context.BIND_IMPORTANT);
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //链接上
            Toast.makeText(MessageService.this, "建立链接", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开链接
            Toast.makeText(MessageService.this, "断开链接", Toast.LENGTH_SHORT).show();
            bindService(new Intent(MessageService.this,GuardService.class),mServiceConnection, Context.BIND_IMPORTANT);
        }
    };
}
