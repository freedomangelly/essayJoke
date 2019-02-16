package com.android.baselibrary.base;

import com.android.baselibrary.ExceptionCrashHandle;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/14.
 *         Date:2019/2/14
 * @description
 */

public class BaseThread extends Thread{

    public BaseThread() {
        initThread();
    }

    public BaseThread(Runnable target) {
        super(target);
        initThread();
    }

    public BaseThread(ThreadGroup group, Runnable target) {
        super(group, target);
        initThread();
    }

    public BaseThread(String name) {
        super(name);
        initThread();
    }

    public BaseThread(ThreadGroup group, String name) {
        super(group, name);
        initThread();
    }

    public BaseThread(Runnable target, String name) {
        super(target, name);
        initThread();
    }

    public BaseThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
        initThread();
    }

    public BaseThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
        initThread();
    }

    private void initThread(){
        this.setUncaughtExceptionHandler(ExceptionCrashHandle.getInstance());
    }
}
