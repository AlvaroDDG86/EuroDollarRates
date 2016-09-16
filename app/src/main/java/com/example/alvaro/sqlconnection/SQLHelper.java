package com.example.alvaro.sqlconnection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alvaro on 05/06/2016.
 */
public class SQLHelper extends SQLiteOpenHelper {
    String SQLCreateEUR = "CREATE TABLE IF NOT EXISTS RatesEUR(name TEXT PRIMARY KEY, price REAL)";
    String SQLCreateDOL = "CREATE TABLE IF NOT EXISTS RatesDOL(name TEXT PRIMARY KEY, price REAL)";
    String SQLCreateDate = "CREATE TABLE IF NOT EXISTS Date(date TEXT PRIMARY KEY)";

    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLCreateEUR);
        db.execSQL(SQLCreateDOL);
        db.execSQL(SQLCreateDate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
