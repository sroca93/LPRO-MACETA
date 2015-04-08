package com.anaroc.anaro.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Luis on 24/02/2015.
 */
public class SQLite extends SQLiteOpenHelper {
    String sqlOnCreate = "create table Usuarios (ID integer primary key, user text, pass text, flag integer)";
    String sqlOnCreateImages = "create table Imagenes (ID integer primary key, userID integer, path text)";


    public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlOnCreate);
        db.execSQL(sqlOnCreateImages);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Usuarios");
        db.execSQL("drop table if exists Imagenes");

        db.execSQL(sqlOnCreate);
        db.execSQL(sqlOnCreateImages);

    }
}
