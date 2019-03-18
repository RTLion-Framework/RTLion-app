package com.k3.rtlion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HostDB extends SQLiteOpenHelper {
    private static final String databaseName = "RTLion_db";
    public String tableName = "hosts_table", dataName = "hosts";
    private static final int ver = 1;
    public HostDB (Context context){
        super(context, databaseName, null, ver);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + tableName + " (" + dataName + " TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        try {
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
            onCreate(db);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}