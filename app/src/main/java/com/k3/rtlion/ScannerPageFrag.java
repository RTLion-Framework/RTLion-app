package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ScannerPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;
    private JSInterface jsInterface;
    private String hostAddr;
    private JSONObject cliArgs;
    private int minSens = 1,
                maxSens = 10,
                sensStep = 1,
                defaultSensivity = 2,
                currentSensivity = 2,
                centerFreq, minFreq, maxFreq;

    private TextView txvScannerWarning, txvScanSensivity;
    private LinearLayout llScanner;
    private SeekBar sbScanSensivity;
    private EditText edtxFreqMin, edtxFreqMax;
    private Button btnStartScan;

    public ScannerPageFrag(Activity activity, ViewGroup viewGroup, JSInterface jsInterface){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
        this.jsInterface = jsInterface;
    }

    private void initViews(){
        txvScannerWarning = viewGroup.findViewById(R.id.txvScannerWarning);
        llScanner = viewGroup.findViewById(R.id.llScanner);
        sbScanSensivity = viewGroup.findViewById(R.id.sbScanSensivity);
        txvScanSensivity = viewGroup.findViewById(R.id.txvScanSensivity);
        edtxFreqMin = viewGroup.findViewById(R.id.edtxFreqMin);
        edtxFreqMax = viewGroup.findViewById(R.id.edtxFreqMax);
        btnStartScan = viewGroup.findViewById(R.id.btnStartScan);
    }
    private void initSeekBar(){
        sbScanSensivity.setOnSeekBarChangeListener(new sbScanSensivity_onChange());
        sbScanSensivity.setMax((maxSens - minSens) / sensStep);
        sbScanSensivity.setProgress(defaultSensivity);
    }
    public void initialize(){
        initViews();
        initSeekBar();
        txvScannerWarning.setVisibility(View.VISIBLE);
        llScanner.setVisibility(View.GONE);
        btnStartScan.setOnClickListener(new btnStartScan_onClick());
    }
    public void removeConWarning(){
        txvScannerWarning.setVisibility(View.GONE);
        llScanner.setVisibility(View.VISIBLE);
    }
    public void setHostAddr(String hostAddr){
        this.hostAddr = hostAddr;
    }
    public void setCliArgs(JSONObject cliArgs){
        try {
            if(cliArgs == null)
                throw new JSONException(context.getString(R.string.invalid_args));
            this.cliArgs = cliArgs;
            centerFreq = Integer.valueOf(cliArgs.getString("freq"));
            edtxFreqMin.setText(String.valueOf(centerFreq - (centerFreq/5)));
            edtxFreqMax.setText(String.valueOf(centerFreq + (centerFreq/5)));
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(activity, context.getString(R.string.invalid_server_settings),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkRange(){
        try {
            minFreq = Integer.parseInt(edtxFreqMin.getText().toString());
            maxFreq = Integer.parseInt(edtxFreqMax.getText().toString());
            if (maxFreq > minFreq)
                return true;
        }catch (Exception e){
            return false;
        }
        return false;
    }
    private class sbScanSensivity_onChange implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            currentSensivity = minSens + (progress * sensStep);
            txvScanSensivity.setText(String.valueOf(currentSensivity));
        }
    }
    private class btnStartScan_onClick implements Button.OnClickListener{
        @Override
        public void onClick(View v) {
            if(checkRange()){
                setDevFrequency(minFreq);
            }else{
                Toast.makeText(activity, context.getString(R.string.invalid_settings),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void setDevFrequency(int freq){
        try {
            if (cliArgs == null)
                throw new JSONException(context.getString(R.string.invalid_settings));
            cliArgs.put("freq", freq);
            jsInterface.setServerArgs(hostAddr, cliArgs.toString(),
                    new JSInterface.JSOutputInterface() {
                        @Override
                        public void onInfo(JSONObject clientInfo) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    createGraph();
                                }
                            });
                        }

                        @Override
                        public void onArgs(JSONObject cliArgs) {
                        }

                        @Override
                        public void onConsoleMsg(ConsoleMessage msg) {
                        }

                        @Override
                        public void onData(String data) {
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(activity, context.getString(R.string.settings_save_error),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void createGraph(){

    }
}
