package com.howaboutthis.satyaraj.videntify;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestInternetLoader extends AsyncTaskLoader {

    TestInternetLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection)(new URL("http://www.google.com").openConnection());
            httpURLConnection.setRequestProperty("User-Agent", "Test");
            httpURLConnection.setRequestProperty("Connection", "close");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.connect();
            return (httpURLConnection.getResponseCode() == 200);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
