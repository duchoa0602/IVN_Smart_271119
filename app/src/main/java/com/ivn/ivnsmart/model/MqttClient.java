package com.ivn.ivnsmart.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ivn.ivnsmart.R;
import com.ivn.ivnsmart.controller.DeviceController;
import com.ivn.ivnsmart.view.CheckQRCodeActivity;
import com.ivn.ivnsmart.view.MainActivity;
import com.ivn.ivnsmart.view.WelcomeActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MqttClient {

    String TAG = "esp32_dien_hoa";
    public static MqttAndroidClient client;
    public static final String urlD = "tcp://soldier.cloudmqtt.com:15258";
    public static final String urlH = "tcp://soldier.cloudmqtt.com:11785";
    public static final String userD = "jhqlhwjr";
    public static final String passD = "26Yvz_31t907";
    public static final String userH = "vyygqmpc";
    public static final String passH = "NXumW4IYXU9A";
    public static final String topicH = "TopicTest";
    public static final String topicD = "cmd/355684292";
    public static String request;
    public static boolean checkConnectSv = false;


    public void connect(final Context context, String url){

//        String user = CheckQRCodeActivity.user.getUser();
//        String pass = CheckQRCodeActivity.user.getPass();
//        String topic = "cmd/"+CheckQRCodeActivity.user.getMacID();

        String clientId = org.eclipse.paho.client.mqttv3.MqttClient.generateClientId();
        client = new MqttAndroidClient(context, url,
                clientId);

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(userD);
            options.setPassword(passD.toCharArray());

            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(context, "connecting success", Toast.LENGTH_SHORT).show();
                    subscribeToTopic(context, "config/" + WelcomeActivity.codeNumber);
//                    MainActivity.tvCheckConnected.setText(R.string.online);
//                    MainActivity.tvCheckConnected.setTextColor(Color.GREEN);
//                    MainActivity.imgCheckConnected.setImageResource(R.drawable.ic_online);
//                    MainActivity.checkConnectLocal = false;
                    checkConnectSv = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(context, "connecting fail, check internet and re-connect", Toast.LENGTH_SHORT).show();
//                    MainActivity.tvCheckConnected.setText(R.string.offline);
//                    MainActivity.tvCheckConnected.setTextColor(Color.RED);
//                    MainActivity.imgCheckConnected.setImageResource(R.drawable.ic_offline);
                    checkConnectSv = false;
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribeToTopic(final Context context, final String subscriptionTopic) {
        Log.d(TAG, "Message:  " + subscriptionTopic);
        try {
            client.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(context, "Subscribed! " + subscriptionTopic, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //Toast.makeText(ct, "Failed to subscribe", Toast.LENGTH_SHORT).show();
                }
            });

            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectionLost(Throwable throwable) {
                    Toast.makeText(context, "The Connection was lost.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    String messageReceived = new String(mqttMessage.getPayload());
                    Log.d(TAG, "messageArrived: " + messageReceived);
                    request = messageReceived;
                    Toast.makeText(context, messageReceived, Toast.LENGTH_SHORT).show();
                    if (request != null && request.indexOf("relayStatus") != -1){
                        parseData(request, context);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    //Toast.makeText(ct, "Message delivered", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    if (reconnect) {
                        //Toast.makeText(ct,  "Reconnected to : " + serverURI, Toast.LENGTH_SHORT).show();
                        // Because Clean Session is true, we need to re-subscribe
                    } else {
                        //Toast.makeText(ct,  "Connected to : " + serverURI, Toast.LENGTH_SHORT).show();
                    }
                }
            }) ;

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    public void puslish(String mess, String topic){
        String payload = mess;
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void parseData(String url, Context ct){
        DeviceController deviceController = new DeviceController(ct);
        Device device = new Device();
//        Toast.makeText(ct, "=="+url, Toast.LENGTH_SHORT).show();
        try {
            JSONObject jsonObject = new JSONObject(url);
            JSONArray array = jsonObject.getJSONArray("relayStatus");
            for (int i = 0;i<array.length();i++){
                // JSONObject number  =array.getJSONObject(i);
                // String g =  number.getString(String.valueOf(i));
                int r = array.getInt(i);
//                Log.d("status:", String.valueOf(r));
                device.setmDeviceID(String.valueOf(i));
                deviceController.insertStatusDevices(device, String.valueOf(r));
            }
            Intent intent;
            intent = new Intent(ct, MainActivity.class);
            ct.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
