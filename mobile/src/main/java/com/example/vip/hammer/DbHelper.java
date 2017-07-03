package com.example.vip.hammer;

/**
 * Created by vip on 2017-05-22.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by SongWook on 2015-07-17.
 */


public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DB_TEST";
    private static final String TEXT_TYPE = " TEXT";
    private static final String Double_TYPE = " Double";

    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DBcontract.DBEntry.TABLE_NAME + "(" +
            //  DBcontract.DBEntry._ID + " INTEGER PRIMARY KEY," +
            DBcontract.DBEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            DBcontract.DBEntry.COLUMN_NAME_x + Double_TYPE + COMMA_SEP +
            DBcontract.DBEntry.COLUMN_NAME_y + Double_TYPE + COMMA_SEP +
            DBcontract.DBEntry.COLUMN_NAME_z + Double_TYPE + COMMA_SEP +
            DBcontract.DBEntry.COLUMN_NAME_TIME + TEXT_TYPE + " );";

    private static final String SQL_CREATE_ENTRIES2 = "CREATE TABLE " + DBcontract.DBEntry2.TABLE_NAME + "(" +
            //  DBcontract.DBEntry._ID + " INTEGER PRIMARY KEY," +
            DBcontract.DBEntry2.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            DBcontract.DBEntry2.COLUMN_NAME_x + Double_TYPE + COMMA_SEP +
            DBcontract.DBEntry2.COLUMN_NAME_y + Double_TYPE + COMMA_SEP +
            DBcontract.DBEntry2.COLUMN_NAME_z + Double_TYPE + COMMA_SEP +
            DBcontract.DBEntry2.COLUMN_NAME_TIME + TEXT_TYPE + " );";
/*
    private static final String SQL_CREATE_ENTRIES3 = "CREATE TABLE " + DBcontract.DBEntry3.TABLE_NAME + "(" +
            //  DBcontract.DBEntry._ID + " INTEGER PRIMARY KEY," +
            DBcontract.DBEntry3.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            DBcontract.DBEntry3.COLUMN_NAME_MIC + Double_TYPE + COMMA_SEP +
            DBcontract.DBEntry3.COLUMN_NAME_TIME + TEXT_TYPE + " );";
*/

    private static final String SQL_CREATE_ENTRIES4 = "CREATE TABLE " + DBcontract.DBEntry4.TABLE_NAME + "(" +
            //  DBcontract.DBEntry._ID + " INTEGER PRIMARY KEY," +
            DBcontract.DBEntry4.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            DBcontract.DBEntry4.COLUMN_NAME_x + Double_TYPE + COMMA_SEP +
            DBcontract.DBEntry4.COLUMN_NAME_y + Double_TYPE + COMMA_SEP +
            DBcontract.DBEntry4.COLUMN_NAME_z + Double_TYPE + COMMA_SEP +
            DBcontract.DBEntry4.COLUMN_NAME_TIME + TEXT_TYPE + " );";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBcontract.DBEntry.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "any8.db";

    public static final String FILE_PATH = "/sdcard/Test/" + DATABASE_NAME;

    public DbHelper(Context context)
    {
        super(context, FILE_PATH,null,DATABASE_VERSION);
    }
    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES2);
        db.execSQL(SQL_CREATE_ENTRIES4);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES2);
        db.execSQL(SQL_CREATE_ENTRIES4);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addAccelerometer(String title, double x, double y, double z, String time, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBcontract.DBEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(DBcontract.DBEntry.COLUMN_NAME_x, x);
        contentValues.put(DBcontract.DBEntry.COLUMN_NAME_y, y);
        contentValues.put(DBcontract.DBEntry.COLUMN_NAME_z, z);
        contentValues.put(DBcontract.DBEntry.COLUMN_NAME_TIME,time);
        db.insert(DBcontract.DBEntry.TABLE_NAME, null, contentValues);

        Log.e("db", "database One row inserted..." + FILE_PATH + "time:" + time);
    }

    public void addPhoneAccelerometer(String title, double x, double y, double z, String time, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBcontract.DBEntry4.COLUMN_NAME_TITLE, title);
        contentValues.put(DBcontract.DBEntry4.COLUMN_NAME_x, x);
        contentValues.put(DBcontract.DBEntry4.COLUMN_NAME_y, y);
        contentValues.put(DBcontract.DBEntry4.COLUMN_NAME_z, z);
        contentValues.put(DBcontract.DBEntry4.COLUMN_NAME_TIME,time);
        db.insert(DBcontract.DBEntry4.TABLE_NAME, null, contentValues);

        Log.e("db", "database One row inserted..." + FILE_PATH + "time:" + time);


    }
    public void addGyroscope (String title, double x, double y, double z, String time, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBcontract.DBEntry2.COLUMN_NAME_TITLE, title);
        contentValues.put(DBcontract.DBEntry2.COLUMN_NAME_x, x);
        contentValues.put(DBcontract.DBEntry2.COLUMN_NAME_y, y);
        contentValues.put(DBcontract.DBEntry2.COLUMN_NAME_z, z);
        contentValues.put(DBcontract.DBEntry2.COLUMN_NAME_TIME,time);
        db.insert(DBcontract.DBEntry2.TABLE_NAME, null, contentValues);

        Log.e("db","database One row inserted..."+FILE_PATH+"time:"+time);


    }
    public void addMIC(String title, double mic, String time, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBcontract.DBEntry3.COLUMN_NAME_TITLE, title);
        contentValues.put(DBcontract.DBEntry3.COLUMN_NAME_MIC, mic);
        contentValues.put(DBcontract.DBEntry3.COLUMN_NAME_TIME,time);
        db.insert(DBcontract.DBEntry3.TABLE_NAME, null, contentValues);


    }
}
