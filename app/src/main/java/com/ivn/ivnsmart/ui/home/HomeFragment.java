package com.ivn.ivnsmart.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ivn.ivnsmart.R;
import com.ivn.ivnsmart.adapter.ListDeviceAdapter;
import com.ivn.ivnsmart.adapter.ListRoomAdapter;
import com.ivn.ivnsmart.controller.DeviceController;
import com.ivn.ivnsmart.controller.RoomController;
import com.ivn.ivnsmart.model.Device;
import com.ivn.ivnsmart.model.Room;
import com.ivn.ivnsmart.view.RoomActivity;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment {
    private RoomController roomController;
    private DeviceController deviceController;
    private HomeViewModel homeViewModel;
    private List<Device> listDevice;
    public static  List<Room> listRoom;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        roomController = new RoomController(getContext());
        try {
            roomController.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        deviceController = new DeviceController(getContext());
        try {
            deviceController.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.out_group, container, false);

        ListView lvDevice = root.findViewById(R.id.lvDevice);
        listDevice = deviceController.getAllDevice();
        ListDeviceAdapter listDeviceAdapter = new ListDeviceAdapter(getContext(), R.layout.item_device ,listDevice);
        lvDevice.setAdapter(listDeviceAdapter);

        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"Test", Toast.LENGTH_LONG).show();
            }
        });

        ListView lvRoom = root.findViewById(R.id.lvRoom);
        if(roomController.getAllRoom()==null){

        }else {
            listRoom = roomController.getAllRoom();
            ListRoomAdapter listRoomAdapter = new ListRoomAdapter(getContext(), R.layout.item_room_device, listRoom);
            lvRoom.setAdapter(listRoomAdapter);
        }


        lvRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"Test", Toast.LENGTH_LONG).show();
                roomManager(position);
            }
        });

        return root;
    }

    public void roomManager(int position){
        Intent intent = new Intent(getActivity(), RoomActivity.class);
        startActivity(intent);
    }

}