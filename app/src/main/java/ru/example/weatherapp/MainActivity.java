package ru.example.weatherapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.drakeet.materialdialog.MaterialDialog;
import ru.example.weatherapp.database.WeatherProvider;
import ru.example.weatherapp.model.Astronomy;
import ru.example.weatherapp.model.Atmosphere;
import ru.example.weatherapp.model.Channel;
import ru.example.weatherapp.model.Condition;
import ru.example.weatherapp.model.Forecast;
import ru.example.weatherapp.model.Item;
import ru.example.weatherapp.model.Location;
import ru.example.weatherapp.model.Units;
import ru.example.weatherapp.model.Wind;
import ru.example.weatherapp.services.ServiceCallbackListener;
import ru.example.weatherapp.services.ServiceHelper;
import ru.example.weatherapp.services.WeatherService;
import ru.example.weatherapp.utils.Constants;


public class MainActivity extends AppCompatActivity implements ServiceCallbackListener {
    private String LOG_TAG = this.getClass().getName();
    @InjectView(R.id.add_city_button)
    ImageButton mAddCityButton;
    @InjectView(R.id.temp)
    TextView mTemp;
    @InjectView(R.id.sunrise)
    TextView mSunrise;
    @InjectView(R.id.sunset)
    TextView mSunset;
    @InjectView(R.id.weather_info)
    TextView mWeatherInfo;
    @InjectView(R.id.wind_speed)
    TextView mWindSpeed;
    @InjectView(R.id.humidity)
    TextView mHumidity;
    @InjectView(R.id.pressure)
    TextView mPressure;
    MaterialDialog mAddingDialog;
    private ContentObserver mObserver;
    MaterialRefreshLayout mMaterialRefreshLayout;
    ServiceHelper mServiceHelper;
    private boolean isRefreshing = false;
    private boolean isAutoRefresh = false;
    Spinner mCitySpinner;
    LocationsAsyncTask mLocationsAsyncTask;
    ArrayAdapter<String> mLocationAdapter;
    MaterialListView mListView;
    int mCurrentSelectedLocation = 0;
    RefreshingUI mRefreshingUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mListView = (MaterialListView) findViewById(R.id.forecast_list);
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);
        mServiceHelper = new ServiceHelper();
        mServiceHelper.setServiceCallbackListener(this);

        mMaterialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        mMaterialRefreshLayout.setLoadMore(true);
        mMaterialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                isRefreshing = true;
                String[] location = getCurrentLocation();
                Intent intent;
                intent = mServiceHelper.createIntent(MainActivity.this, location[0], location[1], 0, WeatherService.COMMAND_WEATHER);
                isAutoRefresh = false;
                startService(intent);

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                isRefreshing = true;
                String[] location = getCurrentLocation();
                Intent intent;
                intent = mServiceHelper.createIntent(MainActivity.this, location[0], location[1], 0, WeatherService.COMMAND_WEATHER_CITY);
                isAutoRefresh = false;
                startService(intent);
            }
        });
        mMaterialRefreshLayout.setWaveColor(0x903F51B5);
        mMaterialRefreshLayout.setIsOverLay(false);
        mMaterialRefreshLayout.setWaveShow(true);

        mObserver = new ContentObserver(new Handler()) {
            public void onChange(boolean selfChange) {
                isRefreshing = false;
                isAutoRefresh = false;
                mMaterialRefreshLayout.finishRefresh();
                mMaterialRefreshLayout.finishRefreshLoadMore();
                Toast.makeText(MainActivity.this, "Данные обновлены!", Toast.LENGTH_LONG).show();
                mRefreshingUI = new RefreshingUI();
                mRefreshingUI.execute(getCurrentLocation());
            }
        };

        mAddCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogAddingCity();
            }
        });
        mLocationAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_dropdown_item_1line, new String[]{});
        mCitySpinner = (Spinner) findViewById(R.id.city_spinner);
        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentSelectedLocation != position) {
                    mCurrentSelectedLocation = position;
                    isAutoRefresh = true;
                    mMaterialRefreshLayout.autoRefreshLoadMore();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCitySpinner.setAdapter(mLocationAdapter);
        mLocationsAsyncTask = new LocationsAsyncTask();
        mLocationsAsyncTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContentResolver().registerContentObserver(WeatherProvider.WEATHER_REFRESH_URI, true, mObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().
                unregisterContentObserver(mObserver);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().
                unregisterContentObserver(mObserver);
    }

    private String[] getCurrentLocation() {
        String[] location = mCitySpinner.getSelectedItem().toString().split(", ");
        return location;
    }

    public void getDialogAddingCity() {
        final MaterialDialog materialDialog = new MaterialDialog(this).setTitle("Новый город");
        materialDialog.setMessage("Вы хотите добавить новый гоод?").setPositiveButton("Да", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                View contentDialog = getLayoutInflater().inflate(R.layout.dialog_add_city, null);
                final EditText country = (EditText) contentDialog.findViewById(R.id.country_dialog);
                final EditText city = (EditText) contentDialog.findViewById(R.id.city_dialog);
                mAddingDialog = new MaterialDialog(MainActivity.this);
                mAddingDialog.setTitle("Новый город").setView(contentDialog).setPositiveButton("Добавить", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(country.getText()) || TextUtils.isEmpty(city.getText())) {
                            Toast.makeText(MainActivity.this, "Не заполнены поля!", Toast.LENGTH_LONG).show();
                        } else {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("country", country.getText().toString());
                            contentValues.put("cityName", city.getText().toString());
                            mLocationsAsyncTask = new LocationsAsyncTask();
                            mLocationsAsyncTask.execute(contentValues);
                            mAddingDialog.dismiss();
                        }
                    }
                }).setNegativeButton("Отмена", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAddingDialog.dismiss();
                    }
                }).show();

            }
        }).setNegativeButton("Нет", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        }).show();
    }


    @Override
    public void onServiceCallback(int requestId, int resultCode, Bundle data) {
        if (isRefreshing) {
            if (resultCode == -1) {
                Log.d(LOG_TAG, "onServiceCallback result error!");
                Toast.makeText(this, "Произошла ошибка при получении данных. Повторите попытку позже!", Toast.LENGTH_LONG).show();
            } else if (resultCode == 100) {
                Log.d(LOG_TAG, "onServiceCallback result success!");
                Toast.makeText(this, "Нет данных для обновления!", Toast.LENGTH_LONG).show();
                mMaterialRefreshLayout.finishRefresh();
                mMaterialRefreshLayout.finishRefreshLoadMore();
            } else if (resultCode == 200) {
                Log.d(LOG_TAG, "onServiceCallback result success!");
            }
            mRefreshingUI = new RefreshingUI();
            mRefreshingUI.execute(getCurrentLocation());
        }
    }


    public void updateUI(Channel channel) {
        mTemp.setText(channel.getItem().getCondition().getTemp() + " C");
        mSunrise.setText("Восход солнца: " + channel.getAstronomy().getSunrise());
        mSunset.setText("Закат солнца: " + channel.getAstronomy().getSunset());
        mWeatherInfo.setText(channel.getItem().getCondition().getText());
        mWindSpeed.setText("Скорость ветра: " + channel.getWind().getSpeed() + " " + channel.getUnits().getSpeed());
        mHumidity.setText("Влажность воздуха: " + channel.getAtmosphere().getHumidity() + "%");
        mPressure.setText("Атмосферное давление: " + channel.getAtmosphere().getPressure() + " " + channel.getUnits().getPressure());
        mListView.getAdapter().clearAll();
        mListView.getAdapter().notifyDataSetChanged();
        List<Card> cards = new ArrayList<>();
        for (final Forecast forecast : channel.getItem().getForecasts()) {
            final CardProvider provider = new Card.Builder(this).setTag(forecast)
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.forecast_row_item)
                    .setTitle(forecast.getHigh() + " C")
                    .addAction(R.id.more_button, new TextViewAction(this)
                            .setText("Подробнее")
                            .setTextResourceColor(R.color.material_blue)
                            .setListener(new OnActionClickListener() {
                                @Override
                                public void onActionClicked(View view, final Card card) {
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Forecast forecastInfo = (Forecast) card.getTag();
                                            View forecastView = getLayoutInflater().inflate(R.layout.dialog_forecast, null);
                                            TextView date = (TextView) forecastView.findViewById(R.id.forecast_dialog_day);
                                            TextView low = (TextView) forecastView.findViewById(R.id.forecast_dialog_low);
                                            TextView temp = (TextView) forecastView.findViewById(R.id.forecast_dialog_temp);
                                            TextView high = (TextView) forecastView.findViewById(R.id.forecast_dialog_high);
                                            TextView text = (TextView) forecastView.findViewById(R.id.forecast_dialog_text);
                                            text.setText(forecast.getText());
                                            date.setText(forecastInfo.getDate());
                                            low.setText("Low: " + forecast.getLow() + " C");
                                            high.setText("High: " + forecast.getHigh() + " C");
                                            temp.setText(forecast.getHigh() + " C");
                                            mAddingDialog = new MaterialDialog(MainActivity.this);
                                            mAddingDialog.setTitle("Прогноз").setView(forecastView).setPositiveButton("ОК", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mAddingDialog.dismiss();
                                                }
                                            }).show();
                                        }
                                    });
                                }
                            })).addAction(R.id.date, new TextViewAction(this)
                            .setText(forecast.getDate()).setTextResourceColor(R.color.material_blue)
                            .setListener(new OnActionClickListener() {
                                @Override
                                public void onActionClicked(View view, Card card) {

                                }
                            }));

            provider.setDividerVisible(true);
            Card card = provider.endConfig().build();
            cards.add(card);
        }

        mListView.getAdapter().addAll(cards);
        mListView.getAdapter().notifyDataSetChanged();
    }

    private class LocationsAsyncTask extends AsyncTask<ContentValues, Void, String[]> {

        @Override
        protected String[] doInBackground(ContentValues... params) {
            if (params.length > 0) {
                getContentResolver().insert(Constants.CITY_CONTENT_URI, params[0]);
            }
            Log.d(LOG_TAG, "Adding new city!");
            Cursor cityCursor = getContentResolver().query(Constants.CITY_CONTENT_URI, null, null, null, null);
            String[] citys = new String[cityCursor.getCount()];
            if (cityCursor.moveToFirst()) {
                do {
                    citys[cityCursor.getPosition()] = cityCursor.getString(cityCursor.getColumnIndex("country")) + ", " + cityCursor.getString(cityCursor.getColumnIndex("cityName"));
                } while (cityCursor.moveToNext());
            }
            cityCursor.close();
            return citys;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result.length > 0) {
                if (mLocationAdapter.getCount() == 0) {
                    mLocationAdapter = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_dropdown_item_1line, result);
                    mCitySpinner.setAdapter(mLocationAdapter);
                } else if (mLocationAdapter.getCount() < result.length) {
                    mLocationAdapter = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_dropdown_item_1line, result);
                    mCitySpinner.setAdapter(mLocationAdapter);
                    mCitySpinner.setSelection(result.length - 1);
                }
            }
            mMaterialRefreshLayout.autoRefresh();
            isAutoRefresh = true;
        }
    }

    private class RefreshingUI extends AsyncTask<String[], Void, Channel> {

        @Override
        protected Channel doInBackground(String[]... params) {
            long idChannel = 0;
            Cursor channelCursor = getContentResolver().query(Constants.CHANNEL_CONTENT_URI, null, "loc.country=? and loc.city=?", params[0], null);
            if (channelCursor.moveToFirst()) {
                idChannel = channelCursor.getLong(channelCursor.getColumnIndex(Constants.ColumnChannel.ID.toString()));
            }
            Cursor cursor = getContentResolver().query(Constants.QUERY_CHANNEL_MODEL, null, null, new String[]{String.valueOf(idChannel)}, null);
            Channel channel = new Channel();
            if (cursor.moveToFirst()) {
                channel.setLastBuildDate(cursor.getString(cursor.getColumnIndex(Constants.ColumnChannel.LAST_BUILD_DATE.toString())));
                List<Forecast> forecasts = new ArrayList<>();
                Astronomy astronomy = new Astronomy();
                astronomy.setSunrise(cursor.getString(cursor.getColumnIndex(Constants.ColumnAstronomy.SUNRISE.toString())));
                astronomy.setSunset(cursor.getString(cursor.getColumnIndex(Constants.ColumnAstronomy.SUNSET.toString())));
                channel.setAstronomy(astronomy);
                Atmosphere atmosphere = new Atmosphere();
                atmosphere.setHumidity(cursor.getInt(cursor.getColumnIndex(Constants.ColumnAtmosphere.HUMIDITY.toString())));
                atmosphere.setPressure(cursor.getFloat(cursor.getColumnIndex(Constants.ColumnAtmosphere.PRESSURE.toString())));
                atmosphere.setRising(cursor.getInt(cursor.getColumnIndex(Constants.ColumnAtmosphere.RISING.toString())));
                atmosphere.setVisibility(cursor.getFloat(cursor.getColumnIndex(Constants.ColumnAtmosphere.VISIBILITY.toString())));
                channel.setAtmosphere(atmosphere);
                Location location = new Location();
                location.setCity(cursor.getString(cursor.getColumnIndex(Constants.ColumnLocation.CITY.toString())));
                location.setCountry(cursor.getString(cursor.getColumnIndex(Constants.ColumnLocation.COUNTRY.toString())));
                location.setRegion(cursor.getString(cursor.getColumnIndex(Constants.ColumnLocation.REGION.toString())));
                channel.setLocation(location);
                Units units = new Units();
                units.setDistance(cursor.getString(cursor.getColumnIndex(Constants.ColumnUnits.DISTANCE.toString())));
                units.setPressure(cursor.getString(cursor.getColumnIndex(Constants.ColumnUnits.PRESSURE.toString())));
                units.setSpeed(cursor.getString(cursor.getColumnIndex(Constants.ColumnUnits.SPEED.toString())));
                units.setTemperature(cursor.getString(cursor.getColumnIndex(Constants.ColumnUnits.TEMP.toString())));
                channel.setUnits(units);
                Wind wind = new Wind();
                wind.setChill(cursor.getInt(cursor.getColumnIndex(Constants.ColumnWind.CHILL.toString())));
                wind.setDirection(cursor.getInt(cursor.getColumnIndex(Constants.ColumnWind.DIRECTION.toString())));
                wind.setSpeed(cursor.getFloat(cursor.getColumnIndex(Constants.ColumnWind.SPEED.toString())));
                channel.setWind(wind);
                Condition condition = new Condition();
                condition.setCode(cursor.getInt(cursor.getColumnIndex(Constants.ColumnConditions.CODE.toString())));
                condition.setDate(cursor.getString(cursor.getColumnIndex(Constants.ColumnConditions.DATE.toString())));
                condition.setTemp(cursor.getInt(cursor.getColumnIndex(Constants.ColumnConditions.TEMP.toString())));
                condition.setText(cursor.getString(cursor.getColumnIndex(Constants.ColumnConditions.TEXT.toString())));
                Item item = new Item();
                item.setId(cursor.getLong(cursor.getColumnIndex(Constants.ColumnItem.ID.toString())));
                item.setPubDate(cursor.getString(cursor.getColumnIndex(Constants.ColumnItem.PUBDATE.toString())));
                item.setCondition(condition);
                channel.setItem(item);

                do {
                    Forecast forecast = new Forecast();
                    forecast.setId(cursor.getInt(cursor.getColumnIndex(Constants.ColumnForecast.ID.toString())));
                    forecast.setCode(cursor.getInt(cursor.getColumnIndex(Constants.ColumnForecast.CODE.toString())));
                    forecast.setDate(cursor.getString(cursor.getColumnIndex(Constants.ColumnForecast.DATE.toString())));
                    forecast.setText(cursor.getString(cursor.getColumnIndex(Constants.ColumnForecast.TEXT.toString())));
                    forecast.setDay(cursor.getString(cursor.getColumnIndex(Constants.ColumnForecast.DAY.toString())));
                    forecast.setHigh(cursor.getInt(cursor.getColumnIndex(Constants.ColumnForecast.HIGH.toString())));
                    forecast.setLow(cursor.getInt(cursor.getColumnIndex(Constants.ColumnForecast.LOW.toString())));
                    forecasts.add(forecast);
                } while (cursor.moveToNext());
                cursor.close();
                channel.getItem().setForecasts(forecasts);
            }
            return channel;
        }

        @Override
        protected void onPostExecute(Channel channel) {
            if (channel != null) {
                updateUI(channel);
            }
            if (isRefreshing) {
                mMaterialRefreshLayout.finishRefresh();
                mMaterialRefreshLayout.finishRefreshLoadMore();
            }
            super.onPostExecute(channel);
        }
    }
}
