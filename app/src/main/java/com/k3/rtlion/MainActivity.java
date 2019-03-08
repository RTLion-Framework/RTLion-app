package com.k3.rtlion;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView txvRtlionFramework;
    private ViewPager vpPages;

    private void init(){
        hideActionBar();
        txvRtlionFramework = (TextView) findViewById(R.id.txvRtlionFramework);
        txvRtlionFramework.setTypeface(new SplashScreen(this).getUbuntuMonoFont());
        vpPages = (ViewPager) findViewById(R.id.vpPages);
        vpPages.setAdapter(new PagesAdapter(this));
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
