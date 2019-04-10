package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScannerPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;
    private JSInterface jsInterface;

    private TextView txvScannerWarning;
    private String hostAddr;

    public ScannerPageFrag(Activity activity, ViewGroup viewGroup, JSInterface jsInterface){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
        this.jsInterface = jsInterface;
    }

    private void initViews(){
        txvScannerWarning = viewGroup.findViewById(R.id.txvScannerWarning);
    }
    public void initialize(){
        initViews();
        txvScannerWarning.setVisibility(View.VISIBLE);
    }
    public void removeConWarning(){
        txvScannerWarning.setVisibility(View.GONE);
    }
    public void setHostAddr(String hostAddr){
        this.hostAddr = hostAddr;
    }
}