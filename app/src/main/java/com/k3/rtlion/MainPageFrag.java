package com.k3.rtlion;


import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MainPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;
    private JSInterface jsInterface;
    private RelativeLayout rlMainFrag;
    private TextView txvServerStatus;
    private TextInputLayout tilHostAddr;
    private EditText edtxHostAddr;
    private Button btnConnect;
    private String serverHost,
            appNamespace = "/app";
    private int portNum;

    public MainPageFrag(Activity activity, ViewGroup viewGroup, JSInterface jsInterface){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
        this.jsInterface = jsInterface;
    }

    private void initViews(){
        rlMainFrag = (RelativeLayout) viewGroup.findViewById(R.id.rlMainFrag);
        txvServerStatus = (TextView) viewGroup.findViewById(R.id.txvServerStatus);
        tilHostAddr = (TextInputLayout) viewGroup.findViewById(R.id.tilHostAddr);
        edtxHostAddr = (EditText) viewGroup.findViewById(R.id.edtxHostAddr);
        btnConnect = (Button) viewGroup.findViewById(R.id.btnConnect);
    }
    public void initialize(){
        initViews();
        edtxHostAddr.setOnEditorActionListener(new edtxHostAddr_onEditorAction());
        btnConnect.setOnClickListener(new btnConnect_onClick());
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
    private void hideKeyboard(){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    context.getSystemService(context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(rlMainFrag.getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private Boolean checkHostAddr(String hostAddr){
        Boolean validAddr = false;
        try{
            if (!hostAddr.isEmpty() && hostAddr.contains(":")) {
                portNum = Integer.parseInt(hostAddr.split(":")[hostAddr.split(":").length-1]);
                serverHost = hostAddr.replace(":"+String.valueOf(portNum), "");
                if (serverHost.length() > 4 && portNum > 0)
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
        String url = edtxHostAddr.getText().toString();
        if(checkHostAddr(url)){
            hideKeyboard();
            enableViews(false);
            txvServerStatus.setText(context.getString(R.string.server_connecting));
            jsInterface.getServerInfo(url + appNamespace, new JSInterface.JSOutputInterface() {
                private void setTxvServerStatus(final String text){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txvServerStatus.setText(text);
                        }
                    });
                }
                @Override
                public void onInfo(JSONObject clientInfo) {
                    try {
                        for (int i = 0; i < clientInfo.length(); i++) {
                            Toast.makeText(activity, clientInfo.getString(clientInfo.names().
                                    getString(i)), Toast.LENGTH_SHORT).show();
                        }
                        setTxvServerStatus(context.getString(R.string.server_connected));
                    }catch (JSONException e){
                        e.printStackTrace();
                        setTxvServerStatus(context.getString(R.string.server_disconnected));
                    }
                    enableViews(true);
                }
            });
        }else{
            Toast.makeText(activity, context.getString(R.string.invalid_host),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
