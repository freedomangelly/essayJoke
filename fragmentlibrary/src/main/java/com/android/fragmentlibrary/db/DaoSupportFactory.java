package com.android.fragmentlibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/12.
 *         Date:2019/2/12
 * @description
 */

public class DaoSupportFactory {
    private static DaoSupportFactory mFactory;
    private SQLiteDatabase mSqliteDatabase;
    private DaoSupportFactory(){
        //TODO 把数据库放到内存卡里面 判断是否有存储卡 6.0要动态申请权限
        //把数据库放到内存卡里面 判断是否有存储卡 6.0要动态申请权限
        File dbRoot=new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator+"nhdz"+File.separator+"databases");

        if(!dbRoot.exists()){
            dbRoot.mkdirs();
        }

        File dbFile=new File(dbRoot,"nhdz.db");

        //打开或者创建数据
        mSqliteDatabase=SQLiteDatabase.openOrCreateDatabase(dbFile,null);
    }

    public static DaoSupportFactory getFactory(){
        if(mFactory == null){
            synchronized (DaoSupportFactory.class){
                if(mFactory == null){
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    public <T>IDaoSoupport<T> getDao(Class<T> clazz){
        IDaoSoupport<T> daoSoupport=new DaoSupport<T>();
        daoSoupport.init(mSqliteDatabase,clazz);
        return daoSoupport;
    }
}
