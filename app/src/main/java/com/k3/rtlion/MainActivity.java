package com.k3.rtlion;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private TextView txvRtlionFramework;
    private ViewPager vpPages;
    private PagesAdapter pagesAdapter;
    private MainPageFrag mainPageFrag;

    private void init(){
        hideActionBar();
        txvRtlionFramework = (TextView) findViewById(R.id.txvRtlionFramework);
        txvRtlionFramework.setTypeface(new SplashScreen(this).getUbuntuMonoFont());
        vpPages = (ViewPager) findViewById(R.id.vpPages);
        pagesAdapter = new PagesAdapter(this, new PagesAdapter.IViewPager() {
            @Override
            public void onViewsAdded(ArrayList<ViewGroup> layouts) {
                mainPageFrag = new MainPageFrag(MainActivity.this, layouts.get(0)).init();
            }
        });
        vpPages.setAdapter(pagesAdapter);
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
