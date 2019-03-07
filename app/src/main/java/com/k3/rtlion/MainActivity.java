package com.k3.rtlion;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private LayoutInflater layoutInflater;
    private Dialog splashDialog;
    private Animation fadeInAnim, fadeOutAnim;
    private AnimationSet fadeAnims;

    private void init(){
        hideActionBar();
        initAnims(1500);
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
    private void initAnims(int duration){
        fadeInAnim = new AlphaAnimation(0, 1);
        fadeInAnim.setInterpolator(new DecelerateInterpolator());
        fadeInAnim.setDuration(duration);
        fadeOutAnim = new AlphaAnimation(1, 0);
        fadeOutAnim.setInterpolator(new AccelerateInterpolator());
        fadeOutAnim.setStartOffset(1000);
        fadeOutAnim.setDuration(duration);
        fadeAnims = new AnimationSet(false);
        fadeAnims.addAnimation(fadeInAnim);
        fadeAnims.addAnimation(fadeOutAnim);
    }
    private void showSplash(){
        try {
            layoutInflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            splashDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            splashDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            splashDialog.setContentView(layoutInflater.inflate(R.layout.layout_splash, null));
            splashDialog.setCancelable(false);
            ImageView imgRtlionLogo = (ImageView) splashDialog.findViewById(R.id.imgRtlionLogo);
            imgRtlionLogo.setAnimation(fadeAnims);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    splashDialog.cancel();
                }
            }, 5000);
            splashDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
