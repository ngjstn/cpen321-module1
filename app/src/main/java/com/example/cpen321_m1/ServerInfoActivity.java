package com.example.cpen321_m1;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;

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

        updateStringValues();
        updateText();
    }

    private void updateStringValues() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        clientIPString = Formatter.formatIpAddress(wifiMgr.getConnectionInfo().getIpAddress());

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
}