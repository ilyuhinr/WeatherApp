package ru.example.weatherapp.services;

import android.os.Bundle;

public interface ServiceCallbackListener {
    void onServiceCallback(int requestId, int resultCode, Bundle data);
}
