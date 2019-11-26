package com.ivn.ivnsmart.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.ivn.ivnsmart.R;
import com.ivn.ivnsmart.model.MqttClient;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    public static MqttClient mqttClient;
    public static String codeNumber;
    public static boolean checkCLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        codeNumber = readFile(CheckQRCodeActivity.SHARED_PREFERENCES_MACID,"macID");
        if (codeNumber!="null"){
            mqttClient = new MqttClient();
            mqttClient.connect(WelcomeActivity.this, MqttClient.urlD);
            checkCLocal = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },SPLASH_TIME_OUT);
            Toast.makeText(WelcomeActivity.this, "Connect to device: " + codeNumber, Toast.LENGTH_SHORT).show();
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, CheckQRCodeActivity.class);
                    startActivity(intent);
                    finish();
                }
            },SPLASH_TIME_OUT);
        }
    }

    private String readFile(String fileName ,String key){
        SharedPreferences sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "null");
    }
}
