package com.android.fragmentlibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/12.
 *         Date:2019/2/12
 * @description
 */

public interface IDaoSoupport<T> {
    public void init(SQLiteDatabase sqliteDatabases, Class<T> clazz);

    public long insert(T t);
    //批量插入
    public void insert(List<T> data);
    //获取专门查询的支持类
    public QuerySupport<T> querySupport();

    int delete(String whereClause,String[] whereArgs);

    int update(T obj, String whereClause, String... whereArgs);

    //按照语句查询
}
