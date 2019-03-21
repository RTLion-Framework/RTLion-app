package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

public class SettingsPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;
    private JSInterface jsInterface;

    public SettingsPageFrag(Activity activity, ViewGroup viewGroup, JSInterface jsInterface){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
        this.jsInterface = jsInterface;
    }

    private void initViews(){

    }
    public void initialize(){
        initViews();
    }
}
