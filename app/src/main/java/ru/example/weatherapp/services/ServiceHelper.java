package ru.example.weatherapp.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by developer on 14.01.2016.
 */
public class ServiceHelper {
    public static String COUNTRY_EXTRA = "country";
    public static String CITY_EXTRA = "city";
    ServiceCallbackListener mServiceCallbackListener;

    public Intent createIntent(final Context context, String country, String city, final int requestId) {
        Intent i = new Intent(context, WeatherService.class);
        i.setAction(WeatherService.COMMAND_WEATHER);

        i.putExtra(COUNTRY_EXTRA, country);
        //  i.putExtra(SFCommandExecutorService.EXTRA_REQUEST_ID, requestId);
        i.putExtra(CITY_EXTRA, city);
        i.putExtra(WeatherService.EXTRA_STATUS_RECEIVER, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (mServiceCallbackListener != null)
                    mServiceCallbackListener.onServiceCallback(requestId, resultCode, resultData);
            }

        });

        return i;
    }

    public void setServiceCallbackListener(ServiceCallbackListener serviceCallbackListener) {
        mServiceCallbackListener = serviceCallbackListener;
    }
}
