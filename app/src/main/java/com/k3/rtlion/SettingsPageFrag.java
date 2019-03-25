package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;
    private JSInterface jsInterface;
    private String hostAddr;
    private JSONObject cliArgs;

    private TextView txvSettingsWarning, txvSettingsInfo;
    private LinearLayout llSettings;
    private EditText edtxDevIndex, edtxSampRate, edtxDevGain;
    private Button btnSaveSettings;

    public SettingsPageFrag(Activity activity, ViewGroup viewGroup, JSInterface jsInterface){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
        this.jsInterface = jsInterface;
    }

    private void initViews(){
        txvSettingsWarning = viewGroup.findViewById(R.id.txvSettingsWarning);
        txvSettingsInfo = viewGroup.findViewById(R.id.txvSettingsInfo);
        llSettings = viewGroup.findViewById(R.id.llSettings);
        edtxDevIndex = viewGroup.findViewById(R.id.edtxDevIndex);
        edtxSampRate = viewGroup.findViewById(R.id.edtxSampRate);
        edtxDevGain = viewGroup.findViewById(R.id.edtxDevGain);
        btnSaveSettings = viewGroup.findViewById(R.id.btnSaveSettings);
    }
    public void initialize(){
        initViews();
        txvSettingsWarning.setVisibility(View.VISIBLE);
        llSettings.setVisibility(View.GONE);
        btnSaveSettings.setEnabled(false);
        btnSaveSettings.setOnClickListener(new btnSaveSettings_onClick());
    }
    public void removeConWarning(){
        txvSettingsWarning.setVisibility(View.GONE);
        llSettings.setVisibility(View.VISIBLE);
    }
    public void setHostAddr(String hostAddr){
        this.hostAddr = hostAddr;
        getArgsFromServer();
    }
    private void getArgsFromServer(){
        jsInterface.getServerArgs(hostAddr, new JSInterface.JSOutputInterface() {
            private void edtx_setText(final EditText editText, final String text){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editText.setText(text);
                    }
                });
            }
            private void enable_btnSaveSettings(){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnSaveSettings.setEnabled(true);
                    }
                });
            }
            @Override
            public void onArgs(JSONObject cliArgs) {
                try {
                    SettingsPageFrag.this.cliArgs = cliArgs;
                    if(cliArgs == null)
                        throw new JSONException("Invalid command-line arguments.");
                    for (int i = 0; i < cliArgs.length(); i++) {
                        switch (cliArgs.names().getString(i)){
                            case "dev":
                                edtx_setText(edtxDevIndex, cliArgs.getString(
                                        cliArgs.names().getString(i)));
                                break;
                            case "samprate":
                                edtx_setText(edtxSampRate, cliArgs.getString(
                                        cliArgs.names().getString(i)));
                                break;
                            case "gain":
                                edtx_setText(edtxDevGain, cliArgs.getString(
                                        cliArgs.names().getString(i)));
                                break;
                            default:
                                break;
                        }
                    }
                enable_btnSaveSettings();
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(activity, context.getString(R.string.invalid_args),
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onInfo(JSONObject clientInfo) { }
            @Override
            public void onConsoleMsg(ConsoleMessage msg) {

            }
        });
    }
    private class btnSaveSettings_onClick implements Button.OnClickListener{
        @Override
        public void onClick(View v) {
            try {
                if (cliArgs == null)
                    throw new JSONException("Invalid settings.");
                cliArgs.put("dev", edtxDevIndex.getText().toString());
                cliArgs.put("samprate", edtxSampRate.getText().toString());
                cliArgs.put("gain", edtxDevGain.getText().toString());
                jsInterface.setServerArgs(hostAddr, cliArgs.toString().replace("\"", "\\\""),
                        new JSInterface.JSOutputInterface() {
                    @Override
                    public void onInfo(JSONObject clientInfo) { }
                    @Override
                    public void onArgs(JSONObject cliArgs) { }
                    @Override

                    public void onConsoleMsg(ConsoleMessage msg) { }
                });
            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(activity, context.getString(R.string.settings_save_error),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
