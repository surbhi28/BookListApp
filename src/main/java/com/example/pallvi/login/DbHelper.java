package com.example.pallvi.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pallvi.login.inv.invClass;


public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "test1.db";

    private static final int DATABASE_VERSION = 3;

    public DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_INV_TABLE = "CREATE TABLE " + invClass.TABLE_NAME + "(" +
                invClass.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                invClass.COLUMN_UNAME + " TEXT NOT NULL," +
                invClass.COLUMN_ADDRESS + " INTEGER NOT NULL," +
                invClass.COLUMN_UDOB + " INTEGER NOT NULL DEFAULT 0," +
                invClass.COLUMN_GENDER + " TEXT," +
                invClass.COLUMN_PHONE + " TEXT," +
                invClass.COLUMN_EMAIL + " TEXT," +
                invClass.COLUMN_PASS + " TEXT," +
                invClass.COLUMN_IMAGE + " TEXT);";
        db.execSQL(SQL_CREATE_INV_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * Storing user details in database
     */














}
