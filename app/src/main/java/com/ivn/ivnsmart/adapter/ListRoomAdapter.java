package com.ivn.ivnsmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ivn.ivnsmart.R;
import com.ivn.ivnsmart.controller.RoomController;
import com.ivn.ivnsmart.model.Room;
import com.ivn.ivnsmart.view.RoomActivity;
import java.util.List;


public class ListRoomAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private List<Room>roomList;
     private RoomController roomController;
    public ListRoomAdapter(Context context, int resource, List<Room>roomList) {
        super(context, resource, roomList);
        this.context = context;
        this.resource = resource;
        this.roomList = roomList;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;
//        MyViewHolder myViewHolder;
        roomController = new RoomController(context);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_room_device,parent,false);
            viewHolder = new ListRoomAdapter.ViewHolder();

            viewHolder.imgRoom = convertView.findViewById(R.id.imgRoom);
            viewHolder.tvRoomName = convertView.findViewById(R.id.tvRoomName);
            viewHolder.tvRoomSize = convertView.findViewById(R.id.tvRoomSize);
            viewHolder.linearLayout = convertView.findViewById(R.id.linearLayoutInRoom);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ListRoomAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.imgRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RoomActivity.class);
                intent.putExtra("nameRoom",roomList.get(position).getrName());
                intent.putExtra("roomId",roomList.get(position).getrID());
                intent.putExtra("image",roomList.get(position).getrImg());
                intent.putExtra("number",roomList.get(position).getrSize());
                Log.d("Room: ", String.valueOf(roomList.get(position).getrID()));
                getContext().startActivity(intent);
            }
        });

        Room room = roomList.get(position);
        viewHolder.imgRoom.setImageResource(Integer.parseInt(room.getrImg()));
        viewHolder.tvRoomName.setText(room.getrName());
        viewHolder.tvRoomSize.setText(room.getrSize());

        return convertView;
    }

    private class ViewHolder {
        ImageView imgRoom;
        TextView tvRoomName;
        TextView tvRoomSize;
        LinearLayout linearLayout;
    }

//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        private ImageView imgRoom;
//        private TextView tvRoomName;
//        private TextView tvRoomSize;
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            imgRoom = itemView.findViewById(R.id.imgRoom);
//            tvRoomName = itemView.findViewById(R.id.tvRoomName);
//            tvRoomSize = itemView.findViewById(R.id.tvRoomSize);
//        }
//    }
}
