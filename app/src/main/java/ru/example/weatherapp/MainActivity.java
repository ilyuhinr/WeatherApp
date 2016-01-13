package ru.example.weatherapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import ru.example.weatherapp.model.Channel;
import ru.example.weatherapp.utils.Constants;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.text_);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        /*values.put("sunrise", "test");
        values.put("sunset", "test");

        sqLiteDatabase.insert("astronomy", null, values);*/
       /* Cursor c = sqLiteDatabase.rawQuery("SELECT sunrise, sunset FROM astronomy", null);
        if (c.moveToFirst()) {
            c.getString(0);
            c.getString(1);
        }
        sqLiteDatabase.close();*/
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constants.URL, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject arg0) {
                try {
                    JSONObject json_query = arg0.getJSONObject("query");
                    JSONObject json_results = json_query.getJSONObject("results");
                    JSONObject json_json_result = json_results.getJSONObject("channel");
                    Gson gson = new Gson();
                    Channel channel = gson.fromJson(json_json_result.toString(), Channel.class);
                    channel.getAstronomy();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub

            }
        });
        queue.add(request);
    }
}
