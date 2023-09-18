package com.example.cpen321_m1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SurpriseActivity extends AppCompatActivity {
    final static String TAG = "SurpriseActivity";
    private Button getWeather;
    String weather_url;
    String apiID = "ec389eb02b704734a663b5fbf9878e54";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise);

        getWeather = findViewById(R.id.btVar1);
        getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Get weather button click");
                getTemp();
            }
        });

    }
    private void getTemp() {
        RequestQueue volleyQueue = Volley.newRequestQueue(this);
        weather_url = "https://api.weatherbit.io/v2.0/current?" + "lat=" + PhoneDetailsActivity.locationLat + "&lon=" + PhoneDetailsActivity.locationLong + "&key=" + apiID;
        StringRequest requestTemp = new StringRequest(Request.Method.GET, weather_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray arr = obj.getJSONArray("data");
                            JSONObject obj2 = arr.getJSONObject(0);
                            String temp = obj2.getString("app_temp");
                            TextView temperature = findViewById(R.id.textView);
                            temperature.setText(String.format(temp + " deg Celsius in " + PhoneDetailsActivity.cityLocation));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, String.valueOf(error));
            }
        });
        volleyQueue.add(requestTemp);
    }
}