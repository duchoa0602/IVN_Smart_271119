package com.ivn.ivnsmart.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.google.android.material.navigation.NavigationView;
import com.ivn.ivnsmart.R;
import com.ivn.ivnsmart.controller.DeviceController;
import com.ivn.ivnsmart.controller.RoomController;
import com.ivn.ivnsmart.model.Device;
import com.ivn.ivnsmart.model.MqttClient;
import com.ivn.ivnsmart.model.Room;
import com.ivn.ivnsmart.model.TCPClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SHARED_PREFERENCES_IPTCPClient = "ipTCPClient";

    public RoomController roomController;
    private NavigationView navigationView;
    public static TextView tvCheckConnected;
    public static ImageView imgCheckConnected;
    private DrawerLayout drawer;
//    public static MqttClient mqttClient;
//    public static String codeNumber;
    private DeviceController deviceController;
    public static String ssID, ipLocal;
    public static TCPClient tcpClient;
    public static Socket socket;
    public static boolean checkConnectLocal, checkConnectServer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         roomController =  new RoomController(this);
        try {
            roomController.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkConnectLocal = WelcomeActivity.checkCLocal;

        Toast.makeText(MainActivity.this, String.valueOf(MainActivity.checkConnectLocal), Toast.LENGTH_SHORT).show();
        
//        codeNumber = readFile(CheckQRCodeActivity.SHARED_PREFERENCES_MACID,"macID");
//        if (codeNumber!="null"){
//            mqttClient = new MqttClient();
//            mqttClient.connect(this, MqttClient.urlD);
//            Toast.makeText(this, "Mac: " + codeNumber, Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this, "Please enter code number", Toast.LENGTH_SHORT).show();
//        }

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        tvCheckConnected = findViewById(R.id.tv_check_connected);
        imgCheckConnected = findViewById(R.id.img_check_connect);

        if (MqttClient.checkConnectSv){
            MainActivity.tvCheckConnected.setText(R.string.online);
            MainActivity.tvCheckConnected.setTextColor(Color.GREEN);
            MainActivity.imgCheckConnected.setImageResource(R.drawable.ic_online);
            MainActivity.checkConnectLocal = false;
        }else {
            MainActivity.tvCheckConnected.setText(R.string.offline);
            MainActivity.tvCheckConnected.setTextColor(Color.RED);
            MainActivity.imgCheckConnected.setImageResource(R.drawable.ic_offline);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.create_room:
                dialogCreateRoom();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogReloadDevice() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_reload_device);
        Button btnOk = dialog.findViewById(R.id.btnOkRD);
        Button btnCancel = dialog.findViewById(R.id.btnCancelRD);
        final Spinner spinner  = dialog.findViewById(R.id.spiRD);
        final TextView tvDNum = dialog.findViewById(R.id.tvDNumber);

        List <String> deviceNumberList = new ArrayList<>();
        deviceNumberList.add("2");
        deviceNumberList.add("4");
        deviceNumberList.add("8");
        deviceNumberList.add("16");
        deviceNumberList.add("32");

        onCreateSpinner(deviceNumberList, spinner, tvDNum);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deviceController  = new DeviceController(MainActivity.this);
                deviceController.deleteDevice();
                Device device = new Device();
                device.setmPhoto(Integer.parseInt("2131165279"));
                deviceController.insertDevice(device, Integer.parseInt(String.valueOf(tvDNum.getText())));

                Intent intent;
                intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);

                dialog.cancel();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    public void onCreateSpinner(List <String> list, final Spinner spinner, final TextView textView){
        ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                textView.setText(spinner.getSelectedItem().toString());
                Toast.makeText(MainActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @SuppressLint("NewApi")
    private void dialogCreateRoom(){
        final FlatDialog flatDialog = new FlatDialog(MainActivity.this);
        flatDialog.setTitle("Create New Room:")
                .setFirstTextFieldHint ( "Enter Room's Name..." )
                .setBackgroundColor(Color.parseColor("#068cff"))
                .setFirstButtonColor(Color.WHITE)
                .setFirstButtonTextColor(Color.parseColor("#068cff"))
                .setSecondButtonColor(Color.WHITE)
                .setSecondButtonTextColor(Color.parseColor("#068cff"))
                .setFirstButtonText("OK")
                .setSecondButtonText("CANCEL")
                .withFirstButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (flatDialog.getFirstTextField().toString().length() == 0) {
                            Toast.makeText(MainActivity.this, "Nhập sai vui lòng nhập tên phòng", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            Room room = new Room();
                            Random rd=  new Random();

                            room.setrID(rd.nextInt());
                            room.setrImg(String.valueOf(R.drawable.bed_room));
                            room.setrSize("0");
                            room.setrName(flatDialog.getFirstTextField().toString());
                            boolean checkInsert = roomController.insertSV(room);

                            if (checkInsert == true) {
                                Intent intent;
                                intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Thêm Thành Công", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(MainActivity.this, "Thêm Thất Bại", Toast.LENGTH_SHORT).show();
                            }
                            // đóng dialog đi
                            flatDialog.dismiss();
                        }

                    }
                })
                .withSecondButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flatDialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.nav_settings:
                navigationView.setCheckedItem(R.id.nav_settings);
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
//            case R.id.nav_connect_local:
//                checkConnectLocal = false;
//                ipLocal = readFile(SHARED_PREFERENCES_IPTCPClient, "ip");
//                Toast.makeText(MainActivity.this, "+"+ipLocal+"+", Toast.LENGTH_LONG).show();
//                if (ipLocal != "null"){
//                    tcpClient = new TCPClient();
//                    checkConnectLocal = true;
//                }else {
//                    checkConnectLocal = false;
//                    getLocalInf();
//                }
//                break;
//            case  R.id.nav_connect_online:
//                checkConnectLocal = false;
//                intent = new Intent(MainActivity.this, MainActivity.class);
//                startActivity(intent);
//                break;
            case R.id.nav_reload_device:
                dialogReloadDevice();
                break;

//            case R.id.nav_feedback:
//
//                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

//    private void getLocalInf(){
//        if (codeNumber!="null"&&MqttClient.client.isConnected() != false){
//            MainActivity.mqttClient.puslish((String) getText(R.string.mess_request), "req/" + MainActivity.codeNumber);
//            String req = MqttClient.request;
//            if (req!=null){
//                String ip = null;
//                try {
//                    JSONObject jsonObject = new JSONObject(req);
//                    ip = jsonObject.getString("ip");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                saveIP("ip", ip);
//            }else {
//                Toast.makeText(MainActivity.this,"Request Null.", Toast.LENGTH_SHORT).show();
//            }
//        }else {
//            Toast.makeText(MainActivity.this,"Please enter codeNumber and reconnect", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void saveIP(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_IPTCPClient, Context.MODE_PRIVATE);
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
