package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
                centerFreq, minFreq, maxFreq,
                stepSize;
    private boolean viewsHidden = false;
    private ArrayList<Double> freqRes, dbRes;

    private TextView txvScannerWarning, txvScanSensivity,
            txvScanSensivityLabel, txvFreqRange;
    private LinearLayout llScanner;
    private RelativeLayout rlScanSensivity;
    private SeekBar sbScanSensivity;
    private TextInputLayout tilFreqMin, tilFreqMax;
    private EditText edtxFreqMin, edtxFreqMax;
    private Button btnStartScan;
    private PhotoView imgFreqScan;
    private ListView lstScanResults;

    public ScannerPageFrag(Activity activity, ViewGroup viewGroup, JSInterface jsInterface){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
        this.jsInterface = jsInterface;
    }

    private void initViews(){
        txvScannerWarning = viewGroup.findViewById(R.id.txvScannerWarning);
        llScanner = viewGroup.findViewById(R.id.llScanner);
        rlScanSensivity = viewGroup.findViewById(R.id.rlScanSensivity);
        sbScanSensivity = viewGroup.findViewById(R.id.sbScanSensivity);
        txvScanSensivity = viewGroup.findViewById(R.id.txvScanSensivity);
        txvScanSensivityLabel = viewGroup.findViewById(R.id.txvScanSensivityLabel);
        tilFreqMin = viewGroup.findViewById(R.id.tilFreqMin);
        tilFreqMax = viewGroup.findViewById(R.id.tilFreqMax);
        edtxFreqMin = viewGroup.findViewById(R.id.edtxFreqMin);
        edtxFreqMax = viewGroup.findViewById(R.id.edtxFreqMax);
        btnStartScan = viewGroup.findViewById(R.id.btnStartScan);
        imgFreqScan = viewGroup.findViewById(R.id.imgFreqScan);
        txvFreqRange = viewGroup.findViewById(R.id.txvFreqRange);
        lstScanResults = viewGroup.findViewById(R.id.lstScanResults);
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
        freqRes = new ArrayList<>();
        dbRes = new ArrayList<>();
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
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    edtxFreqMin.setText(String.valueOf(centerFreq - (centerFreq/5)));
                    edtxFreqMax.setText(String.valueOf(centerFreq + (centerFreq/5)));
                }
            });
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
    private void hideViews(boolean state){
        if (state){
            edtxFreqMin.setVisibility(View.GONE);
            edtxFreqMax.setVisibility(View.GONE);
            tilFreqMin.setVisibility(View.GONE);
            tilFreqMax.setVisibility(View.GONE);
            rlScanSensivity.setVisibility(View.GONE);
            txvScanSensivityLabel.setVisibility(View.GONE);
            txvFreqRange.setVisibility(View.VISIBLE);
            lstScanResults.setVisibility(View.VISIBLE);
            btnStartScan.setText(context.getString(R.string.stop_graph));
            viewsHidden = true;
        }else{
            edtxFreqMin.setVisibility(View.VISIBLE);
            edtxFreqMax.setVisibility(View.VISIBLE);
            tilFreqMin.setVisibility(View.VISIBLE);
            tilFreqMax.setVisibility(View.VISIBLE);
            rlScanSensivity.setVisibility(View.VISIBLE);
            txvScanSensivityLabel.setVisibility(View.VISIBLE);
            btnStartScan.setText(context.getString(R.string.start_scan));
            viewsHidden = false;
        }
    }
    private void enableViews(boolean state) {
        edtxFreqMin.setEnabled(state);
        edtxFreqMax.setEnabled(state);
        btnStartScan.setEnabled(true);
    }
    private class btnStartScan_onClick implements Button.OnClickListener{
        @Override
        public void onClick(View v) {
            if (viewsHidden) {
                centerFreq = maxFreq;
            }else{
                if (checkRange()) {
                    freqRes.clear();
                    dbRes.clear();
                    stepSize = 2 * (int) Math.pow(10, (int) Math.log10(maxFreq - minFreq) - 1);
                    enableViews(false);
                    btnStartScan.setText(context.getString(R.string.graph_wait));
                    btnStartScan.setEnabled(false);
                    setDevFrequency(minFreq);
                } else {
                    Toast.makeText(activity, context.getString(R.string.invalid_settings),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void setDevFrequency(int freq){
        try {
            if (cliArgs == null)
                throw new JSONException(context.getString(R.string.invalid_settings));
            centerFreq = freq;
            cliArgs.put("freq", centerFreq);
            jsInterface.setServerArgs(hostAddr, cliArgs.toString(),
                    new JSInterface.JSOutputInterface() {
                        @Override
                        public void onInfo(JSONObject clientInfo) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    scanFrequencyRange();
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
            enableViews(true);
        }
    }
    private void scanFrequencyRange(){
        jsInterface.getScannedValues(hostAddr, new JSInterface.JSOutputInterface() {
            private void setGraphImage(final String data){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap fftBitmap = new ImageBase64().getImage(data.split("[|]")[0]);
                        if(fftBitmap == null){
                            Toast.makeText(activity, context.getString(R.string.graph_error),
                                    Toast.LENGTH_SHORT).show();
                            hideViews(false);
                            enableViews(true);
                        }else{
                            imgFreqScan.setImageBitmap(Bitmap.createScaledBitmap(
                                    fftBitmap,
                                    fftBitmap.getWidth()*2,
                                    fftBitmap.getHeight()*2, false));
                            if(!viewsHidden) {
                                hideViews(true);
                                btnStartScan.setEnabled(true);
                            }
                            if(centerFreq < maxFreq){
                                setDevFrequency(centerFreq + stepSize);
                            }else{
                                hideViews(false);
                                enableViews(true);
                            }
                        }
                    }
                });
            }
            @Override
            public void onInfo(JSONObject clientInfo) { }

            @Override
            public void onArgs(JSONObject cliArgs) { }

            @Override
            public void onConsoleMsg(ConsoleMessage msg) { }

            @Override
            public void onData(String data) {
                setGraphImage(data);
            }
        });
    }
}
