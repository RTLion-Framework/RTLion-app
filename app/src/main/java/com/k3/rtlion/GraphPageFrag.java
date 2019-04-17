package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class GraphPageFrag {

    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;
    private JSInterface jsInterface;
    private String hostAddr;
    private JSONObject cliArgs;
    private int centerFreq, numRead, maxRead, readInterval,
            minFreq, maxFreq;
    private static int refreshDuration = 800;
    private double freqShift = 20*(Math.pow(10, 6)),
            stepSize = Math.pow(10, 6)/5;
    public boolean viewsHidden = false;
    private boolean contRead = true,
            freqChanged = false;
    private Bitmap fftBitmap;
    private Object[] uiObjects;

    private TextView txvGraphWarning, txvFreqVal, txvGraphPerc;
    private LinearLayout llGraph;
    private RelativeLayout rlFreqChange;
    private SwipeRefreshLayout swpGraph;
    private ImageView imgFFTGraph;
    private Button btnFFTGraph;
    private EditText edtxFreq, edtxNumRead, edtxInterval;
    private TextInputLayout tilFreq, tilNumRead, tilInterval;
    private SeekBar sbCenterFreq;

    public GraphPageFrag(Activity activity, ViewGroup viewGroup, JSInterface jsInterface){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
        this.jsInterface = jsInterface;
    }
    private void initViews(){
        txvGraphWarning = viewGroup.findViewById(R.id.txvGraphWarning);
        llGraph = viewGroup.findViewById(R.id.llGraph);
        swpGraph = viewGroup.findViewById(R.id.swpGraph);
        imgFFTGraph = viewGroup.findViewById(R.id.imgFFTGraph);
        btnFFTGraph = viewGroup.findViewById(R.id.btnFFTGraph);
        edtxFreq = viewGroup.findViewById(R.id.edtxFreq);
        edtxNumRead = viewGroup.findViewById(R.id.edtxNumRead);
        edtxInterval = viewGroup.findViewById(R.id.edtxInterval);
        tilFreq = viewGroup.findViewById(R.id.tilFreq);
        tilNumRead = viewGroup.findViewById(R.id.tilNumRead);
        tilInterval = viewGroup.findViewById(R.id.tilInterval);
        sbCenterFreq = viewGroup.findViewById(R.id.sbCenterFreq);
        rlFreqChange = viewGroup.findViewById(R.id.rlFreqChange);
        txvFreqVal = viewGroup.findViewById(R.id.txvFreqVal);
        txvGraphPerc = viewGroup.findViewById(R.id.txvGraphPerc);
    }
    public void initialize(){
        initViews();
        txvGraphWarning.setVisibility(View.VISIBLE);
        llGraph.setVisibility(View.GONE);
        btnFFTGraph.setOnClickListener(new btnFFTGraph_onClick());
        sbCenterFreq.setOnSeekBarChangeListener(new sbCenterFreq_onChange());
        swpGraph.setOnRefreshListener(new swpGraph_onRefresh());
    }
    public void setUIObjects(Object[] uiObjects){
        txvGraphWarning.setVisibility(View.GONE);
        llGraph.setVisibility(View.VISIBLE);
        this.uiObjects = uiObjects;
        this.hostAddr = ((MainPageFrag) uiObjects[1]).getHostAddr();
    }
    public void setGraphParams(JSONObject cliArgs){
        try {
            if(cliArgs == null)
                throw new JSONException(context.getString(R.string.invalid_args));
            this.cliArgs = cliArgs;
            for (int i = 0; i < cliArgs.length(); i++) {
                switch (cliArgs.names().getString(i)){
                    case "freq":
                        try{
                            centerFreq = Integer.valueOf(cliArgs.getString(cliArgs.names()
                                    .getString(i)));
                            edtx_setText(edtxFreq, String.valueOf(centerFreq));
                        }catch (Exception e){
                            edtx_setText(edtxFreq, "");
                        }
                        break;
                    case "n":
                        try {
                            numRead = Integer.valueOf(cliArgs.getString(cliArgs.names()
                                    .getString(i)));
                            edtx_setText(edtxNumRead, String.valueOf(numRead));
                        }catch (Exception e){
                            edtx_setText(edtxNumRead, "");
                        }
                        break;
                    case "i":
                        try {
                            readInterval = Integer.valueOf(cliArgs.getString(cliArgs.names()
                                    .getString(i)));
                            edtx_setText(edtxInterval, String.valueOf(readInterval));
                        }catch (Exception e){
                            edtx_setText(edtxInterval, "");
                        }
                        break;
                    default:
                        break;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(activity, context.getString(R.string.invalid_server_settings),
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void showGraph(int frequency){
        edtxFreq.setText(String.valueOf(frequency));
        btnFFTGraph.performClick();
    }
    private void hideViews(boolean state){
        if (state){
            edtxFreq.setVisibility(View.GONE);
            edtxNumRead.setVisibility(View.GONE);
            edtxInterval.setVisibility(View.GONE);
            tilFreq.setVisibility(View.GONE);
            tilNumRead.setVisibility(View.GONE);
            tilInterval.setVisibility(View.GONE);
            rlFreqChange.setVisibility(View.VISIBLE);
            btnFFTGraph.setText(context.getString(R.string.stop_graph));
            viewsHidden = true;
        }else{
            edtxFreq.setVisibility(View.VISIBLE);
            edtxNumRead.setVisibility(View.VISIBLE);
            edtxInterval.setVisibility(View.VISIBLE);
            tilFreq.setVisibility(View.VISIBLE);
            tilNumRead.setVisibility(View.VISIBLE);
            tilInterval.setVisibility(View.VISIBLE);
            rlFreqChange.setVisibility(View.GONE);
            btnFFTGraph.setText(context.getString(R.string.create_graph));
            viewsHidden = false;
        }
    }
    private void enableViews(boolean state) {
        edtxFreq.setEnabled(state);
        edtxNumRead.setEnabled(state);
        edtxInterval.setEnabled(state);
        btnFFTGraph.setEnabled(true);
        txvGraphPerc.setText("");
    }
    private class btnFFTGraph_onClick implements Button.OnClickListener{
        @Override
        public void onClick(View v) {
            if(viewsHidden){
                numRead = 0;
                contRead = false;
                freqChanged = false;
            }else if(!((ScannerPageFrag)uiObjects[4]).viewsHidden){
                contRead = true;
                enableViews(false);
                if (checkFreq()) {
                    try {
                        if (cliArgs == null)
                            throw new JSONException(context.getString(R.string.invalid_settings));
                        numRead = Integer.valueOf(edtxNumRead.getText().toString());
                        maxRead = numRead;
                        readInterval = Integer.valueOf(edtxInterval.getText().toString());
                        cliArgs.put("freq", edtxFreq.getText().toString());
                        cliArgs.put("n", edtxNumRead.getText().toString());
                        cliArgs.put("i", edtxInterval.getText().toString());
                        jsInterface.setServerArgs(hostAddr, cliArgs.toString(),
                                new JSInterface.JSOutputInterface() {
                            @Override
                            public void onInfo(JSONObject clientInfo) {
                                configureGraphSettings();
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
                } else {
                    Toast.makeText(activity, context.getString(R.string.invalid_freq),
                            Toast.LENGTH_SHORT).show();
                    edtxFreq.setText("");
                    enableViews(true);
                }
            }else{
                Toast.makeText(activity, context.getString(R.string.framework_busy),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class sbCenterFreq_onChange implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser && viewsHidden) {
                centerFreq = minFreq + (progress * (int) stepSize);
                txvFreqVal.setText(String.valueOf(centerFreq));
                edtxFreq.setText(String.valueOf(centerFreq));
                numRead = 0;
                contRead = false;
                freqChanged = true;
            }
        }
    }
    private class swpGraph_onRefresh implements SwipeRefreshLayout.OnRefreshListener{
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swpGraph.setRefreshing(false);
                    ((SettingsPageFrag)uiObjects[2]).getArgsFromServer();
                }
            }, refreshDuration);
        }
    }
    private boolean checkFreq(){
        boolean valid = false;
        try {
            centerFreq = Integer.parseInt(edtxFreq.getText().toString());
            if (centerFreq > 0)
                valid = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return valid;
    }
    private void edtx_setText(final EditText editText, final String text){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.setText(text);
            }
        });
    }
    private void configureGraphSettings(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!freqChanged) {
                    btnFFTGraph.setText(context.getString(R.string.graph_wait));
                    btnFFTGraph.setEnabled(false);
                    sbCenterFreq.setEnabled(false);
                    maxFreq = centerFreq + (int) freqShift;
                    minFreq = centerFreq - (int) freqShift;
                    txvFreqVal.setText(String.valueOf(centerFreq));
                    sbCenterFreq.setMax((maxFreq - minFreq) / (int) stepSize);
                    sbCenterFreq.setProgress(sbCenterFreq.getMax() / 2);
                }
                createGraph();
            }
        });
    }
    private void createGraph(){
        jsInterface.getGraphFFT(hostAddr, new JSInterface.JSOutputInterface() {
            private void changeProgress(){
                numRead -= 1;
                if(numRead > 0)
                    txvGraphPerc.setText("[%" + String.valueOf(100 -
                            ((numRead * 100) / maxRead)) + "]");
            }
            private void setGraphImage(final String data){
                activity.runOnUiThread(new Runnable() {
                    private void readWithInterval(){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                createGraph();
                            }
                        }, readInterval);
                    }
                    @Override
                    public void run() {
                        fftBitmap = new ImageBase64().getImage(data);
                        if(fftBitmap == null){
                            Toast.makeText(activity, context.getString(R.string.graph_error),
                                    Toast.LENGTH_SHORT).show();
                            enableViews(true);
                            hideViews(false);
                        }else{
                            imgFFTGraph.setImageBitmap(Bitmap.createScaledBitmap(
                                    fftBitmap,
                                    fftBitmap.getWidth()*2,
                                    fftBitmap.getHeight()*2, false));
                            if(!viewsHidden) {
                                hideViews(true);
                                btnFFTGraph.setEnabled(true);
                                sbCenterFreq.setEnabled(true);
                            }
                            changeProgress();
                            if(numRead != 0 && contRead){
                                readWithInterval();
                            }else{
                                if(!freqChanged) {
                                    hideViews(false);
                                    enableViews(true);
                                }else{
                                    viewsHidden = false;
                                    btnFFTGraph.performClick();
                                }
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
