package com.k3.rtlion.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HostDB extends SQLiteOpenHelper {
    private static final String databaseName = "RTLion_db";
    public String tableName = "hosts_table", dataName = "hosts";
    private static final int ver = 1;
    private SQLiteDatabase hostDB;
    private Cursor dbCursor;
    private ContentValues contentValues;

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
    public String getHostAddr(){
        String hostAddr = null;
        try {
            hostDB = getWritableDatabase();
            String[] hostColumn = {dataName};
            dbCursor = hostDB.query(tableName, hostColumn, null, null, null, null, null);
            while (dbCursor.moveToNext()) {
                hostAddr = dbCursor.getString(dbCursor.getColumnIndex(dataName));
            }
            hostDB.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return hostAddr;
    }
    public void updateHostAddr(String newData){
        try {
            hostDB = getWritableDatabase();
            hostDB.execSQL("DELETE FROM " + tableName);
            contentValues = new ContentValues();
            contentValues.put(dataName, newData);
            hostDB.insertOrThrow(tableName, null, contentValues);
            hostDB.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}