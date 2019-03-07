package com.k3.rtlion;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Window;

public class MainActivity extends Activity {

    private LayoutInflater layoutInflater;
    private Dialog splashDialog;

    private void init(){
        hideActionBar();
        layoutInflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        showSplash();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void hideActionBar(){
        try{
            getActionBar().hide();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void showSplash(){
        try {
            splashDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            splashDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            splashDialog.setContentView(layoutInflater.inflate(R.layout.layout_splash, null));
            splashDialog.setCancelable(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    splashDialog.cancel();
                }
            }, 3000);
            splashDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
