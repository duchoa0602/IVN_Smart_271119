package com.ivn.ivnsmart.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ivn.ivnsmart.R;
import com.ivn.ivnsmart.model.MqttClient;
import com.ivn.ivnsmart.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CheckQRCodeActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES_MACID = "macID";
    private EditText edtCodeNumber;
    private Button btnConnect, btnQRCode;
    private String code;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_qrcode);

        setWidget();
        final IntentIntegrator intentIntegrator = new IntentIntegrator(this);

        Toast.makeText(CheckQRCodeActivity.this, "Please enter code number", Toast.LENGTH_SHORT).show();

        edtCodeNumber.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = edtCodeNumber.getText().toString();
                if (TextUtils.isEmpty(code)){
                    Toast.makeText(CheckQRCodeActivity.this, "Code Number, Device Number Not None", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(CheckQRCodeActivity.this, "MAC: " + code, Toast.LENGTH_SHORT).show();
                    saveMacID("macID", code);

                    Intent intent = new Intent(CheckQRCodeActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult!=null){
            if (intentResult.getContents()==null){
                Toast.makeText(CheckQRCodeActivity.this, "Data none!!", Toast.LENGTH_SHORT).show();
            }else {
                user = readJsonData(intentResult.getContents());
                saveUser(user);
                edtCodeNumber.setText(user.getUser()+"\n" + user.getPass() + "\n" + user.getMacID());
                Toast.makeText(CheckQRCodeActivity.this, "Result: " + intentResult.getContents(), Toast.LENGTH_SHORT).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setWidget() {
        edtCodeNumber = findViewById(R.id.edtCodeNumber);
        btnConnect = findViewById(R.id.btnConnect);
        btnQRCode = findViewById(R.id.btnQRCode);
    }

    public void saveMacID(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_MACID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString(key, value);
        editor.apply();
    }

    private void saveUser(User us){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_MACID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("user", us.getUser());
        editor.putString("pass", us.getPass());
        editor.putString("macID", us.getMacID());
        editor.apply();
    }

    private User readJsonData(String json){
        User user = null;
        String us, pa, co;
        try {
            JSONObject jsonObject = new JSONObject(json);
            us = jsonObject.getString("user");
            pa = jsonObject.getString("pass");
            co = jsonObject.getString("macID");
            edtCodeNumber.setText(co);
            user = new User(us, pa, co);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

}
