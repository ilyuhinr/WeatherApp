package ru.example.weatherapp.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.example.weatherapp.utils.Constants;

public class WeatherProvider extends ContentProvider {
    private String LOG_TAG = getClass().getName();
    public static final String AUTHORITY = "com.example.weatherApp";

    public static final String PATH_INFO = "weather";
    public static final Uri WEATHER_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + PATH_INFO);
    static final String WEATHER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + PATH_INFO;
    static final String WEATHER_CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + PATH_INFO;
    static final int URI_CHANEL = 1;
    static final int URI_CHANEL_ID = 2;
    static final int URI_CITY = 3;
    static final int URI_CITY_ID = 4;
    static final int ASTRONOMY_URI = 5;
    static final int ATMOSPHERE_URI = 6;
    static final int CONDITION_URI = 8;
    static final int FORECAST_URI = 9;
    static final int IMAGE_URI = 10;
    static final int ITEM_URI = 11;
    static final int LOCATION_URI = 12;
    static final int UNITS_URI = 13;
    static final int WIND_URI = 14;

    static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, Constants.CHANEL_TABLE_NAME, URI_CHANEL);
        sUriMatcher.addURI(AUTHORITY, Constants.CHANEL_TABLE_NAME + "/#", URI_CHANEL_ID);
        sUriMatcher.addURI(AUTHORITY, Constants.ASTRONOMY_TABLE_NAME, ASTRONOMY_URI);
        sUriMatcher.addURI(AUTHORITY, Constants.ATMOSPHERE_TABLE_NAME, ATMOSPHERE_URI);
        sUriMatcher.addURI(AUTHORITY, Constants.CONDITION_TABLE_NAME, CONDITION_URI);
        sUriMatcher.addURI(AUTHORITY, Constants.FORECAST_TABLE_NAME, FORECAST_URI);
        sUriMatcher.addURI(AUTHORITY, Constants.IMAGE_TABLE_NAME, IMAGE_URI);
        sUriMatcher.addURI(AUTHORITY, Constants.ITEM_TABLE_NAME, ITEM_URI);
        sUriMatcher.addURI(AUTHORITY, Constants.LOCATION_TABLE_NAME, LOCATION_URI);
        sUriMatcher.addURI(AUTHORITY, Constants.UNITS_TABLE_NAME, UNITS_URI);
        sUriMatcher.addURI(AUTHORITY, Constants.WIND_TABLE_NAME, WIND_URI);
        sUriMatcher.addURI(AUTHORITY, Constants.CITY_TABLE_NAME, URI_CITY);
        sUriMatcher.addURI(AUTHORITY, Constants.CITY_TABLE_NAME + "/#", URI_CITY_ID);
    }

    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String table = "";
        switch (sUriMatcher.match(uri)) {
            case URI_CHANEL:
                table = Constants.CHANEL_TABLE_NAME;
                break;
            case URI_CHANEL_ID:
                table = Constants.CHANEL_TABLE_NAME;
                String idChannel = uri.getLastPathSegment();
                if (selection.isEmpty()) {
                    selection = "id" + " = " + idChannel;
                } else {
                    selection = selection + " AND id = " + idChannel;
                }
                break;
            case ASTRONOMY_URI:
                table = Constants.ASTRONOMY_TABLE_NAME;
                break;
            case ATMOSPHERE_URI:
                table = Constants.ATMOSPHERE_TABLE_NAME;
                break;
            case CONDITION_URI:
                table = Constants.CONDITION_TABLE_NAME;
                break;
            case FORECAST_URI:
                table = Constants.FORECAST_TABLE_NAME;
                break;
            case IMAGE_URI:
                table = Constants.IMAGE_TABLE_NAME;
                break;
            case ITEM_URI:
                table = Constants.ITEM_TABLE_NAME;
                break;
            case LOCATION_URI:
                table = Constants.LOCATION_TABLE_NAME;
                break;
            case UNITS_URI:
                table = Constants.UNITS_TABLE_NAME;
                break;
            case WIND_URI:
                table = Constants.WIND_TABLE_NAME;
                break;
            case URI_CITY:
                table = Constants.CITY_TABLE_NAME;
                break;
            case URI_CITY_ID:
                table = Constants.CITY_TABLE_NAME;
                String idCity = uri.getLastPathSegment();
                if (selection.isEmpty()) {
                    selection = "id" + " = " + idCity;
                } else {
                    selection = selection + " AND id = " + idCity;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (!table.isEmpty()) {
            SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.query(table, projection, selection,
                    selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(),
                    WEATHER_CONTENT_URI);
            return cursor;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        Uri resultUri = null;
        String table = "";
        switch (sUriMatcher.match(uri)) {
            case URI_CHANEL:
                table = Constants.CHANEL_TABLE_NAME;
                break;
            case ASTRONOMY_URI:
                table = Constants.ASTRONOMY_TABLE_NAME;
                break;
            case ATMOSPHERE_URI:
                table = Constants.ATMOSPHERE_TABLE_NAME;
                break;
            case CONDITION_URI:
                table = Constants.CONDITION_TABLE_NAME;
                break;
            case FORECAST_URI:
                table = Constants.FORECAST_TABLE_NAME;
                break;
            case IMAGE_URI:
                table = Constants.IMAGE_TABLE_NAME;
                break;
            case ITEM_URI:
                table = Constants.ITEM_TABLE_NAME;
                break;
            case LOCATION_URI:
                table = Constants.LOCATION_TABLE_NAME;
                break;
            case UNITS_URI:
                table = Constants.UNITS_TABLE_NAME;
                break;
            case WIND_URI:
                table = Constants.WIND_TABLE_NAME;
                break;
            case URI_CITY:
                table = Constants.CITY_TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        if (!table.isEmpty()) {
            SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();
            long id = sqLiteDatabase.insert(table, null, values);
            resultUri = ContentUris.withAppendedId(Constants.ASTRONOMY_CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(resultUri, null, false);
        }
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    public class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
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
            Log.i(DatabaseHelper.class.getName(),
                    "Create tables database!");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(DatabaseHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + Constants.ASTRONOMY_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.ATMOSPHERE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.CHANEL_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.CONDITION_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.FORECAST_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.IMAGE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.ITEM_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.LOCATION_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.UNITS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.WIND_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.CITY_TABLE_NAME);
            onCreate(db);
        }
    }
}
