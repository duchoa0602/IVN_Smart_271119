package com.ivn.ivnsmart.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.ivn.ivnsmart.R;
import com.ivn.ivnsmart.controller.DeviceController;
import com.ivn.ivnsmart.model.Device;
import com.ivn.ivnsmart.model.MqttClient;
import com.ivn.ivnsmart.view.CheckQRCodeActivity;
import com.ivn.ivnsmart.view.MainActivity;
import com.ivn.ivnsmart.view.RoomActivity;
import com.ivn.ivnsmart.view.WelcomeActivity;
import com.michaldrabik.classicmaterialtimepicker.CmtpDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListDeviceAdapter extends ArrayAdapter{
    private Context context;
    private int resource;
    private List<Device>deviceList;
    private String mess = (String)getContext().getText(R.string.mess_on);
    DeviceController deviceController;
    private Calendar calendarStart = null, calendarEnd = null, calendarCd = null, calendarNow = Calendar.getInstance() ;
    private boolean state;
    private String check = "";
    private int timeCD;

    public ListDeviceAdapter(Context context, int resource, List<Device>deviceList) {
        super(context, resource, deviceList);
        this.context = context;
        this.resource = resource;
        this.deviceList = deviceList;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder viewHolder;
        deviceController = new DeviceController(context);
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_device,parent,false);

            viewHolder.linearLayout = convertView.findViewById(R.id.device_item_id);
            viewHolder.imageView = convertView.findViewById(R.id.imgDevice);
            viewHolder.tvDeviceName = convertView.findViewById(R.id.tvDeviceName);
            viewHolder.tvContentView = convertView.findViewById(R.id.tvContentView);
            viewHolder.imgBtnState = convertView.findViewById(R.id.imgBtnState);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Device device = deviceList.get(position);
        viewHolder.imageView.setImageResource(device.getmPhoto());
        viewHolder.tvDeviceName.setText(device.getmDeviceName());
        viewHolder.tvContentView.setText(device.getmContent());
        state = device.ismStatus();
        if (state==true){
            viewHolder.imgBtnState.setBackgroundResource(R.drawable.ic_sw_on);
        }else {
            viewHolder.imgBtnState.setBackgroundResource(R.drawable.ic_sw_off);
        }
//        Log.d("vfvfvfvv", String.valueOf(device.ismStatus()));
        viewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(deviceController.getRoomId(device.getmDeviceID())== null){

                }else {
                    Toast.makeText(context, "Delete device " + position, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder adb = new AlertDialog.Builder(context);
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to delete? " + position);
                    final int positionToRemove = position;
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deviceController.deleteDeviceinRoom(device);
                        }
                    });
                    adb.show();
                }
                return false;
            }

        });

