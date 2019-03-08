package com.k3.rtlion;


import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

public class MainPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;

    public MainPageFrag(Activity activity, ViewGroup viewGroup){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
    }

    public void init(){
        // initialize views
    }
}
