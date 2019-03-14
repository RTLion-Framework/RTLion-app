package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;

public class WebInterface {

    private Activity activity;
    private Context context;
    private WebView webView;

    public WebInterface(Activity activity, WebView webView){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.webView = webView;
    }
    private WebView getWebView(){
        return webView;
    }
}