//        viewHolder.swState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    mess = (String) getContext().getText(R.string.mess_on);
//                    String nMessOn = updateMess(mess, device);
//                    sendMessage(nMessOn,viewHolder.swState);
//                     //viewHolder.swState.setChecked(isChecked);
//                }else {
//                   /// viewHolder.swState.setChecked(isChecked);
//                    mess = (String) getContext().getText(R.string.mess_off);
//                    String nMessOff = updateMess(mess, device);
//                    sendMessage(nMessOff,viewHolder.swState);
//                }
//            }
//        });

        viewHolder.imgBtnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state==true){
                    mess = (String) getContext().getText(R.string.mess_off);
                    String nMessOff = updateMess(mess, device, "1");
                    sendMessage(nMessOff, state, viewHolder.imgBtnState);
                }else {
                    mess = (String) getContext().getText(R.string.mess_on);
                    String nMessOn = updateMess(mess, device, "0");
                    sendMessage(nMessOn, state, viewHolder.imgBtnState);
                }
            }
        });


        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_device);

                final TextView tvContent = dialog.findViewById(R.id.tvContent);
                final ImageView imageDvD = dialog.findViewById(R.id.imgDeviceDialog);
                Button btnSetTime = dialog.findViewById(R.id.btnSetTime);
                Button btnSelectTime = dialog.findViewById(R.id.btnSelectTime);
                Button btnSaveD = dialog.findViewById(R.id.btnSave);
                Button btnCancelD = dialog.findViewById(R.id.btnCancel);
                final EditText edtNameDvD = dialog.findViewById(R.id.edtDvNameD);

                Device dvD = deviceList.get(position);
                tvContent.setText(dvD.getmContent());
                imageDvD.setImageResource(dvD.getmPhoto());
                edtNameDvD.setText(dvD.getmDeviceName());

                btnSetTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialogSetTime = new Dialog(getContext());
                        dialogSetTime.setTitle("Timer:");
                        dialogSetTime.setContentView(R.layout.set_timer);

                        final TextView tvTimer, tvStateTimer, tvLoopTimer;
                        Spinner spiLoop;
                        Button btnOkTimer, btnCancelTimer;
                        Switch swStateTimer = dialogSetTime.findViewById(R.id.swStateTimer);
                        tvTimer = dialogSetTime.findViewById(R.id.tvTimer);
                        tvStateTimer = dialogSetTime.findViewById(R.id.tvStateTimer);
                        tvLoopTimer = dialogSetTime.findViewById(R.id.tvLoopTimer);
                        spiLoop = dialogSetTime.findViewById(R.id.spiLoop);
                        btnOkTimer = dialogSetTime.findViewById(R.id.btnOkTimer);
                        btnCancelTimer = dialogSetTime.findViewById(R.id.btnCancelTimer);

                        tvTimer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                calendarEnd = timerPickerDialog(tvTimer, getContext());
                            }
                        });

                        swStateTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    tvStateTimer.setText("On");
                                    tvStateTimer.setTextColor(Color.parseColor("#0000FF"));
                                }else {
                                    tvStateTimer.setText("Off");
                                    tvStateTimer.setTextColor(Color.parseColor("#FF0000"));
                                }
                            }
                        });

                        btnOkTimer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tvContent.setTextColor(tvStateTimer.getTextColors());
                                tvContent.setText(tvTimer.getText()+ " " + tvStateTimer.getText());
                                check = "set";
                                dialogSetTime.cancel();
                            }
                        });

                        btnCancelTimer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogSetTime.cancel();
                            }
                        });

                        dialogSetTime.show();
                    }
                });

                btnSelectTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialogCd = new Dialog(getContext());
                        dialogCd.setTitle("Select time:");
                        dialogCd.setContentView(R.layout.dialog_timer_cd);

                        final Spinner spnH = dialogCd.findViewById(R.id.spnHour);
                        Spinner spnM = dialogCd.findViewById(R.id.spnMin);
                        Spinner spnS = dialogCd.findViewById(R.id.spnSecond);
                        Button btnOk = dialogCd.findViewById(R.id.btnOkTimerCd);
                        Button btnCancel = dialogCd.findViewById(R.id.btnCancelTimerCd);
                        Switch swStateTimerCd = dialogCd.findViewById(R.id.swStateTimerCd);
                        final TextView tvH, tvM, tvS, tvStateTimerCd;
                        tvStateTimerCd  = dialogCd.findViewById(R.id.tvStateTimerCd);
                        tvH = dialogCd.findViewById(R.id.tvH);
                        tvM = dialogCd.findViewById(R.id.tvM);
                        tvS = dialogCd.findViewById(R.id.tvS);


                        List<Integer> listH;
                        listH = addList(24);
                        onCreateSpinner(listH, spnH, tvH);

                        List<Integer> listM;
                        listM = addList(60);
                        onCreateSpinner(listM, spnM, tvM);

                        List<Integer> listS = listM;
                        onCreateSpinner(listS, spnS, tvS);

                        swStateTimerCd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    tvStateTimerCd.setText("On");
                                    tvStateTimerCd.setTextColor(Color.parseColor("#0000FF"));
                                }else {
                                    tvStateTimerCd.setText("Off");
                                    tvStateTimerCd.setTextColor(Color.parseColor("#FF0000"));
                                }
                            }
                        });

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int finalH, finalM, finalS;
                                int yearNow = calendarNow.get(Calendar.YEAR);
                                int monthNow = calendarNow.get(Calendar.MONTH);
                                int dateNow = calendarNow.get(Calendar.DATE);
                                int hourOfDay = calendarNow.get(Calendar.HOUR_OF_DAY);
                                int min = calendarNow.get(Calendar.MINUTE);
                                int second = calendarNow.get(Calendar.SECOND);

                                calendarCd = calendarNow;

                                if (String.valueOf(tvH.getText())!=""){
                                    finalH = Integer.parseInt(String.valueOf(tvH.getText()));
                                }else {
                                    finalH = 0;
                                }

                                if (String.valueOf(tvH.getText())!=""){
                                    finalM = Integer.parseInt(String.valueOf(tvM.getText()));
                                }else {
                                    finalM = 0;
                                }

                                if (String.valueOf(tvH.getText())!=""){
                                    finalS = Integer.parseInt(String.valueOf(tvS.getText()));
                                }else {
                                    finalS = 0;
                                }
                                Toast.makeText(getContext(), finalH + ":" + finalM + ":" + finalS,Toast.LENGTH_LONG).show();

                                if (finalH !=0 || finalM !=0 || finalS !=0){
                                    timeCD = finalH*3600 + finalM*60 + finalS;
                                    calendarCd.set(0, 0, 0, finalH, finalM, finalS);
                                    Toast.makeText(getContext(), String.valueOf(timeCD), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), "Please select time to countdown!", Toast.LENGTH_SHORT).show();
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                tvContent.setTextColor(tvStateTimerCd.getTextColors());
                                tvContent.setText(simpleDateFormat.format(calendarCd.getTime()) + " " + tvStateTimerCd.getText());
                                check = "select";
                                dialogCd.cancel();
                            }
                        });

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogCd.cancel();
                            }
                        });

                        dialogCd.show();

                    }
                });

                btnSaveD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ti = null, ts = "0", te = "0", tc = "0";
