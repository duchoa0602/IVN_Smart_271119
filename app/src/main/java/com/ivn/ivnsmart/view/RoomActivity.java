package com.ivn.ivnsmart.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.ivn.ivnsmart.R;
import com.ivn.ivnsmart.adapter.ListDeviceAdapter;
import com.ivn.ivnsmart.controller.DeviceController;
import com.ivn.ivnsmart.controller.RoomController;
import com.ivn.ivnsmart.model.Device;
import com.ivn.ivnsmart.model.Room;
import com.ivn.ivnsmart.ui.home.HomeViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class RoomActivity extends AppCompatActivity {
    private List<Device> listDeviceSpinner;
    private HomeViewModel homeViewModel;
    private List <Device> listDv;
    private ListDeviceAdapter listDeviceAdapter;
    private ListView lvDeviceInRoom;
    DeviceController deviceController;
    private Integer roomId;
    private String roomName;
    private String numberDevice;
    private String imgRoom;
    private String name;
    private String image;
    private String timerOff;
    private String timerOn;
    private String mStatus;
    private String deviceId;
    ImageButton imgInRoom;
    TextView tvRNInRoom, tvRSInRoom;
    LinearLayout linearLayout;
    RoomController roomController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_room);

        deviceController = new DeviceController(this);
        roomController = new RoomController(this);

        setWidget();
//        Toolbar toolbar = findViewById(R.id.toolbarRoom);
//        setSupportActionBar(toolbar);

        Intent intentGetContent = getIntent();
        roomId = intentGetContent.getIntExtra("roomId",0);
        roomName = intentGetContent.getStringExtra("nameRoom");
        numberDevice = intentGetContent.getStringExtra("number");
        imgRoom = intentGetContent.getStringExtra("image");
        tvRNInRoom.setText(roomName);
        imgInRoom.setImageResource(Integer.valueOf(imgRoom));
        Log.d("Room's profile: ", roomId +"   "+ roomName +"   "+ imgRoom);

        try {
            deviceController.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            roomController.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        listDv = deviceController.getDeviceId(String.valueOf(roomId));
        tvRSInRoom.setText(String.valueOf(listDv.size()));

        Toast.makeText(RoomActivity.this, listDv.size() + " device ", Toast.LENGTH_SHORT).show();

        listDeviceAdapter = new ListDeviceAdapter(this, R.layout.item_device, listDv);
        lvDeviceInRoom.setAdapter(listDeviceAdapter);

        linearLayout.setOnLongClickListener( new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View view) {
                 final FlatDialog flatDialog = new FlatDialog(RoomActivity.this);
                 flatDialog.setTitle("Room")
                         .setSubtitle("Are you sure you want to remove the room? ")
                         .setBackgroundColor(Color.BLUE)
                         .setFirstButtonColor(Color.WHITE)
                         .setFirstButtonTextColor(Color.BLACK)
                         .setSecondButtonColor(Color.WHITE)
                         .setSecondButtonTextColor(Color.BLACK)
                         .setFirstButtonText("OK")
                         .setSecondButtonText("CANCEL")
                         .withFirstButtonListner(new View.OnClickListener() {
                             @Override
                             public void onClick(View view) {
                                 if(listDv.size()==0){
                                     roomController.deleteRoom(roomId);
                                     Intent intent = new Intent(RoomActivity.this,MainActivity.class);
                                     startActivity(intent);
                                 }else{
                                     Toast.makeText(RoomActivity.this,"You have to remove all the devices in the room.",Toast.LENGTH_LONG).show();

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

                 return false;
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

    private void setWidget() {
        imgInRoom = findViewById(R.id.imgInRoom);
        tvRNInRoom = findViewById(R.id.tvRNInRoom);
        tvRSInRoom = findViewById(R.id.tvRSInRoom);
        linearLayout=  findViewById(R.id.linearLayoutInRoom);
        lvDeviceInRoom = findViewById(R.id.lvLDInRoom);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentRM = new Intent(RoomActivity.this,MainActivity.class);
        startActivity(intentRM);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.device, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addDevice:
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                     addDeviceToRoom(listDv.get(i));
//                    }
//                });
                addDeviceToRoom();

                break;
            case R.id.removeDevice:

                break;
            case R.id.deleteRoom:

                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void addDeviceToRoom(){

      final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_device);
        dialog.setTitle("Create New Room: ");
        Button btnOk = (Button)dialog.findViewById(R.id.btnOk);
        Button btnCancel = (Button)dialog.findViewById(R.id.btnCancel);
        Spinner spinner  = (Spinner)dialog.findViewById(R.id.spi);
        final EditText textView = (EditText) dialog.findViewById(R.id.edtMSV);
        deviceController = new DeviceController(this);
        listDeviceSpinner =  new ArrayList<>();
        listDeviceSpinner = deviceController.getAllDevice();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listDeviceSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Device device1 = listDeviceSpinner.get(i);
                deviceId = device1.getmDeviceID();
                Log.d("htthyn",deviceId);
                textView.setText(device1.getmDeviceName());
                   name = device1.getmDeviceName();
                Log.d("htthyn",name);

                image = String.valueOf(device1.getmPhoto());
                   timerOff = device1.getmTimerOff();
                   timerOn = device1.getmTimerOn();
                   mStatus = String.valueOf(device1.ismStatus());



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Device device= new Device();
                device.setmDeviceID(deviceId);
                    device.setmDeviceName(name);
                    device.setmPhoto(Integer.parseInt(image));
                    device.setmTimerOff(timerOff);
                    device.setmTimerOn(timerOn);
                    device.setmStatus(Boolean.valueOf(mStatus));
                    Room room  =new Room();
                    room.setrID(roomId);
                    room.setrName(roomName);
                    room.setrImg(imgRoom);
                    room.setrSize(String.valueOf(listDv.size()));
                   boolean checkupdate = deviceController.updateDevice(device, String.valueOf(roomId));
                   boolean checkuproom = roomController.updateRoom(room,listDv.size());
                if (checkupdate == true) {
                    listDv = deviceController.getDeviceId(String.valueOf(roomId));
                    tvRSInRoom.setText(String.valueOf(listDv.size()));
                    listDeviceAdapter = new ListDeviceAdapter(RoomActivity.this, R.layout.item_device, listDv);
                    lvDeviceInRoom.setAdapter(listDeviceAdapter);

                    Toast.makeText(RoomActivity.this, "Thêm Thành Công", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(RoomActivity.this, "Thêm Thất Bại", Toast.LENGTH_SHORT).show();
                }
                   dialog.dismiss();

            }
        });
        dialog.show();
    }



}
