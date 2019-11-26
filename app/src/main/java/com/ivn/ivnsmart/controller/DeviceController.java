package com.ivn.ivnsmart.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.ivn.ivnsmart.connectdatabase.DataBase;
import com.ivn.ivnsmart.model.Device;

import java.util.ArrayList;

public class DeviceController extends DataBase {
    public DeviceController(Context con) {
        super(con);
    }
    public ArrayList<Device> getAllDevice(){
        ArrayList<Device> listDevice = new ArrayList<>();
        try {
            // Trước khi thao tác với file sqlite thì phải mở nó ra
            openDataBase();
            // Lấy ra các bản ghi có trong file qlsv.db trong bộ nhớ trong
            // database.rawQuery() dùng để truy vấn ra dữ liệu sử dụng ngôn ngữ sql
            StringBuffer buffer = new StringBuffer();
           // String selectQuery = "SELECT * FROM devices WHERE roomid = " ;
            String selectQuery = "SELECT * FROM devices WHERE  ";
            buffer.append(selectQuery);
             buffer.append("roomid ");
             buffer.append("IS NULL");
            Cursor cursor = database.rawQuery(buffer.toString(),null);

            // Lấy các thông tin ra từ trong đối tượng Cursor
            while (cursor.moveToNext()) {
                // Lấy ra giá trị của từng bản ghi
                //int roomid = cursor.getInt(0);
                int deviceId = cursor.getInt(0);
                String image = cursor.getString(2);
                String nameDevice = cursor.getString(3);
                String timerOff = cursor.getString(4);
                String timerOn = cursor.getString(5);
                boolean mStatus ;
                if(cursor.getString(6).equals("0")){
                   mStatus = true;
                }else mStatus = false;
                Device device1 = new Device(String .valueOf(deviceId), Integer.valueOf (image), nameDevice, timerOff,timerOn,mStatus);
                Log.d("gfggfgggf", String.valueOf(mStatus));
                listDevice.add(device1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng db lại
            close();
        }
        return listDevice;
    }

    public boolean insertDevice(Device device ,int number) {
        try {
            openDataBase();
            int i = 0;
            ContentValues values = new ContentValues();
            while ( i<number) {
                values.put("deviceroomid",i);
            values.put("roomid", (byte[]) null);
            values.put("image", device.getmPhoto());
            values.put("nameDevice", "devide "+ i);
            values.put("timerOff", "0");
            values.put("timerOn","0");
            values.put("mStatus","1");
            long id = database.insert("devices", null, values);
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

      public void deleteDevice(){
          try {
              openDataBase();
                  long id = database.delete("devices", null, null);


          } catch (SQLException e) {
              e.printStackTrace();
          } finally {
              close();
          }

      }
    public String getRoomId(String deviceid){
        String roomid = null;
        try {
            openDataBase();
            StringBuffer buffer = new StringBuffer();
            // String selectQuery = "SELECT * FROM devices WHERE roomid = " ;
            String selectQuery = "SELECT * FROM devices WHERE   ";
            buffer.append(selectQuery);
            buffer.append("deviceroomid = ");
            buffer.append(deviceid);
            Cursor cursor = database.rawQuery(buffer.toString(),null);

            // Lấy các thông tin ra từ trong đối tượng Cursor
            while (cursor.moveToNext()) {
                 roomid = cursor.getString(1);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng db lại
            close();
        }
        return roomid ;
    }

    public ArrayList<Device> getDeviceId(String roomid){
        ArrayList<Device> listDevice = new ArrayList<>();
        try {
            // Trước khi thao tác với file sqlite thì phải mở nó ra
            openDataBase();
            // Lấy ra các bản ghi có trong file qlsv.db trong bộ nhớ trong
            // database.rawQuery() dùng để truy vấn ra dữ liệu sử dụng ngôn ngữ sql
            StringBuffer buffer = new StringBuffer();
            // String selectQuery = "SELECT * FROM devices WHERE roomid = " ;
            String selectQuery = "SELECT * FROM devices WHERE   ";
            buffer.append(selectQuery);
            buffer.append("roomid =  ");
            buffer.append(roomid);
            Cursor cursor = database.rawQuery(buffer.toString(),null);

            // Lấy các thông tin ra từ trong đối tượng Cursor
            while (cursor.moveToNext()) {
                // Lấy ra giá trị của từng bản ghi
                //int roomid = cursor.getInt(0);
                int deviceId = cursor.getInt(0);
                String image = cursor.getString(2);
                String nameDevice = cursor.getString(3);
                String timerOff = cursor.getString(4);
                String timerOn = cursor.getString(5);
                String mStatus = cursor.getString(6);
                Device device1 = new Device(String.valueOf(deviceId), Integer.valueOf (image), nameDevice, timerOff,timerOn,Boolean.valueOf(mStatus));
                listDevice.add(device1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng db lại
            close();
        }
        return listDevice;
    }
    public boolean updateDevice(Device device,String roomid) {
        try {
            openDataBase();
            // KHởi tạo contetnValue để chuyển từ đối tượng sang bản ghi
            ContentValues values = new ContentValues();
            values.put("roomid",roomid );
            values.put("image", device.getmPhoto());
            values.put("nameDevice", device.getmDeviceName());
            values.put("timerOff", device.getmTimerOff());
            values.put("timerOn", device.getmTimerOn());
            values.put("mStatus",device.ismStatus());

            long id = database.update("devices", values,"deviceroomid = "+ device.getmDeviceID(), null);
            Log.d("ccdcdcdcc",device.getmDeviceID());
            if (id > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
    public boolean deleteDeviceinRoom(Device device) {
        try {
            openDataBase();
            // KHởi tạo contetnValue để chuyển từ đối tượng sang bản ghi
            ContentValues values = new ContentValues();
            values.put("roomid", (byte[]) null);
            values.put("image", device.getmPhoto());
            values.put("nameDevice", device.getmDeviceName());
            values.put("timerOff", device.getmTimerOff());
            values.put("timerOn", device.getmTimerOn());
            values.put("mStatus",device.ismStatus());

            long id = database.update("devices", values,"deviceroomid = "+ device.getmDeviceID(), null);
            Log.d("ccdcdcdcc",device.getmDeviceID());
            if (id > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
    public void  insertStatusDevices(Device device ,String status){
        try {
            openDataBase();
            ContentValues values = new ContentValues();
            values.put("mStatus",status);
            long id  = database.update("devices",values,"deviceroomid = "+ device.getmDeviceID(),null);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
  public void updateNameDevice(Device device ){
      try {
          openDataBase();
          ContentValues values = new ContentValues();
          values.put("nameDevice", device.getmDeviceName());
          long id  = database.update("devices",values,"deviceroomid = "+ device.getmDeviceID(),null);
      } catch (SQLException e) {
          e.printStackTrace();
      } finally {
          close();
      }
  }

}
