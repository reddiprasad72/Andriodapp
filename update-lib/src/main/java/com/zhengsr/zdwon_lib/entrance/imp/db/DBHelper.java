package com.zhengsr.zdwon_lib.entrance.imp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static String URL = "url";
    public static String THREADID = "threadId";
    public static String THREAD_LENGTH = "threadLength";
    public static String THREAD_START = "startPos";
    public static String THREAD_END = "endPos";
    private static final String TABLE = "zdown.db";
    public static final String BOOK_TABLE = "zdown";
    private static final String SQL_CREATE = "create table "+BOOK_TABLE+" ("
            + "_id integer primary key autoincrement, "
            + URL+" text, "
            + THREADID +" integer, "
            + THREAD_START +" integer, "
            + THREAD_END +" integer, "
            + THREAD_LENGTH+" integer)";

    private static final String SQL_DROP = "drop table if exists "+BOOK_TABLE;

    public DBHelper(Context context) {
        super(context, TABLE, null, 4);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }
}