//                        Log.d("mess_ti: ", ti);
                        calendarNow = Calendar.getInstance();
                        ts = String.valueOf((calendarNow.getTimeInMillis())/1000);
                        deviceList.get(position).setmDeviceName(String.valueOf(edtNameDvD.getText()));
                        deviceController.updateNameDevice(device);
//                        mqttClient.updateMess(mess, String.valueOf(calendarOn.getTimeInMillis()/1000), String.valueOf(calendarOff.getTimeInMillis()/1000));
//                        mess = updateMess(json, String.valueOf(calendarOn.getTimeInMillis()/1000), String.valueOf(calendarOff.getTimeInMillis()/1000), String.valueOf(calendarCd.getTimeInMillis()/1000), ti, TAG);
//                        mqttClient.subscribeToTopic(topicH, mess, getContext(), TAG);
                        if(check.equals("")){
                            Toast.makeText(getContext(), "Timer none!!", Toast.LENGTH_SHORT).show();
                        }else if (check.equals("set")){
                            te = String.valueOf((calendarEnd.getTimeInMillis())/1000);
                            if (tvContent.getText().toString().indexOf("Off")!=-1){
                                ti = "0";
                            }
                            else if (tvContent.getText().toString().indexOf("On")!=-1){
                                ti = "1";
                            }else {
                                Toast.makeText(getContext(), "State None", Toast.LENGTH_SHORT).show();
                            }
                            device.setmTimerOff(te);
                            device.setmTimerOn(ts);
                            mess = updateMess(mess, device, ti);

                        } else if (check.equals("select")) {
                            te = String.valueOf((calendarNow.getTimeInMillis())/1000 + timeCD);
                            if (tvContent.getText().toString().indexOf("Off")!=-1){
                                ti = "0";
                            }
                            else if (tvContent.getText().toString().indexOf("On")!=-1){
                                ti = "1";
                            }else {
                                Toast.makeText(getContext(), "State None", Toast.LENGTH_SHORT).show();
                            }
                            device.setmTimerOff(te);
                            device.setmTimerOn(ts);
                            mess = updateMess(mess, device, ti);

                        }
//                        Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
                        sendMessageTimer(mess);
