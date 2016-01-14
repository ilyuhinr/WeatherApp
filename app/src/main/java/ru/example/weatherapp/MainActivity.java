package ru.example.weatherapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import ru.example.weatherapp.database.DBHelper;
import ru.example.weatherapp.services.WeatherService;
import ru.example.weatherapp.utils.Constants;


public class MainActivity extends AppCompatActivity {
    private String LOG_TAG = this.getClass().getName();
    public static Handler mUiHandler = null;
    private ContentObserver mObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.text_);
        DBHelper dbHelper = new DBHelper(this);
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        final MaterialRefreshLayout materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                Intent intent = new Intent(MainActivity.this, WeatherService.class);
                startService(intent);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

            }
        });
        materialRefreshLayout.setWaveColor(0x903F51B5);
        materialRefreshLayout.setIsOverLay(false);
        materialRefreshLayout.setWaveShow(true);

        mObserver = new ContentObserver(new Handler()) {
            public void onChange(boolean selfChange) {
                materialRefreshLayout.finishRefresh();
                Toast.makeText(MainActivity.this, "ContentObserver", Toast.LENGTH_LONG).show();
            }
        };
        getContentResolver().registerContentObserver(Constants.ASTRONOMY_CONTENT_URI, true, mObserver);

    }

    public void buildUIHandler() {
        mUiHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                        break;

                    default:
                        break;
                }
            }
        };
    }
}
