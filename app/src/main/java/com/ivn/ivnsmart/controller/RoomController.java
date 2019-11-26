package com.ivn.ivnsmart.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.ivn.ivnsmart.connectdatabase.DataBase;
import com.ivn.ivnsmart.model.Room;

import java.util.ArrayList;

public class RoomController extends DataBase {
    public RoomController(Context con) {
        super(con);
    }
public ArrayList<Room> getAllRoom(){
    ArrayList<Room> listRoom = new ArrayList<>();
    try {
        // Trước khi thao tác với file sqlite thì phải mở nó ra
        openDataBase();
        // Lấy ra các bản ghi có trong file qlsv.db trong bộ nhớ trong
        // database.rawQuery() dùng để truy vấn ra dữ liệu sử dụng ngôn ngữ sql
        String selectQuery = "SELECT * FROM room" ;

        Cursor cursor = database.rawQuery(selectQuery,null);

        // Lấy các thông tin ra từ trong đối tượng Cursor
        while (cursor.moveToNext()) {
            // Lấy ra giá trị của từng bản ghi
            int roomid = cursor.getInt(0);
            String name = cursor.getString(1);
            String image = cursor.getString(2);
            String device = cursor.getString(3);
            Room room = new Room(name, roomid, device, image);
            listRoom.add(room);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Đóng db lại
        close();
    }
    return listRoom;
}
    public boolean insertSV(Room room) {
        try {
            openDataBase();
            // KHởi tạo contetnValue để chuyển từ đối tượng sang bản ghi
            ContentValues values = new ContentValues();
            values.put("roomid",room.getrID());
            values.put("name", room.getrName());
            values.put("image", room.getrImg());
            values.put("device", room.getrSize());
            long id = database.insert("room", null, values);
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
    public boolean updateRoom( Room room,int devices) {
        try {
            openDataBase();
            // KHởi tạo contetnValue để chuyển từ đối tượng sang bản ghi
            ContentValues values = new ContentValues();
            values.put("roomid",room.getrID());
            values.put("name", room.getrName());
            values.put("image", room.getrImg());
            values.put("device", devices);
            // insert a room
            long id = database.update("room", values,"roomid = "+ room.getrID(),null );
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
    public boolean deleteRoom(double roomid) {
        try {
            openDataBase();

            long sl = database.delete("room", "roomid = " + roomid, null);
            if (sl > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
}
