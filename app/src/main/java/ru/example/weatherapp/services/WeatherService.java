package ru.example.weatherapp.services;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.example.weatherapp.model.Channel;
import ru.example.weatherapp.model.Forecast;
import ru.example.weatherapp.utils.Constants;

public class WeatherService extends IntentService {
    private String LOG_TAG = getClass().getName();
    private ResultReceiver mResultReceiver;
    public static String EXTRA_STATUS_RECEIVER = "receiver";
    public static String COMMAND_WEATHER = "getWeather";
    public static String COMMAND_WEATHER_CITY = "getWeatherCity";
    public static String REQUEST_ERROR_RESPONSE = "error_response";
    public static String REQUEST_SUCCESS_RESPONSE = "success_response";

    public WeatherService() {
        super("WeatherService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        mResultReceiver = intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getURL(intent), (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {


                    JSONObject jsonQuery = jsonObject.getJSONObject("query");
                    JSONObject jsonResults = jsonQuery.getJSONObject("results");
                    JSONObject jsonChannel = jsonResults.getJSONObject("channel");
                    Gson gson = new Gson();
                    Channel channel = gson.fromJson(jsonChannel.toString(), Channel.class);
                    if (channel != null) {
                        Cursor cursorChannel = getContentResolver().query(Constants.CHANNEL_CONTENT_URI, null, "loc.country=? and loc.city=?", new String[]{String.valueOf(channel.getLocation().getCountry()), String.valueOf(channel.getLocation().getCity())}, null);
                        if (cursorChannel.getCount() > 0) {
                            if (cursorChannel.moveToFirst()) {
                                Date date = getDataFromString(channel.getLastBuildDate());
                                Date dateResponse = getDataFromString(cursorChannel.getString(cursorChannel.getColumnIndex(Constants.ColumnChannel.LAST_BUILD_DATE.toString())));
                                long oldChannelId = cursorChannel.getLong(cursorChannel.getColumnIndex(Constants.ColumnChannel.ID.toString()));
                                cursorChannel.close();
                                if (date.equals(dateResponse)) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("message", REQUEST_SUCCESS_RESPONSE);
                                    if (intent.getAction().equals(COMMAND_WEATHER_CITY)) {
                                        getReceiver(intent).send(200, bundle);
                                    } else {
                                        getReceiver(intent).send(100, bundle);
                                    }
                                } else {
                                    updateResultChannel(channel, oldChannelId);
                                }
                            }
                        } else {
                            insertResultChannel(channel);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", REQUEST_ERROR_RESPONSE);
                    getReceiver(intent).send(-1, bundle);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Bundle bundle = new Bundle();
                bundle.putString("message", REQUEST_ERROR_RESPONSE);
                getReceiver(intent).send(-1, bundle);
            }
        });
        queue.add(request);
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                Log.i(LOG_TAG, "request " + request.getUrl() + " finished!");
            }
        });
    }

    public void updateResultChannel(Channel channel, long oldChannelId) {
        if (getContentResolver().delete(Constants.DELETE_ALL_URI, null, new String[]{String.valueOf(oldChannelId)}) == 1) {
            insertResultChannel(channel);
        }
    }

    public void insertResultChannel(Channel channel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.ColumnAstronomy.SUNRISE.toString(), channel.getAstronomy().getSunrise());
        contentValues.put(Constants.ColumnAstronomy.SUNSET.toString(), channel.getAstronomy().getSunset());
        Uri astronomyUri = getContentResolver().insert(Constants.ASTRONOMY_CONTENT_URI, contentValues);
        long idAstronomy = ContentUris.parseId(astronomyUri);
        contentValues.clear();
        contentValues.put(Constants.ColumnAtmosphere.HUMIDITY.toString(), channel.getAtmosphere().getHumidity());
        contentValues.put(Constants.ColumnAtmosphere.PRESSURE.toString(), channel.getAtmosphere().getPressure());
        contentValues.put(Constants.ColumnAtmosphere.RISING.toString(), channel.getAtmosphere().getRising());
        contentValues.put(Constants.ColumnAtmosphere.VISIBILITY.toString(), channel.getAtmosphere().getVisibility());
        Uri atmosphereUri = getContentResolver().insert(Constants.ATMOSPHERE_CONTENT_URI, contentValues);
        long idAtmosphere = ContentUris.parseId(atmosphereUri);
        contentValues.clear();
        contentValues.put(Constants.ColumnConditions.TEXT.toString(), channel.getItem().getCondition().getText());
        contentValues.put(Constants.ColumnConditions.CODE.toString(), channel.getItem().getCondition().getCode());
        contentValues.put(Constants.ColumnConditions.TEMP.toString(), channel.getItem().getCondition().getTemp());
        contentValues.put(Constants.ColumnConditions.DATE.toString(), channel.getItem().getCondition().getDate());
        Uri conditionsUri = getContentResolver().insert(Constants.CONDITION_CONTENT_URI, contentValues);
        long idconditions = ContentUris.parseId(conditionsUri);
        contentValues.clear();
        contentValues.put(Constants.ColumnItem.DESCR.toString(), channel.getItem().getDescription());
        contentValues.put(Constants.ColumnItem.CONDITION_ID.toString(), idconditions);
        contentValues.put(Constants.ColumnItem.LAT.toString(), channel.getItem().getGeoLat());
        contentValues.put(Constants.ColumnItem.LONG.toString(), channel.getItem().getGeoLong());
        contentValues.put(Constants.ColumnItem.LINK.toString(), channel.getItem().getLink());
        contentValues.put(Constants.ColumnItem.PUBDATE.toString(), channel.getItem().getPubDate());
        contentValues.put(Constants.ColumnItem.TITLE.toString(), channel.getItem().getTitle());
        Uri itemUri = getContentResolver().insert(Constants.ITEM_CONTENT_URI, contentValues);
        long iditem = ContentUris.parseId(itemUri);
        contentValues.clear();
        for (Forecast forecast : channel.getItem().getForecasts()) {
            contentValues.put(Constants.ColumnForecast.CODE.toString(), forecast.getCode());
            contentValues.put(Constants.ColumnForecast.DATE.toString(), forecast.getDate());
            contentValues.put(Constants.ColumnForecast.DAY.toString(), forecast.getDay());
            contentValues.put(Constants.ColumnForecast.HIGH.toString(), forecast.getHigh());
            contentValues.put(Constants.ColumnForecast.LOW.toString(), forecast.getLow());
            contentValues.put(Constants.ColumnForecast.TEXT.toString(), forecast.getText());
            contentValues.put(Constants.ColumnForecast.ITEM_ID.toString(), iditem);
            Uri forecastUri = getContentResolver().insert(Constants.FORECAST_CONTENT_URI, contentValues);
            ContentUris.parseId(forecastUri);
            contentValues.clear();
        }
        contentValues.put(Constants.ColumnLocation.CITY.toString(), channel.getLocation().getCity());
        contentValues.put(Constants.ColumnLocation.COUNTRY.toString(), channel.getLocation().getCountry());
        contentValues.put(Constants.ColumnLocation.REGION.toString(), channel.getLocation().getRegion());
        Uri locationUri = getContentResolver().insert(Constants.LOCATION_CONTENT_URI, contentValues);
        long idLocation = ContentUris.parseId(locationUri);
        contentValues.clear();
        contentValues.put(Constants.ColumnUnits.DISTANCE.toString(), channel.getUnits().getDistance());
        contentValues.put(Constants.ColumnUnits.PRESSURE.toString(), channel.getUnits().getPressure());
        contentValues.put(Constants.ColumnUnits.SPEED.toString(), channel.getUnits().getSpeed());
        contentValues.put(Constants.ColumnUnits.TEMP.toString(), channel.getUnits().getTemperature());
        Uri unitsUri = getContentResolver().insert(Constants.UNITS_CONTENT_URI, contentValues);
        long idUnits = ContentUris.parseId(unitsUri);
        contentValues.clear();
        contentValues.put(Constants.ColumnWind.CHILL.toString(), channel.getWind().getChill());
        contentValues.put(Constants.ColumnWind.DIRECTION.toString(), channel.getWind().getDirection());
        contentValues.put(Constants.ColumnWind.SPEED.toString(), String.valueOf(channel.getWind().getSpeed()));
        Uri windUri = getContentResolver().insert(Constants.WIND_CONTENT_URI, contentValues);
        long idWind = ContentUris.parseId(windUri);
        contentValues.clear();
        contentValues.put(Constants.ColumnChannel.ASTRONOMY_ID.toString(), idAstronomy);
        contentValues.put(Constants.ColumnChannel.ATMOSPHERE_ID.toString(), idAtmosphere);
        contentValues.put(Constants.ColumnChannel.DESCRIPTION.toString(), channel.getDescription());
        contentValues.put(Constants.ColumnChannel.LINK.toString(), channel.getLink());
        contentValues.put(Constants.ColumnChannel.ITEM_ID.toString(), iditem);
        contentValues.put(Constants.ColumnChannel.LANGUAGE.toString(), channel.getLanguage());
        contentValues.put(Constants.ColumnChannel.LAST_BUILD_DATE.toString(), channel.getLastBuildDate());
        contentValues.put(Constants.ColumnChannel.LOCATION_ID.toString(), idLocation);
        contentValues.put(Constants.ColumnChannel.TITLE.toString(), channel.getTitle());
        contentValues.put(Constants.ColumnChannel.TTL.toString(), channel.getTtl());
        contentValues.put(Constants.ColumnChannel.UNITS_ID.toString(), idUnits);
        contentValues.put(Constants.ColumnChannel.WIND_ID.toString(), idWind);
        Uri channelUri = getContentResolver().insert(Constants.CHANNEL_CONTENT_URI, contentValues);
        long idChannel = ContentUris.parseId(channelUri);
        contentValues.clear();
    }

    public String getURL(Intent intent) {
        if (intent.getAction().equals(COMMAND_WEATHER) || intent.getAction().equals(COMMAND_WEATHER_CITY)) {
            return ServiceHelper.getURLParams(intent.getStringExtra(ServiceHelper.COUNTRY_EXTRA), intent.getStringExtra(ServiceHelper.CITY_EXTRA), null);
        } else {
            return "";
        }
    }

    private ResultReceiver getReceiver(Intent intent) {
        return intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);
    }

    public static Date getDataFromString(final String date) {
        try {
            final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm a", Locale.ENGLISH);
            return format.parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
