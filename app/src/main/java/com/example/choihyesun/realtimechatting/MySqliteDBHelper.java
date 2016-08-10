package com.example.choihyesun.realtimechatting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by choihyesun on 16. 8. 10..
 */
public class MySqliteDBHelper extends SQLiteOpenHelper{

    public MySqliteDBHelper(Context context){
        super(context, "chatDB", null, 1); // context - this - MainActivity
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE userTBL (studentNumber integer, name char(10), userId char(10) primary key, pswd char(15));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS userTBL");
        onCreate(db);
    }
}