//                        dialog.cancel();
                    }
                });

                btnCancelD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


                dialog.show();
            }
        });

        return convertView;
    }



    private class ViewHolder{
        ImageView imageView;
        TextView tvDeviceName;
        TextView tvContentView;
        ImageButton imgBtnState;
        LinearLayout linearLayout;
    }

    public Calendar timerPickerDialog(final TextView textView, final Context context){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int date = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        final Calendar finalCalendar = calendar;

        TimePickerDialog timePD = new TimePickerDialog(context, android.R .style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                finalCalendar.set(year, month, date, hourOfDay, minute);
                textView.setText(simpleDateFormat.format(finalCalendar.getTime()));
            }
        }, hour, min, true);

        timePD.show();
        return finalCalendar;
    }


    public String updateMess(String json, Device device, String ti){
        JSONObject jsonObject = null;
        int id = Integer.parseInt(device.getmDeviceID());
        try {
            jsonObject = new JSONObject(json);
            jsonObject.put("id", id);
            jsonObject.put("relayID", id);
            jsonObject.put("ts", device.getmTimerOn());
            jsonObject.put("te", device.getmTimerOff());
//            jsonObject.put("tc", device.getmTimerCycle());
            jsonObject.put("ti", ti);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(getContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
        return jsonObject.toString();
    }
    public Device getJson(String json){
        Device device = new Device();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            device.setmStatus(Boolean.parseBoolean(jsonObject.getString("ti")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return device;
    }

    public List <Integer> addList(int finNumber){
        List <Integer> list = new ArrayList<>();
        for (int i = 0; i<finNumber; i++){
            list.add(i);
        }
        return list;
    }

    public int onCreateSpinner(List <Integer> list, final Spinner spinner, final TextView textView){
        ArrayAdapter<Integer> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                textView.setText(spinner.getSelectedItem().toString());
                Toast.makeText(getContext(), String.valueOf(spinner.getSelectedItemPosition()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return spinner.getSelectedItemPosition();
    }

    public void sendMessage(String mess, boolean st, ImageButton imageButton){
        boolean finalSt = st;
        if (!TextUtils.isEmpty(WelcomeActivity.codeNumber)) {
            if (MainActivity.checkConnectLocal!=false) {
                Toast.makeText(getContext(), "Local/" + mess, Toast.LENGTH_SHORT).show();
                localControl(mess);
                finalSt = !st;
            }else if (MqttClient.client.isConnected()!=false) {
                Toast.makeText(getContext(), "Online/" + mess, Toast.LENGTH_SHORT).show();
                WelcomeActivity.mqttClient.puslish(mess, "cmd/" + WelcomeActivity.codeNumber);
                finalSt = !st;
            }else {
                finalSt = st;
                WelcomeActivity.mqttClient.connect(getContext(), MqttClient.urlD);
                Toast.makeText(getContext(), "Please connect Internet or re-connect local", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getContext(), "Please enter code number!", Toast.LENGTH_LONG).show();
        }
        if (finalSt==true){
            imageButton.setBackgroundResource(R.drawable.ic_sw_on);
            state = true;
        }else {
            imageButton.setBackgroundResource(R.drawable.ic_sw_off);
            state = false;
        }
    }

    public void sendMessageTimer(String mess){
        if (!TextUtils.isEmpty(WelcomeActivity.codeNumber)) {
            if (MainActivity.checkConnectLocal!=false) {
                Toast.makeText(getContext(), "Local/" + mess, Toast.LENGTH_SHORT).show();
                localControl(mess);
            }else if (MqttClient.client.isConnected()!=false) {
                Toast.makeText(getContext(), "Online/" + mess, Toast.LENGTH_SHORT).show();
                WelcomeActivity.mqttClient.puslish(mess, "cmd/" + WelcomeActivity.codeNumber);
            }else {
                WelcomeActivity.mqttClient.connect(getContext(), MqttClient.urlD);
                Toast.makeText(getContext(), "Please connect Internet or re-connect local", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getContext(), "Please enter code number!", Toast.LENGTH_LONG).show();
        }
    }

    public void localControl(String mess){
            MainActivity.tcpClient.sendMessage(mess, MainActivity.ipLocal ,getContext());
    }

    public void parseData(String url, Device device){
        DeviceController deviceController = new DeviceController(getContext());
        Toast.makeText(getContext(), "=="+url, Toast.LENGTH_SHORT).show();
        try {
            JSONObject jsonObject = new JSONObject(url);
            JSONArray array = jsonObject.getJSONArray("relayStatus");
            for (int i = 0;i<array.length();i++){
                // JSONObject number  =array.getJSONObject(i);
                // String g =  number.getString(String.valueOf(i));
                int r = array.getInt(i);
                device.setmDeviceID(String.valueOf(i));
                deviceController.insertStatusDevices(device, String.valueOf(r));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
