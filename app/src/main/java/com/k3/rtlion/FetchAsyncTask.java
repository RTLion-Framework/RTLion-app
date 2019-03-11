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
        void onFetch(String source);
    }
    private AsyncResponse asyncResponse;
    private URL url;
    private Boolean isValidURL = true;
    private HttpURLConnection httpURLConnection;
    private InputStream in;
    private BufferedReader reader;
    private StringBuilder source;
    public FetchAsyncTask(String url, AsyncResponse asyncResponse){
        this.asyncResponse = asyncResponse;
        try {
            this.url = new URL(url);
        }catch (MalformedURLException ex){
            isValidURL = false;
        }
    }
    @Override
    protected String doInBackground(Void... params) {
        if(!isValidURL){
            return null;
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
            return null;
        }finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
            return source.toString();
        }
    }
    @Override
    protected void onPostExecute(String source) {
        asyncResponse.onFetch(source);
    }
}