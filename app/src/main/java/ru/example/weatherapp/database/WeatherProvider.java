package ru.example.weatherapp.database;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.example.weatherapp.utils.Constants;

public class WeatherProvider extends ContentProvider {
    private String LOG_TAG = getClass().getName();
    public static final String AUTHORITY = "com.example.weatherApp";

    public static final String PATH_INFO = "weather";
    public static final Uri WEATHER_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + PATH_INFO);
    public static final Uri WEATHER_REFRESH_URI = Uri.parse("content://"
            + AUTHORITY + "/" + PATH_INFO + "/refresh");
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
    static final int DELETE_ALL = 15;
    static final int GET_CHANNEL = 16;
    static final int FORECAST_ID = 17;

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
        sUriMatcher.addURI(AUTHORITY, Constants.CHANEL_TABLE_NAME + "/deleteAll", DELETE_ALL);
        sUriMatcher.addURI(AUTHORITY, Constants.CHANEL_TABLE_NAME + "/model", GET_CHANNEL);
        sUriMatcher.addURI(AUTHORITY, Constants.FORECAST_TABLE_NAME + "/#", FORECAST_ID);
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
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case URI_CHANEL:
                table = Constants.CHANEL_TABLE_NAME;
                if (selection != null && selection.contains("city") && selection.contains("country")) {
                    Cursor cursor = sqLiteDatabase.query(Constants.CHANEL_TABLE_NAME
                                    + " LEFT JOIN " + Constants.LOCATION_TABLE_NAME + " as loc ON "
                                    + "loc." + Constants.ColumnLocation.ID + " = " + Constants.CHANEL_TABLE_NAME + "." + Constants.ColumnChannel.LOCATION_ID.toString()
                            , projection, selection,
                            selectionArgs, null, null, null);
                    return cursor;
                }
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
            case FORECAST_ID:
                table = Constants.FORECAST_TABLE_NAME;
                String idForecast = uri.getLastPathSegment();
                if (selection.isEmpty()) {
                    selection = "id" + " = " + idForecast;
                } else {
                    selection = selection + " AND id = " + idForecast;
                }
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
            case GET_CHANNEL:
                return getChannelModel(selectionArgs);
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
                sqLiteDatabase.close();
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (!table.isEmpty()) {
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

    private Cursor getChannelModel(String[] location) {
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        return sqLiteDatabase.rawQuery("SELECT*FROM channel LEFT JOIN wind on wind.id = channel.wind_id " +
                "LEFT JOIN location on location.id = channel.location_id " +
                "LEFT JOIN astronomy on astronomy.id = channel.astronomy_id " +
                "LEFT JOIN atmosphere on atmosphere.id = channel.atmosphere_id " +
                "LEFT JOIN units on units.id = channel.units_id " +
                "LEFT JOIN item on item.id = channel.item_id " +
                "INNER JOIN condition on item.condition_id = condition.id " +
                "LEFT JOIN forecast on forecast.item_id = item.id " +
                "WHERE channel.id =?", location);
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
            resultUri = ContentUris.withAppendedId(WEATHER_CONTENT_URI, id);
            if (table.equals(Constants.CHANEL_TABLE_NAME))
                getContext().getContentResolver().notifyChange(WEATHER_REFRESH_URI, null, false);
        }
        return resultUri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case DELETE_ALL:
                String idsWind = "SELECT " + Constants.WIND_TABLE_NAME + ".id as row_id FROM " + Constants.WIND_TABLE_NAME +
                        " LEFT JOIN " + Constants.CHANEL_TABLE_NAME + " ON " + Constants.CHANEL_TABLE_NAME + "." + Constants.ColumnChannel.WIND_ID + " = " + Constants.WIND_TABLE_NAME + ".id" +
                        " WHERE " + Constants.CHANEL_TABLE_NAME + ".id = " + selectionArgs[0];
                String idsAstronomy = "SELECT " + Constants.ASTRONOMY_TABLE_NAME + ".id as row_id FROM " + Constants.ASTRONOMY_TABLE_NAME +
                        " LEFT JOIN " + Constants.CHANEL_TABLE_NAME + " ON " + Constants.CHANEL_TABLE_NAME + "." + Constants.ColumnChannel.ASTRONOMY_ID + " = " + Constants.ASTRONOMY_TABLE_NAME + ".id" +
                        " WHERE " + Constants.CHANEL_TABLE_NAME + ".id = " + selectionArgs[0];
                String idsAtmosphere = "SELECT " + Constants.ATMOSPHERE_TABLE_NAME + ".id as row_id FROM " + Constants.ATMOSPHERE_TABLE_NAME +
                        " LEFT JOIN " + Constants.CHANEL_TABLE_NAME + " ON " + Constants.CHANEL_TABLE_NAME + "." + Constants.ColumnChannel.ATMOSPHERE_ID + " = " + Constants.ATMOSPHERE_TABLE_NAME + ".id" +
                        " WHERE " + Constants.CHANEL_TABLE_NAME + ".id = " + selectionArgs[0];
                String idsLocation = "SELECT " + Constants.LOCATION_TABLE_NAME + ".id as row_id FROM " + Constants.LOCATION_TABLE_NAME +
                        " LEFT JOIN " + Constants.CHANEL_TABLE_NAME + " ON " + Constants.CHANEL_TABLE_NAME + "." + Constants.ColumnChannel.LOCATION_ID + " = " + Constants.LOCATION_TABLE_NAME + ".id" +
                        " WHERE " + Constants.CHANEL_TABLE_NAME + ".id = " + selectionArgs[0];
                String idsUnits = "SELECT " + Constants.UNITS_TABLE_NAME + ".id as row_id FROM " + Constants.UNITS_TABLE_NAME +
                        " LEFT JOIN " + Constants.CHANEL_TABLE_NAME + " ON " + Constants.CHANEL_TABLE_NAME + "." + Constants.ColumnChannel.UNITS_ID + " = " + Constants.UNITS_TABLE_NAME + ".id" +
                        " WHERE " + Constants.CHANEL_TABLE_NAME + ".id = " + selectionArgs[0];
                String idsForecast = "SELECT " + Constants.FORECAST_TABLE_NAME + ".id as row_id FROM " + Constants.FORECAST_TABLE_NAME +
                        " LEFT JOIN " + Constants.ITEM_TABLE_NAME + " ON " + Constants.ITEM_TABLE_NAME + "." + Constants.ColumnItem.ID + " = " + Constants.FORECAST_TABLE_NAME + "." + Constants.ColumnForecast.ITEM_ID.toString() +
                        " LEFT JOIN " + Constants.CHANEL_TABLE_NAME + " ON " + Constants.CHANEL_TABLE_NAME + "." + Constants.ColumnChannel.ITEM_ID + " = " + Constants.ITEM_TABLE_NAME + "." + Constants.ColumnItem.ID.toString() +
                        " WHERE " + Constants.CHANEL_TABLE_NAME + ".id = " + selectionArgs[0];
                String idsCondition = "SELECT " + Constants.CONDITION_TABLE_NAME + ".id as row_id FROM " + Constants.CONDITION_TABLE_NAME +
                        " LEFT JOIN " + Constants.ITEM_TABLE_NAME + " ON " + Constants.ITEM_TABLE_NAME + "." + Constants.ColumnItem.CONDITION_ID + " = " + Constants.CONDITION_TABLE_NAME + "." + Constants.ColumnConditions.ID.toString() +
                        " LEFT JOIN " + Constants.CHANEL_TABLE_NAME + " ON " + Constants.CHANEL_TABLE_NAME + "." + Constants.ColumnChannel.ITEM_ID + " = " + Constants.ITEM_TABLE_NAME + "." + Constants.ColumnItem.ID.toString() +
                        " WHERE " + Constants.CHANEL_TABLE_NAME + ".id = ?";
                String idsItem = "SELECT " + Constants.ITEM_TABLE_NAME + ".id as row_id FROM " + Constants.ITEM_TABLE_NAME +
                        " LEFT JOIN " + Constants.CHANEL_TABLE_NAME + " ON " + Constants.CHANEL_TABLE_NAME + "." + Constants.ColumnChannel.ITEM_ID + " = " + Constants.ITEM_TABLE_NAME + ".id" +
                        " WHERE " + Constants.CHANEL_TABLE_NAME + ".id = " + selectionArgs[0];
                SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();

                Cursor c = sqLiteDatabase.rawQuery(idsAtmosphere, null);
                if (c.moveToFirst()) {
                    c.getFloat(c.getColumnIndex("row_id"));
                }
                sqLiteDatabase.delete(Constants.WIND_TABLE_NAME, "id IN (SELECT t1.row_id FROM (" + idsWind + ") as t1)", null);
                sqLiteDatabase.delete(Constants.ATMOSPHERE_TABLE_NAME, "id IN (SELECT t1.row_id FROM (" + idsAtmosphere + ") as t1)", null);
                sqLiteDatabase.delete(Constants.ASTRONOMY_TABLE_NAME, "id IN (SELECT t1.row_id FROM (" + idsAstronomy + ") as t1)", null);
                sqLiteDatabase.delete(Constants.LOCATION_TABLE_NAME, "id IN (SELECT t1.row_id FROM (" + idsLocation + ") as t1)", null);
                sqLiteDatabase.delete(Constants.UNITS_TABLE_NAME, "id IN (SELECT t1.row_id FROM (" + idsUnits + ") as t1)", null);
                sqLiteDatabase.delete(Constants.FORECAST_TABLE_NAME, "id IN (SELECT t1.row_id FROM (" + idsForecast + ") as t1)", null);
                sqLiteDatabase.delete(Constants.CONDITION_TABLE_NAME, "id IN ((SELECT t1.row_id FROM (" + idsCondition + ") as t1))", selectionArgs);
                sqLiteDatabase.delete(Constants.ITEM_TABLE_NAME, "id IN (SELECT t1.row_id FROM (" + idsItem + ") as t1)", null);
                sqLiteDatabase.delete(Constants.CHANEL_TABLE_NAME, "id = ?", selectionArgs);
                sqLiteDatabase.close();
                return 1;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    public class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            db.execSQL("PRAGMA foreign_keys=ON");
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
            db.execSQL(Constants.INSERT_START_APP);
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
