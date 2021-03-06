package com.k3.rtlion.fragment;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.k3.rtlion.handler.HostDB;
import com.k3.rtlion.handler.JSInterface;
import com.k3.rtlion.R;
import com.k3.rtlion.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("FieldCanBeLocal")
public class MainPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;
    private JSInterface jsInterface;
    private HostDB hostDB;
    private String serverUrl,
            serverHost,
            appNamespace = "/app";
    private String[] infoNames;
    private int portNum, refreshDuration = 800;
    public boolean connectionStatus = false;

    private RelativeLayout rlMainFrag;
    private SwipeRefreshLayout swpMain;
    private TextView txvServerStatus, txvServerInfo;
    private EditText edtxHostAddr;
    private Button btnConnect;

    public MainPageFrag(Activity activity, ViewGroup viewGroup, JSInterface jsInterface){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
        this.jsInterface = jsInterface;
    }

    private void initViews(){
        rlMainFrag = viewGroup.findViewById(R.id.rlMainFrag);
        swpMain = viewGroup.findViewById(R.id.swpMain);
        txvServerStatus = viewGroup.findViewById(R.id.txvServerStatus);
        edtxHostAddr = viewGroup.findViewById(R.id.edtxHostAddr);
        btnConnect = viewGroup.findViewById(R.id.btnConnect);
        txvServerInfo = viewGroup.findViewById(R.id.txvServerInfo);
    }
    public void initialize() {
        initViews();
        initDatabase();
        infoNames = context.getResources().getStringArray(R.array.info_names);
        edtxHostAddr.setOnEditorActionListener(new edtxHostAddr_onEditorAction());
        btnConnect.setOnClickListener(new btnConnect_onClick());
        txvServerInfo.setMovementMethod(new ScrollingMovementMethod());
        swpMain.setOnRefreshListener(new swpMain_onRefresh());
    }
    public boolean getConnectionStatus(){
        return connectionStatus;
    }
    public String getHostAddr(){
        return serverUrl + appNamespace;
    }
    private void initDatabase(){
        try {
            hostDB = new HostDB(context);
            if (hostDB.getHostAddr() != null)
                edtxHostAddr.setText(hostDB.getHostAddr());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private class edtxHostAddr_onEditorAction implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEND) {
                tryConnect();
                return true;
            }
            return false;
        }
    }
    private class btnConnect_onClick implements Button.OnClickListener{
        @Override
        public void onClick(View v) {
            tryConnect();
        }
    }
    private class swpMain_onRefresh implements SwipeRefreshLayout.OnRefreshListener{
        @Override
        public void onRefresh() {
            Toast.makeText(activity, context.getString(R.string.app_restart),
                    Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent mStartActivity = new Intent(context, MainActivity.class);
                        int mPendingIntentId = 99;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(context,
                                mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.
                                ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100,
                                mPendingIntent);
                        System.exit(0);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, refreshDuration);
        }
    }
    @SuppressWarnings("ConstantConditions")
    private void hideKeyboard(){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    context.getSystemService(context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(rlMainFrag.getWindowToken(), 0);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    private Boolean checkHostAddr(String hostAddr){
        boolean validAddr = false;
        try{
            if (!hostAddr.isEmpty() && hostAddr.contains(":") && hostAddr.contains("http")) {
                portNum = Integer.parseInt(hostAddr.split(":")[hostAddr.split(":").length-1]);
                serverHost = hostAddr.replace(":"+String.valueOf(portNum), "");
                if (serverHost.length() > 12 && portNum > 0)
                    validAddr = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return validAddr;
    }
    private void enableViews(final Boolean state){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                edtxHostAddr.setEnabled(state);
                btnConnect.setEnabled(state);
            }
        });
    }
    private void tryConnect(){
        serverUrl = edtxHostAddr.getText().toString();
        if(checkHostAddr(serverUrl)){
            hideKeyboard();
            enableViews(false);
            txvServerStatus.setText(context.getString(R.string.server_connecting));
            jsInterface.getServerInfo(serverUrl + appNamespace, new JSInterface.JSOutputInterface() {
                private void setTxvServerStatus(final String text){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txvServerStatus.setText(text);
                        }
                    });
                }
                private void setTxvServerInfo(final String text){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txvServerInfo.setText(text);
                        }
                    });
                }
                @Override
                public void onInfo(JSONObject clientInfo) {
                    try {
                        if(clientInfo.length() != 6)
                            throw new JSONException("Invalid client information.");
                        String clientInfos = "";
                        for (int i = 0; i < clientInfo.length(); i++) {
                            clientInfos += infoNames[i] + ": " + clientInfo.getString(
                                    clientInfo.names().getString(i)) + "\n\n";
                        }
                        clientInfos += context.getString(R.string.swipe_text)+ "\n\n";
                        setTxvServerStatus(context.getString(R.string.server_connected));
                        setTxvServerInfo(clientInfos);
                        hostDB.updateHostAddr(serverUrl);
                        connectionStatus = true;
                    }catch (JSONException e){
                        e.printStackTrace();
                        setTxvServerStatus(context.getString(R.string.server_disconnected));
                        enableViews(true);
                    }
                }
                @Override
                public void onConsoleMsg(ConsoleMessage msg) {
                    String notDefinedError = "Uncaught ReferenceError: " + 
                            JSInterface.JSCommands.ServerInfo.getClientCmd() + " is not defined";
                    if (msg.message().trim().equals(notDefinedError)){
                        Toast.makeText(activity, context.getString(R.string.server_unreachable) +
                                " [" + serverUrl + appNamespace + "]", Toast.LENGTH_SHORT).show();
                    }
                    setTxvServerStatus(context.getString(R.string.server_disconnected));
                    enableViews(true);
                }

                @Override
                public void onArgs(JSONObject cliArgs) { }

                @Override
                public void onData(String data) { }
            });
        }else{
            Toast.makeText(activity, context.getString(R.string.invalid_host),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
