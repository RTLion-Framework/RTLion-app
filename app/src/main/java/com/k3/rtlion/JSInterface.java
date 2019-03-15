package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class JSInterface {

    private Activity activity;
    private Context context;
    private WebView webView;
    private String jsInterfaceName;
    private Object[] globalParams;
    private JSONObject clientInfo;
    private String[] infoValues;

    private enum JSCommands {
        ServerInfo("fetchServerInfo", "getClientInfo");
        private String serverCmd, clientCmd;
        JSCommands(String serverCmd, String clientCmd) {
            this.serverCmd = serverCmd;
            this.clientCmd = clientCmd;
        }
        public String getServerCmd() { return serverCmd; }
        public String getClientCmd() { return clientCmd; }
    }
    public interface ClientInfoInterface{
        public void onInfo(String[] infoValues);
    }
    private ClientInfoInterface clientInfoInterface;
    public JSInterface(Activity activity){
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }
    public void initialize(WebView webView){
        this.webView = webView;
        webView.getSettings().setJavaScriptEnabled(true);
        jsInterfaceName = this.getClass().getSimpleName();
        webView.addJavascriptInterface(this, jsInterfaceName);
        webView.setWebViewClient(new webView_client());
    }
    private class webView_client extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            String jsCommand = createJSCommand(JSCommands.valueOf(url.split("#")[1]).ordinal(),
                    globalParams);
            webView.loadUrl(jsCommand);
        }
    }
    private String createJSCommand(int index, Object[] params){
        StringBuilder jsCommand = new StringBuilder();
        String jsHead = "javascript:",
                serverCmd = JSCommands.values()[index].getServerCmd(),
                clientCmd = JSCommands.values()[index].getClientCmd();
        jsCommand.append(jsHead);
        jsCommand.append(jsInterfaceName);
        jsCommand.append(".");
        jsCommand.append(serverCmd);
        jsCommand.append("(");
        jsCommand.append(clientCmd);
        jsCommand.append("(");
        if (params != null){
            for (int i = 0; i < params.length; i++){
                jsCommand.append(String.valueOf(params[i]));
                if (i != params.length - 1)
                    jsCommand.append(",");
            }
        }
        jsCommand.append("));");
        return jsCommand.toString();
    }
    public void getServerInfo(String url, ClientInfoInterface clientInfoInterface){
        this.clientInfoInterface = clientInfoInterface;
        webView.loadUrl(url + "#" + JSCommands.ServerInfo.name());
        globalParams = null;
    }
    @JavascriptInterface
    public void fetchServerInfo(String info){
        try {
            clientInfo = new JSONObject(info);
            infoValues = new String[clientInfo.length()];
            for (int i = 0; i < clientInfo.length(); i++) {
                infoValues[i] = clientInfo.getString(clientInfo.names().getString(i));
                Toast.makeText(activity, clientInfo.names().getString(i) + " : " +
                        clientInfo.getString(clientInfo.names().getString(i)),
                        Toast.LENGTH_SHORT).show();
            }
            clientInfoInterface.onInfo(infoValues);
        }catch (JSONException e){
            e.printStackTrace();
            clientInfoInterface.onInfo(infoValues);
        }
    }
}
