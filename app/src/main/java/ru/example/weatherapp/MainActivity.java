package ru.example.weatherapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ru.example.weatherapp.database.DBHelper;
import ru.example.weatherapp.services.WeatherService;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.text_);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        /*values.put("sunrise", "test");
        values.put("sunset", "test");

        sqLiteDatabase.insert("astronomy", null, values);*/
       /* Cursor c = sqLiteDatabase.rawQuery("SELECT sunrise, sunset FROM astronomy", null);
        if (c.moveToFirst()) {
            c.getString(0);
            c.getString(1);
        }
        sqLiteDatabase.close();*/
        Intent intent = new Intent(getBaseContext(), WeatherService.class);
        startService(intent);
    }
}
