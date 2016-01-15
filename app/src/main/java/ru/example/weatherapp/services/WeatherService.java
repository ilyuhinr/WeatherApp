package ru.example.weatherapp.services;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
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

import ru.example.weatherapp.database.DBHelper;
import ru.example.weatherapp.model.Channel;
import ru.example.weatherapp.utils.Constants;

public class WeatherService extends IntentService {
    private String LOG_TAG = getClass().getName();
    private ResultReceiver mResultReceiver;
    public static String EXTRA_STATUS_RECEIVER = "receiver";
    public static String COMMAND_WEATHER = "getWeather";
    public static String REQUEST_ERROR_RESPONSE = "error_response";

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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constants.URL, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject jsonQuery = jsonObject.getJSONObject("query");
                    JSONObject jsonResults = jsonQuery.getJSONObject("results");
                    JSONObject jsonChannel = jsonResults.getJSONObject("channel");
                    Gson gson = new Gson();
                    Channel channel = gson.fromJson(jsonChannel.toString(), Channel.class);
                    if (channel != null) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Constants.ColumnAstronomy.SUNRISE.toString(), channel.getAstronomy().getSunrise());
                        contentValues.put(Constants.ColumnAstronomy.SUNSET.toString(), channel.getAstronomy().getSunset());
                        Uri astronomyUri = getContentResolver().insert(Constants.ASTRONOMY_CONTENT_URI, contentValues);
                        long idAstronomy = ContentUris.parseId(astronomyUri);
                        contentValues.clear();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private ResultReceiver getReceiver(Intent intent) {
        return intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);
    }

}
