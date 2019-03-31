package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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

    private TextView txvGraphWarning;
    private LinearLayout llGraph;
    private ImageView imgFFTGraph;
    private Button btnFFTGraph;
    private EditText edtxFreq, edtxNumRead, edtxInterval;

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
        getGraphParamsFromServer();
    }
    public void setHostAddr(String hostAddr){
        this.hostAddr = hostAddr;
    }
    private class btnFFTGraph_onClick implements Button.OnClickListener{
        @Override
        public void onClick(View v) {
            createGraph();
        }
    }
    private void getGraphParamsFromServer(){
        jsInterface.getServerArgs(hostAddr, new JSInterface.JSOutputInterface() {
            @Override
            public void onArgs(JSONObject cliArgs) {
                try {
                    if(cliArgs == null)
                        throw new JSONException(context.getString(R.string.invalid_args));
                    GraphPageFrag.this.cliArgs = cliArgs;

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(activity, context.getString(R.string.invalid_server_settings),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onConsoleMsg(ConsoleMessage msg) {

            }
            @Override
            public void onInfo(JSONObject clientInfo) { }
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
