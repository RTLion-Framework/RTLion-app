package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class JSInterface {

    private Activity activity;
    private Context context;
    private WebView webView;
    private String jsInterfaceName = "JSInterface";

    public JSInterface(Activity activity, WebView webView){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.webView = webView;
    }
    public void fetchPage(String url){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, jsInterfaceName);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                String js_getClientInfo = "javascript:JSInterface.onClientInfo(getClientInfo());";
                webView.loadUrl(js_getClientInfo);
            }
        });
    }
    @android.webkit.JavascriptInterface
    public void onClientInfo(String info){
        Toast.makeText(activity, info, Toast.LENGTH_SHORT).show();
    }
}
