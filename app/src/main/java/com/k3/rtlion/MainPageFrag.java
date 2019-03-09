package com.k3.rtlion;


import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;


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

    }
    private void tryConnect(){
        try {
            socket = new IO.socket("http://192.168.1.36:8081");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
