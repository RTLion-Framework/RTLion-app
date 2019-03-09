package com.k3.rtlion;


import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

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
        tryConnect();
    }
    private boolean connectSocket(){
        try {
            socket = IO.socket("http://192.168.1.36:8081").connect();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return socket.connected();
    }
}
