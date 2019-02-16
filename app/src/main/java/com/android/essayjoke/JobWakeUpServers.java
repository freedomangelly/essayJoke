package com.android.essayjoke;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import java.util.List;

/**
 * @author Created by freed
 * Created by freed on 2019/2/16.
 * Date:2019/2/16
 * @description
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//5.0以上才有
public class JobWakeUpServers extends JobService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启一个轮询
        int jobWakeUpId = 1;
        JobInfo.Builder jobBuider = new JobInfo.Builder(jobWakeUpId,new ComponentName(this,JobWakeUpServers.class));
        jobBuider.setPeriodic(2000);

        JobScheduler jobScheduler= (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobBuider.build());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        //开启定时任务，定时轮询，看messageService有没有被杀死
        //如果杀死了启动， 罗内训onstartJob

        //判断服务有没有在运行
        boolean messageServiceAlive=isServiceRunning(this,MessageService.class.getName());
        if(!messageServiceAlive){
            startService(new Intent(this,MessageService.class));
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    /**
     * 判断Service是否正在运行
     *
     * @param context     上下文
     * @param serviceName Service 类全名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList = manager.getRunningServices(200);
        if (serviceInfoList.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo info : serviceInfoList) {
            if (info.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}
