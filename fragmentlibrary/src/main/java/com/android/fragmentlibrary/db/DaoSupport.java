package com.android.fragmentlibrary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/12.
 *         Date:2019/2/12
 * @description
 */

public class DaoSupport<T> implements IDaoSoupport<T> {
    private String TAG = "DaoSupport";
    private SQLiteDatabase mSqliteDatabases;
    //泛型类
    private Class<T> mClazz;

    private static final Object[] mPutMethodArgs = new Object[2];

    private static final Map<String,Method> mPutMethods=new ArrayMap<>();



    public void init(SQLiteDatabase sqliteDatabases, Class<T> clazz){
        this.mSqliteDatabases = sqliteDatabases;
        this.mClazz=clazz;
        //创建表
        StringBuffer sb=new StringBuffer();
        sb.append("create table if not exists ")
                .append(DaoUtil.getTableName(mClazz))
                .append(" (id integer primary key autoincrement,");
        Field[] fields=mClazz.getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
            String name=field.getName();
            String type=field.getType().getSimpleName();
            //type 需要转换 int --> integer,Strring text
            sb.append(name).append(DaoUtil.getColumnType(type)).append(" ,");
        }
        sb.replace(sb.length()-2,sb.length(),")");
        String createTableSql=sb.toString();
        Log.i(TAG,"表语句-->"+createTableSql);
        //创建表
        mSqliteDatabases.execSQL(createTableSql);
    }
    @Override
    public long insert(T o) {
         //速度比第三方的快一倍左右
        ContentValues values=ContentValuesbyObj(o);
        return mSqliteDatabases.insert(DaoUtil.getTableName(mClazz),null,values);
    }

    @Override
    public void insert(List<T> datas) {
        Log.i("info","inserts="+datas.size());
        for (T data:datas){
            //调用单条插入
            insert(data);
        }
    }

    private ContentValues ContentValuesbyObj(T o) {
        //第三方的 使用比对一下 了解一下源码

        ContentValues values=new ContentValues();
        //封装values.put();
        Field[] fields=mClazz.getDeclaredFields();
        for (Field field:fields){
            try {
                field.setAccessible(true);
                String key=field.getName();
                //设置权限,私有和共有都可以方粉
                Object value=field.get(o);

                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;
                //put第二个参数是类型 把它转换
                //还是使用反射获取方法
                String filedTypeName=field.getType().getName();
                Method putMethod = mPutMethods.get(field.getType().getName());
                if(putMethod==null){
                    putMethod=ContentValues.class.getDeclaredMethod("put",String.class,value.getClass());
                    mPutMethods.put(filedTypeName,putMethod);
                }
                //缓存方法
//                putMethod.invoke(values,key,value);
                putMethod.invoke(values,mPutMethodArgs);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                mPutMethodArgs[0]=null;
                mPutMethodArgs[1]=null;
            }
        }
        return values;
    }

    //查询
    private QuerySupport<T> mQuarySupport;
    @Override
    public QuerySupport<T> querySupport() {
        if(mQuarySupport==null){
            mQuarySupport=new QuerySupport<>(mSqliteDatabases,mClazz);
        }
        return mQuarySupport;
    }


    //修改
    /**
     * 更新  这些你需要对  最原始的写法比较明了 extends
     */
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = ContentValuesbyObj(obj);
        return mSqliteDatabases.update(DaoUtil.getTableName(mClazz),
                values, whereClause, whereArgs);
    }
    //删除
    /**
     * 删除
     */
    public int delete(String whereClause, String[] whereArgs) {
        return mSqliteDatabases.delete(DaoUtil.getTableName(mClazz), whereClause, whereArgs);
    }



    // 结合到
    // 1. 网络引擎的缓存
    // 2. 资源加载的源码NDK

}
