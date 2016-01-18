package ru.example.weatherapp.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class ServiceHelper {
    public static String COUNTRY_EXTRA = "country";
    public static String CITY_EXTRA = "city";
    ServiceCallbackListener mServiceCallbackListener;

    public Intent createIntent(final Context context, String country, String city, final int requestId, String action) {
        Intent intent = new Intent(context, WeatherService.class);
        intent.setAction(action);
        intent.putExtra(COUNTRY_EXTRA, country);
        intent.putExtra(CITY_EXTRA, city);
        intent.putExtra(WeatherService.EXTRA_STATUS_RECEIVER, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (mServiceCallbackListener != null)
                    mServiceCallbackListener.onServiceCallback(requestId, resultCode, resultData);
            }

        });

        return intent;
    }

    public void setServiceCallbackListener(ServiceCallbackListener serviceCallbackListener) {
        mServiceCallbackListener = serviceCallbackListener;
    }

    public static String getURLParams(String country, String city, String tempUnits) {
        if (city.toUpperCase().contains("Санкт")) {
            city = "St. Petersburg";
        }
        country = country.replace(" ", "%20");
        city = city.replace(" ", "%20");
        return "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + country + "%2C%20" + city + "%22)%20and%20u%20%3D%20'c'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    }
}
