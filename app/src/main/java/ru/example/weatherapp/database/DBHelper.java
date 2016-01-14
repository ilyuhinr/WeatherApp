package ru.example.weatherapp.database;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import ru.example.weatherapp.utils.Constants;

/**
 * Created by developer on 13.01.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.SQL_LITE_CREATE_TABLE_ASTRONOMY);
        db.execSQL(Constants.SQL_LITE_CREATE_TABLE_ATMOSPHERE);
        db.execSQL(Constants.SQL_LITE_CREATE_TABLE_CONDITION);
        db.execSQL(Constants.SQL_LITE_CREATE_TABLE_ITEM);
        db.execSQL(Constants.SQL_LITE_CREATE_TABLE_FORECAST);
        db.execSQL(Constants.SQL_LITE_CREATE_TABLE_CITY);
        db.execSQL(Constants.SQL_LITE_CREATE_TABLE_IMAGE);
        db.execSQL(Constants.SQL_LITE_CREATE_TABLE_LOCATION);
        db.execSQL(Constants.SQL_LITE_CREATE_TABLE_UNITS);
        db.execSQL(Constants.SQL_LITE_CREATE_TABLE_WIND);
        db.execSQL(Constants.SQL_LITE_CREATE_TABLE_CHANNEL);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}