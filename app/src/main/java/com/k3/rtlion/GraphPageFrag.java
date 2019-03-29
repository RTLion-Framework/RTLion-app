package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GraphPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;
    private JSInterface jsInterface;
    private String hostAddr;

    private TextView txvGraphWarning;
    private LinearLayout llGraph;
    private ImageView imgFFTGraph;

    public GraphPageFrag(Activity activity, ViewGroup viewGroup, JSInterface jsInterface){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
        this.jsInterface = jsInterface;
    }
    private void initViews(){
        txvGraphWarning = viewGroup.findViewById(R.id.txvGraphWarning);
        llGraph = viewGroup.findViewById(R.id.llGraph);
        imgFFTGraph = viewGroup.findViewById(R.id.imgFFTGraph);
    }
    public void initialize(){
        initViews();
        txvGraphWarning.setVisibility(View.VISIBLE);
        llGraph.setVisibility(View.GONE);
    }
    public void removeConWarning(){
        txvGraphWarning.setVisibility(View.GONE);
        llGraph.setVisibility(View.VISIBLE);
    }
    public void setHostAddr(String hostAddr){
        this.hostAddr = hostAddr;
    }
}
