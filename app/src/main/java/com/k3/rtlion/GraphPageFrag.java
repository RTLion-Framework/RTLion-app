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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private int centerFreq;

    private TextView txvGraphWarning;
    private LinearLayout llGraph;
    private ImageView imgFFTGraph;
    private Button btnFFTGraph;
    private EditText edtxFreq, edtxNumRead, edtxInterval;
    private TextInputLayout tilFreq, tilNumRead, tilInterval;

    public GraphPageFrag(Activity activity, ViewGroup viewGroup, JSInterface jsInterface){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.viewGroup = viewGroup;
        this.jsInterface = jsInterface;
    }
    private void initViews(){
        txvGraphWarning = viewGroup.findViewById(R.id.txvGraphWarning);
        llGraph = viewGroup.findViewById(R.id.llGraph);
        imgFFTGraph = viewGroup.findViewById(R.id.imgFFTGraph);
        btnFFTGraph = viewGroup.findViewById(R.id.btnFFTGraph);
        edtxFreq = viewGroup.findViewById(R.id.edtxFreq);
        edtxNumRead = viewGroup.findViewById(R.id.edtxNumRead);
        edtxInterval = viewGroup.findViewById(R.id.edtxInterval);
        tilFreq = viewGroup.findViewById(R.id.tilFreq);
        tilNumRead = viewGroup.findViewById(R.id.tilNumRead);
        tilInterval = viewGroup.findViewById(R.id.tilInterval);
    }
    public void initialize(){
        initViews();
        txvGraphWarning.setVisibility(View.VISIBLE);
        llGraph.setVisibility(View.GONE);
        btnFFTGraph.setOnClickListener(new btnFFTGraph_onClick());
    }
    public void removeConWarning(){
        txvGraphWarning.setVisibility(View.GONE);
        llGraph.setVisibility(View.VISIBLE);
    }
    public void setHostAddr(String hostAddr){
        this.hostAddr = hostAddr;
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
                            centerFreq = Integer.valueOf(cliArgs.getString(cliArgs.names().getString(i)));
                            edtxFreq.setText(String.valueOf(centerFreq));
                        }catch (Exception e){
                            edtxFreq.setText("");
                        }
                        break;
                    case "n":
                        edtxNumRead.setText(cliArgs.getString(cliArgs.names().getString(i)));
                        break;
                    case "i":
                        edtxInterval.setText(cliArgs.getString(cliArgs.names().getString(i)));
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
    private void hideViews(boolean state){
        if (state){
            edtxFreq.setVisibility(View.GONE);
            edtxNumRead.setVisibility(View.GONE);
            edtxInterval.setVisibility(View.GONE);
            tilFreq.setVisibility(View.GONE);
            tilNumRead.setVisibility(View.GONE);
            tilInterval.setVisibility(View.GONE);
        }else{
            edtxFreq.setVisibility(View.VISIBLE);
            edtxNumRead.setVisibility(View.VISIBLE);
            edtxInterval.setVisibility(View.VISIBLE);
            tilFreq.setVisibility(View.VISIBLE);
            tilNumRead.setVisibility(View.VISIBLE);
            tilInterval.setVisibility(View.VISIBLE);
        }
    }
    private void enableViews(boolean state) {
        edtxFreq.setEnabled(state);
        edtxNumRead.setEnabled(state);
        edtxInterval.setEnabled(state);
    }
    private class btnFFTGraph_onClick implements Button.OnClickListener{
        @Override
        public void onClick(View v) {
            enableViews(false);
            if(checkFreq()) {
                try {
                    if (cliArgs == null)
                        throw new JSONException(context.getString(R.string.invalid_settings));
                    cliArgs.put("freq", edtxFreq.getText().toString());
                    cliArgs.put("n", edtxNumRead.getText().toString());
                    cliArgs.put("i", edtxInterval.getText().toString());
                    jsInterface.setServerArgs(hostAddr, cliArgs.toString(), new JSInterface.JSOutputInterface() {
                        @Override
                        public void onInfo(JSONObject clientInfo) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    checkGraphSettings();
                                }
                            });
                        }
                        @Override
                        public void onArgs(JSONObject cliArgs) { }

                        @Override
                        public void onConsoleMsg(ConsoleMessage msg) { }

                        @Override
                        public void onData(String data) { }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(activity, context.getString(R.string.settings_save_error),
                            Toast.LENGTH_SHORT).show();
                    enableViews(true);
                }
            }else{
                Toast.makeText(activity, context.getString(R.string.invalid_freq),
                        Toast.LENGTH_SHORT).show();
                edtxFreq.setText("");
                enableViews(true);
            }
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
    private void checkGraphSettings(){
        jsInterface.getServerArgs(hostAddr, new JSInterface.JSOutputInterface() {
            private void edtx_setText(final EditText editText, final String text){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editText.setText(text);
                    }
                });
            }
            @Override
            public void onArgs(JSONObject cliArgs) {
                try {
                    if(cliArgs == null)
                        throw new JSONException(context.getString(R.string.invalid_args));
                    GraphPageFrag.this.cliArgs = cliArgs;
                    if(cliArgs.getString("freq").equals(String.valueOf(centerFreq))){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createGraph();
                                hideViews(true);
                            }
                        });
                    }else{
                        edtx_setText(edtxFreq, "");
                        Toast.makeText(activity, context.getString(R.string.save_error),
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(activity, context.getString(R.string.invalid_server_settings),
                            Toast.LENGTH_SHORT).show();
                    enableViews(true);
                }
            }
            @Override
            public void onInfo(JSONObject clientInfo) { }

            @Override
            public void onConsoleMsg(ConsoleMessage msg) { }

            @Override
            public void onData(String data) { }
        });
    }
    private void createGraph(){
        jsInterface.getGraphFFT(hostAddr, new JSInterface.JSOutputInterface() {
            private void setGraphImage(final String data){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap fftBitmap = new ImageBase64().getImage(data);
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
