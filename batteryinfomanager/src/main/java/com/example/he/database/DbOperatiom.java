//package com.example.he.database;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by HE on 2016/3/3.
// */
//public class DbOperatiom {
//    private MySQLiteOpenHelper helper;
//    private SQLiteDatabase database;
//
//    void DbOperation(Context context){
//        //数据库操作，新建库
//        helper = new MySQLiteOpenHelper(context, "battery_one.db", null, 1);
//    }
//
//     void createTable() {
//        //新建表
//        database = helper.getWritableDatabase();//调用方法，创建或打开链接
//         //database.execSQL("create table one(mid INTEGER PRIMARY KEY autoincrement);");
//         database.execSQL("DROP TABLE IF EXISTS one");
//         database.execSQL("CREATE TABLE one (b_id INTEGER PRIMARY KEY AUTOINCREMENT);");
//    }
//
//    void addColumn(String name){
//        //数据库操作，添加列
//        String sql = "alter table one add column " + name + " text;";
//        database.execSQL(sql);
//    }
//
//    void addValues(List<Map<String,String>> listItems , int num){
//        ContentValues cv = new ContentValues();
//        database.beginTransaction();
//        for (int j = 0; j < num; j++) {
//            Map<String, String> listItem = new HashMap<String, String>();
//            listItem = listItems.get(j);
//            cv.clear();
//            for (int i= 0;i<count;i++){
//                String key = tlistItem.get(String.valueOf(i));
//                cv.put(key,listItem.get(String.valueOf(i)));
//            }
//            database.insert("one", null, cv);
//        }
//        database.setTransactionSuccessful();
//        database.endTransaction();
//    }
//}
