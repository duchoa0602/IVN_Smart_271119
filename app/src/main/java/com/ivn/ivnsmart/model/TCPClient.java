package com.ivn.ivnsmart.model;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {

    public void sendMessage(final String msg, final String serverIP, final Context ct) {

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.
                    final Socket s = new Socket(serverIP, 1234);

                    OutputStream out = s.getOutputStream();

                    PrintWriter output = new PrintWriter(out);
                    output.println(msg);
                    output.flush();

                    output.close();
                    out.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    String err = e.toString();
                    Log.d("Connect Err: ", err);
                }
            }
        });

        thread.start();
    }
}
