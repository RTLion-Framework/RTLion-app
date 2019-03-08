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
        vpPages_onPageChange(0);
        vpPages = (ViewPager) findViewById(R.id.vpPages);
        pagesAdapter = new PagesAdapter(this, new PagesAdapter.IViewPager() {
            @Override
            public void onViewsAdded(ArrayList<ViewGroup> layouts) {
                mainPageFrag = new MainPageFrag(MainActivity.this, layouts.get(0));
                mainPageFrag.init();
            }
        });
        vpPages.setAdapter(pagesAdapter);
        vpPages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                vpPages_onPageChange(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
    private void vpPages_onPageChange(int position){
        txvRtlionFramework.setText(getString(R.string.app_desc_long) + " > " +
                getString(PagesAdapter.PagesEnum.values()[position].getTitleResID()));
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
