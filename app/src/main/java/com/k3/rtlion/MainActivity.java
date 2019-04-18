package com.k3.rtlion;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private PagesAdapter pagesAdapter;
    private MainPageFrag mainPageFrag;
    private SettingsPageFrag settingsPageFrag;
    private GraphPageFrag graphPageFrag;
    private ScannerPageFrag scannerPageFrag;
    private int currentPageNum,
            hostAddrPassed = 0,
            offScreenLimit = 5;
    private JSInterface jsInterface;
    private Object[] uiObjects;

    private ImageView imgRtlionSmall;
    private TextView txvRtlionFramework, txvPageNum, txvPrevPage, txvNextPage;
    private XViewPager vpPages;
    private WebView wvBase;

    private void init(){
        imgRtlionSmall = (ImageView) findViewById(R.id.imgRtlionSmall);
        imgRtlionSmall.setOnClickListener(new imgRtlionSmall_onClick());
        txvPageNum = (TextView) findViewById(R.id.txvPageNum);
        txvPrevPage = (TextView) findViewById(R.id.txvPrevPage);
        txvNextPage = (TextView) findViewById(R.id.txvNextPage);
        txvRtlionFramework = (TextView) findViewById(R.id.txvRtlionFramework);
        wvBase = (WebView) findViewById(R.id.wvBase);
        jsInterface = new JSInterface(this);
        jsInterface.initialize(wvBase);
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
        vpPages = (XViewPager) findViewById(R.id.vpPages);
        vpPages.setOffscreenPageLimit(offScreenLimit);
        pagesAdapter = new PagesAdapter(this, new PagesAdapter.IViewPager() {
            @Override
            public void onViewsAdded(ArrayList<ViewGroup> layouts) {
                mainPageFrag = new MainPageFrag(MainActivity.this, layouts.get(0), jsInterface);
                mainPageFrag.initialize();
                settingsPageFrag = new SettingsPageFrag(MainActivity.this, layouts.get(1), jsInterface);
                settingsPageFrag.initialize();
                graphPageFrag = new GraphPageFrag(MainActivity.this, layouts.get(2), jsInterface);
                graphPageFrag.initialize();
                scannerPageFrag = new ScannerPageFrag(MainActivity.this, layouts.get(3), jsInterface);
                scannerPageFrag.initialize();
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
        if (mainPageFrag != null && !mainPageFrag.getConnectionStatus()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    vpPages.setCurrentItem(0);
                }
            }, 500);
        }else if (mainPageFrag != null &&
                mainPageFrag.getConnectionStatus() &&
                hostAddrPassed == 0){
            uiObjects = new Object[]{
                    vpPages,
                    mainPageFrag,
                    settingsPageFrag,
                    graphPageFrag,
                    scannerPageFrag};
            settingsPageFrag.setUIObjects(uiObjects);
            graphPageFrag.setUIObjects(uiObjects);
            scannerPageFrag.setUIObjects(uiObjects);
            hostAddrPassed = 1;
        }
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
}
