package com.k3.rtlion;


import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import java.net.URISyntaxException;


public class MainPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;
    private Socket socket;

    public MainPageFrag(Activity activity, ViewGroup viewGroup){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
    }

    public void init(){
        String uri = "http://192.168.1.36:8081";
        String isConnected = String.valueOf(connectSocket(uri));
        Toast.makeText(activity, isConnected, Toast.LENGTH_SHORT).show();
    }
    private boolean connectSocket(String uri){
        try {
            socket = IO.socket(uri).connect();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return socket.connected();
    }
}
