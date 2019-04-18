package com.k3.rtlion.ui;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.k3.rtlion.R;

public class SplashScreen {

    private Activity activity;
    private Context context;
    private LayoutInflater layoutInflater;
    private Dialog splashDialog;
    private Typeface tfUbuntuMono;
    private String ubuntuMonoFont = "fonts/UbuntuMono_R.ttf", loadingDots = "...";
    final int splashDelay1 = 600, splashDelay2 = 1300;

    private ImageView imgRtlionLogo;
    private RelativeLayout rlProjectDesc;
    private TextView txvRtlionDesc, txvAuthor, txvLoading;

    private void initDialog(Dialog splashDialog){
        imgRtlionLogo = (ImageView) splashDialog.findViewById(R.id.imgRtlionLogo);
        rlProjectDesc = (RelativeLayout) splashDialog.findViewById(R.id.rlProjectDesc);
        txvRtlionDesc = (TextView) splashDialog.findViewById(R.id.txvRtlionDesc);
        txvAuthor = (TextView) splashDialog.findViewById(R.id.txvAuthor);
        txvLoading = (TextView) splashDialog.findViewById(R.id.txvLoading);
        tfUbuntuMono = getUbuntuMonoFont();
        txvRtlionDesc.setTypeface(tfUbuntuMono);
        txvAuthor.setTypeface(tfUbuntuMono);
        txvLoading.setTypeface(tfUbuntuMono);
    }
    public SplashScreen(Activity activity){
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }
    public void show(){
        try {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.
                    LAYOUT_INFLATER_SERVICE);
            splashDialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            splashDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            splashDialog.setContentView(layoutInflater.inflate(R.layout.layout_splash, null));
            splashDialog.setCancelable(false);
            initDialog(splashDialog);
            setAnimations();
            animateDotsLoading();
            splashDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Typeface getUbuntuMonoFont(){
        return Typeface.createFromAsset(context.getAssets(), ubuntuMonoFont);
    }
    private Animation fadeAnimation(int duration){
        Animation fadeInAnim = new AlphaAnimation(0, 1);
        fadeInAnim.setInterpolator(new DecelerateInterpolator());
        fadeInAnim.setDuration(duration);
        return fadeInAnim;
    }
    private void setAnimations(){
        imgRtlionLogo.setAnimation(fadeAnimation(1000));
        txvRtlionDesc.setAnimation(fadeAnimation(1500));
        rlProjectDesc.setAnimation(fadeAnimation(1500));
        txvAuthor.setAnimation(fadeAnimation(500));
    }
    private void animateDotsLoading(){
        for (int s = 0; s < loadingDots.toCharArray().length; s++) {
            final int i = s;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txvLoading.setText("[" + txvLoading.getText().toString()
                            .replace("[", "").replace("]", "") +
                            String.valueOf(loadingDots.toCharArray()[i]) + "]");
                    if (i == loadingDots.toCharArray().length - 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    splashDialog.dismiss();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }, splashDelay2);
                    }
                }
            }, s * splashDelay1);
        }
    }
}
