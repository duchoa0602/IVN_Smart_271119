package com.ivn.ivnsmart.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ivn.ivnsmart.R;
import com.ivn.ivnsmart.model.MqttClient;
import com.ivn.ivnsmart.model.TCPClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Button btnEditCodeNumber, btnConnectLocal, btnConnectOnline;
//    private Button btnReloadDevice;
    private ImageView imgStatusCodeNumber, imgStatusConnectLocal, imgStatusConnectOnline;
//    private TextView tvDeviceNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mapping();

        if (WelcomeActivity.codeNumber!="null"){
            imgStatusCodeNumber.setImageResource(R.drawable.ic_online);
        }else imgStatusCodeNumber.setImageResource(R.drawable.ic_offline);

        if (WelcomeActivity.checkCLocal==false){
            imgStatusConnectLocal.setImageResource(R.drawable.ic_offline);
            imgStatusConnectOnline.setImageResource(R.drawable.ic_online);
        }else {
            imgStatusConnectLocal.setImageResource(R.drawable.ic_online);
            imgStatusConnectOnline.setImageResource(R.drawable.ic_offline);
        }

        btnEditCodeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SettingsActivity.this, CheckQRCodeActivity.class);
                startActivity(intent);
            }
        });

        btnConnectLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.ipLocal = readFile(MainActivity.SHARED_PREFERENCES_IPTCPClient, "ip");
                Toast.makeText(SettingsActivity.this, "+"+MainActivity.ipLocal+"+", Toast.LENGTH_LONG).show();
                if (MainActivity.ipLocal != "null"){
                    MainActivity.tcpClient = new TCPClient();
                    WelcomeActivity.checkCLocal = true;
                    Toast.makeText(SettingsActivity.this, "Connect Local success.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    WelcomeActivity.checkCLocal = false;
                    getLocalInf();
                    Toast.makeText(SettingsActivity.this, "Connect Local false.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnConnectOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WelcomeActivity.checkCLocal = false;
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void mapping(){
        btnConnectLocal = findViewById(R.id.btnConnectLocal);
        btnConnectOnline = findViewById(R.id.btnConnectOnline);
        btnEditCodeNumber = findViewById(R.id.btnEditCodeNumber);
//        btnReloadDevice = findViewById(R.id.btnReloadDevice);
        imgStatusCodeNumber = findViewById(R.id.imgStatusCodeNumber);
        imgStatusConnectLocal = findViewById(R.id.imgStatusConnectLocal);
        imgStatusConnectOnline = findViewById(R.id.imgStatusConnectOnline);
//        tvDeviceNumber = findViewById(R.id.tvDeviceNumber);
    }

    private void getLocalInf(){
        if (WelcomeActivity.codeNumber!="null"&& MqttClient.client.isConnected() != false){
            WelcomeActivity.mqttClient.puslish((String) getText(R.string.mess_request), "req/" + WelcomeActivity.codeNumber);
            String req = MqttClient.request;
            if (req!=null){
                String ip = null;
                try {
                    JSONObject jsonObject = new JSONObject(req);
                    ip = jsonObject.getString("ip");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                saveIP("ip", ip);
            }else {
                Toast.makeText(SettingsActivity.this,"Request Null.", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(SettingsActivity.this,"Please enter codeNumber and reconnect", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveIP(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES_IPTCPClient, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString(key, value);
        editor.apply();
    }

    private String readFile(String fileName ,String key){
        SharedPreferences sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "null");
    }
}
