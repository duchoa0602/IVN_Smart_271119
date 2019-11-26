package com.ivn.ivnsmart.ui.home;

import com.ivn.ivnsmart.R;
import com.ivn.ivnsmart.model.Device;
import com.ivn.ivnsmart.model.Room;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private List <Device> listDevice;
    private List <Room> listRoom;
    private List<Device>deviceRoomList;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        listDevice = new ArrayList<>();
        mText.setValue("This is home fragment");
      listDevice.add(new Device("1", R.drawable.light, "Fan", "10:00", "09:00", true));
//        listDevice.add(new Device("2",R.drawable.light, "Light", "11:00", "09:00", false));
//        listDevice.add(new Device("3",R.drawable.light, "dsdd", "10:00", "09:00", true));
//        listDevice.add(new Device("4",R.drawable.light, "mnhnhn", "11:00", "09:00", false));

//        deviceRoomList = new ArrayList<>();
//        deviceRoomList.add(new Device(R.drawable.light, "Fan", "10:00", "09:00", true));
//        deviceRoomList.add(new Device(R.drawable.light, "Light", "12:00", "09:00", false));


        listRoom = new ArrayList<>();
//        listRoom.add(new Room("Bed Room","A12", String.valueOf(deviceRoomList.size()) + " Device", R.drawable.bed_room));
//        listRoom.add(new Room("Living Room","A23",String.valueOf(deviceRoomList.size()) + " Device", R.drawable.work_room));
    }

    public LiveData<String> getText() {
        return mText;
    }

    public List<Device> getListDevice() {
        return listDevice;
    }

    public void setListDevice(List<Device> listDevice) {
        this.listDevice = listDevice;
    }

    public List<Room> getListRoom() {
        return listRoom;
    }

    public void setListRoom(List<Room> listRoom) {
        this.listRoom = listRoom;
    }

    public void createNewRoom(Room room){
        listRoom.add(room);
    }

    public void deleteRoom(Room room){
        listRoom.remove(room);
    }

    public void removeDevice(Device device){
        deviceRoomList.remove(device);
    }
}