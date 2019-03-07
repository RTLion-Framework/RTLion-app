package com.k3.rtlion;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    private void init(){
        hideActionBar();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        new SplashScreen(this).show();
    }
    private void hideActionBar(){
        try{
            getActionBar().hide();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
