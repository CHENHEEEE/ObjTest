package com.example.he.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HE on 2016/2/25.
 * 使用单例模式
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;//版本号
    private static final String DBNAME = "battery.db";// 定义数据库名

    /* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */
    private static MySQLiteOpenHelper helper = null;

    /**
     * 构造方法，调用父类SQLiteOpenHelper的构造函数
     * 私有构造方法，防止被实例化
     * @param context  上下文环境
     */
    private MySQLiteOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    public static MySQLiteOpenHelper getInstance(Context context){
        if(helper == null){
            helper = new MySQLiteOpenHelper(context);
        }
        return helper;
    }

    public static MySQLiteOpenHelper getInstance(){
        return helper;
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
