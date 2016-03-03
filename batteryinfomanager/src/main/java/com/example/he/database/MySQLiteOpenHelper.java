package com.example.he.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HE on 2016/2/25.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper{
    /**
     * 构造方法，调用父类SQLiteOpenHelper的构造函数
     * @param context  上下文环境
     * @param name     数据库名称(以.db结尾)
     * @param factory  游标工厂(默认为null)
     * @param version  代表使用数据库模型版本的证书
     */
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * 代表使用数据库模型版本的证书
     * @param db          对象
     * @param oldVersion  旧版本号
     * @param newVersion  新版本号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
