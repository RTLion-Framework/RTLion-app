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

public class FetchAsyncTask extends AsyncTask<String, Void, String> {
    private URL url;
    private Boolean isValidURL = true;
    private HttpURLConnection httpURLConnection;
    private InputStream in;
    private BufferedReader reader;
    private StringBuilder source;
    private String statusCode;
    private static String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; " +
            "rv:1.8.1.13) Gecko/20080311 Firefox/2.0.0.13", acceptLang = "en-US,en;q=0.9",
            acceptEnc = "UTF-8";
    public interface AsyncResponse {
        void onFetch(String source);
    }
    private AsyncResponse asyncResponse;
    public FetchAsyncTask(AsyncResponse asyncResponse){
        this.asyncResponse = asyncResponse;
    }
    @Override
    protected String doInBackground(String... url) {
        try {
            return fetchPage(new URL(url[0]));
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onPostExecute(String connectionResult) {
        asyncResponse.onFetch(connectionResult);
    }
    private String fetchPage(URL url) throws IOException{
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", userAgent);
        httpURLConnection.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpURLConnection.setRequestProperty("Accept-Language", acceptLang);
        httpURLConnection.setRequestProperty("Accept-Encoding", acceptEnc);
        httpURLConnection.setRequestProperty("Connection", "keep-alive");
        statusCode = String.valueOf(httpURLConnection.getResponseCode());
        in = httpURLConnection.getInputStream();
        reader = new BufferedReader(new InputStreamReader(in));
        source = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            source.append(line);
        }
        if (httpURLConnection != null)
            httpURLConnection.disconnect();
        return source.toString();
    }
}