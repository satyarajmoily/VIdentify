package com.howaboutthis.satyaraj.videntify;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ElementTone;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class ToneAnalyzerLoader extends AsyncTaskLoader<ElementTone> {

    private String textToAnalyze;

    ToneAnalyzerLoader(Context context, String textToAnalyze) {
        super(context);
        this.textToAnalyze = textToAnalyze;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ElementTone loadInBackground() {

        try {

            HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            httpURLConnection.setRequestProperty("User-Agent", "Test");
            httpURLConnection.setRequestProperty("Connection", "close");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Please check your Internet Connection", Toast.LENGTH_LONG).show();
            return null;
        }

        String data =
                "{\"textToAnalyze\": \"" + textToAnalyze + "\"," +
                        " \"username\"     : \"a42e9899-f62a-4c1d-abac-e2acc8adfbc2\"," +
                        " \"password\"     : \"i24s2VHuFxXZ\"," +
                        " \"endpoint\"     : \"https://gateway.watsonplatform.net/tone-analyzer/api\"," +
                        " \"skip_authentication\": \"true\"}";

        ToneAnalysis result;
        try {
            JsonParser parser = new JsonParser();
            JsonObject args = parser.parse(data).getAsJsonObject();

            ToneAnalyzer service = new ToneAnalyzer("2016-05-19");
            service.setUsernameAndPassword(args.get("username").getAsString(),
                    args.get("password").getAsString());

            if (args.get("endpoint") != null)
                service.setEndPoint(args.get("endpoint").getAsString());

            if (args.get("skip_authentication") != null)
                service.setSkipAuthentication(Objects.equals(args.get("skip_authentication")
                        .getAsString(), "true"));

            result =
                    service.getTone(args.get("textToAnalyze").getAsString(), null).
                            execute();
        }catch (Exception e){
            Toast.makeText(getContext(),"Please try again in sometime.",Toast.LENGTH_LONG).show();
            return null;
        }

        if (result!=null)
            return result.getDocumentTone();
        else
            return null;

    }
}