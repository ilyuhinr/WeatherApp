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
import ru.example.weatherapp.services.ServiceCallbackListener;
import ru.example.weatherapp.services.ServiceHelper;
import ru.example.weatherapp.services.WeatherService;
import ru.example.weatherapp.utils.Constants;


public class MainActivity extends AppCompatActivity implements ServiceCallbackListener {
    private String LOG_TAG = this.getClass().getName();
    public static Handler mUiHandler = null;
    private ContentObserver mObserver;
    MaterialRefreshLayout mMaterialRefreshLayout;
    ServiceHelper mServiceHelper;
    private boolean isRefreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.text_);
        DBHelper dbHelper = new DBHelper(this);
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        mServiceHelper = new ServiceHelper();
        mServiceHelper.setServiceCallbackListener(this);

        mMaterialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        mMaterialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                isRefreshing = true;
                Intent intent = mServiceHelper.createIntent(MainActivity.this, "Russia", "Bryansk", 0);
                startService(intent);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

            }
        });
        mMaterialRefreshLayout.setWaveColor(0x903F51B5);
        mMaterialRefreshLayout.setIsOverLay(false);
        mMaterialRefreshLayout.setWaveShow(true);

        mObserver = new ContentObserver(new Handler()) {
            public void onChange(boolean selfChange) {
                isRefreshing = false;
                mMaterialRefreshLayout.finishRefresh();
                Toast.makeText(MainActivity.this, "ContentObserver", Toast.LENGTH_LONG).show();
            }
        };
        getContentResolver().registerContentObserver(Constants.ASTRONOMY_CONTENT_URI, true, mObserver);


    }


    @Override
    public void onServiceCallback(int requestId, int resultCode, Bundle data) {
        if (resultCode == -1) {
            if (isRefreshing) {
                mMaterialRefreshLayout.finishRefresh();
                Toast.makeText(this, "Произошла ошибка при получении данных. Повторите попытку позже!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
