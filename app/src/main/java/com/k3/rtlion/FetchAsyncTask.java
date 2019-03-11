package com.k3.rtlion;

import android.content.Context;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchAsyncTask extends AsyncTask<Void, Void, String> {
    public interface AsyncResponse {
        void onProcessFinish(String output);
    }
    private AsyncResponse delegate = null;
    private URL url;
    private Context context;
    private Boolean isValid = true;
    private HttpURLConnection httpURLConnection;
    private InputStream in;
    private BufferedReader reader;
    private StringBuilder source;
    public FetchAsyncTask(Context _context, String _url, AsyncResponse delegate){
        this.delegate = delegate;
        this.context = _context;
        try {
            this.url = new URL(_url);
        }catch (MalformedURLException ex){
            isValid = false;
        }
    }
    @Override
    protected String doInBackground(Void... params) {
        if(!isValid){
            return "";
        }
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            in = httpURLConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            source = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
            return source.toString();
        }
    }
    @Override
    protected void onPostExecute(String result) {
        delegate.onProcessFinish(result);
    }
}