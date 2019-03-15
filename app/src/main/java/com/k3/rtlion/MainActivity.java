package com.k3.rtlion;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ImageView imgRtlionSmall;
    private TextView txvRtlionFramework, txvPageNum, txvPrevPage, txvNextPage;
    private ViewPager vpPages;
    private WebView wvBase;
    private PagesAdapter pagesAdapter;
    private MainPageFrag mainPageFrag;
    private int currentPageNum;
    private JSInterface JSInterface;

    private void init(){
        hideActionBar();
        imgRtlionSmall = (ImageView) findViewById(R.id.imgRtlionSmall);
        imgRtlionSmall.setOnClickListener(new imgRtlionSmall_onClick());
        txvPageNum = (TextView) findViewById(R.id.txvPageNum);
        txvPrevPage = (TextView) findViewById(R.id.txvPrevPage);
        txvNextPage = (TextView) findViewById(R.id.txvNextPage);
        txvRtlionFramework = (TextView) findViewById(R.id.txvRtlionFramework);
        wvBase = (WebView) findViewById(R.id.wvBase);
        JSInterface = new JSInterface(this, wvBase);
        txvRtlionFramework.setTypeface(new SplashScreen(this).getUbuntuMonoFont());
        initPageNavigators();
        setupViewPager();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        new SplashScreen(this).show();
    }
    private void setupViewPager(){
        vpPages_onPageChange(0);
        vpPages = (ViewPager) findViewById(R.id.vpPages);
        pagesAdapter = new PagesAdapter(this, new PagesAdapter.IViewPager() {
            @Override
            public void onViewsAdded(ArrayList<ViewGroup> layouts) {
                mainPageFrag = new MainPageFrag(MainActivity.this, layouts.get(0), JSInterface);
                mainPageFrag.initialize();
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
        txvPageNum.setText(String.valueOf(position+1));
        currentPageNum = position;
    }
    private void initPageNavigators(){
        txvPrevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPageNum - 1 >= 0)
                    vpPages.setCurrentItem(currentPageNum - 1);
                else
                    vpPages.setCurrentItem(PagesAdapter.PagesEnum.values().length - 1);
            }
        });
        txvNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPageNum + 1 < PagesAdapter.PagesEnum.values().length)
                    vpPages.setCurrentItem(currentPageNum + 1);
                else
                    vpPages.setCurrentItem(0);
            }
        });
        txvPageNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txvNextPage.performClick();
            }
        });
    }
    private class imgRtlionSmall_onClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            try {
                String projectPage = "https://github.com/RTLion-Framework";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(projectPage));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void hideActionBar(){
        try{
            getActionBar().hide();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
