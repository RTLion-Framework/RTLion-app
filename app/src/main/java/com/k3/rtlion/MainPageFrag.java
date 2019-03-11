package com.k3.rtlion;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;
    private TextInputLayout tilHostAddr;
    private EditText edtxHostAddr;
    private Button btnConnect;

    public MainPageFrag(Activity activity, ViewGroup viewGroup){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
    }

    private void initViews(){
        tilHostAddr = (TextInputLayout) viewGroup.findViewById(R.id.tilHostAddr);
        edtxHostAddr = (EditText) viewGroup.findViewById(R.id.edtxHostAddr);
        btnConnect = (Button) viewGroup.findViewById(R.id.btnConnect);
        edtxHostAddr.setTypeface(new SplashScreen(activity).getUbuntuMonoFont());
    }

    public void initialize(){
        initViews();
        new FetchAsyncTask("http://www.k3pwn.me", new FetchAsyncTask.AsyncResponse() {
            @Override
            public void onFetch(String source) {
                Toast.makeText(activity, source, Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }


}
