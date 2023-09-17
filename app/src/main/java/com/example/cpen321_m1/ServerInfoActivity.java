package com.example.cpen321_m1;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.OffsetTime;
import java.util.Calendar;
import java.util.Date;

public class ServerInfoActivity extends AppCompatActivity {
    final static String TAG = "ServerInfoActivity";
    String serverIPString;
    String clientIPString;
    String serverTimeString;
    String clientTimeString;
    String myNameString;
    String loggedInString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_info);
        requestBackendAPIs();
        updateClientValues();
        updateText();
    }

    private void updateClientValues() {
//        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//        clientIPString = Formatter.formatIpAddress(wifiMgr.getConnectionInfo().getIpAddress());
        RequestQueue volleyQueue = Volley.newRequestQueue(this);
        String ipifyUrl = "https://api.ipify.org?format=json";
        StringRequest requestIpify = new StringRequest(Request.Method.GET, ipifyUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            clientIPString = jObject.getString("ip");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        updateText();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, String.valueOf(error));
            }
        });
        volleyQueue.add(requestIpify);

        OffsetTime offset = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            offset = OffsetTime.now();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            clientTimeString = offset.getHour() + ":" + offset.getMinute() + ":" + offset.getSecond();
        }

        Bundle b = getIntent().getExtras();
        assert b != null;
        loggedInString = b.getString("username");
    }

    private void updateText() {
        TextView serverIPText = (TextView) findViewById(R.id.server_ip_text);
        serverIPText.setText(String.format("Server IP address: %s", serverIPString));

        TextView clientIPText = (TextView) findViewById(R.id.client_ip_text);
        clientIPText.setText(String.format("Client IP address: %s", clientIPString));

        TextView serverTimeText = (TextView) findViewById(R.id.server_time_text);
        serverTimeText.setText(String.format("Server local time: %s", serverTimeString));

        TextView clientTimeText = (TextView) findViewById(R.id.client_time_text);
        clientTimeText.setText(String.format("Client local time: %s", clientTimeString));

        TextView myNameText = (TextView) findViewById(R.id.my_name_text);
        myNameText.setText(String.format("My name: %s", myNameString));

        TextView loggedInText = (TextView) findViewById(R.id.logged_in_text);
        loggedInText.setText(String.format("Logged in: %s", loggedInString));
    }

    private void requestBackendAPIs() {
        RequestQueue volleyQueue = Volley.newRequestQueue(this);
        String urlIP = "http://20.239.76.13:8081/ip";
        String urlTime = "http://20.239.76.13:8081/time";
        String urlName = "http://20.239.76.13:8081/name";

        StringRequest requestIP = new StringRequest(Request.Method.GET, urlIP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        serverIPString = response;
                        updateText();
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, String.valueOf(error));
                }
        });

        StringRequest requestTime = new StringRequest(Request.Method.GET, urlTime,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        serverTimeString = response;
                        updateText();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, String.valueOf(error));
            }
        });

        StringRequest requestMyName = new StringRequest(Request.Method.GET, urlName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        myNameString = response;
                        updateText();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, String.valueOf(error));
            }
        });
        volleyQueue.add(requestIP);
        volleyQueue.add(requestTime);
        volleyQueue.add(requestMyName);
    }


}