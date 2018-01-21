package com.howaboutthis.satyaraj.videntify;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;

public class detailedLoader extends AsyncTaskLoader<List<Concept>> {

    private String path;
    private int concept;

    detailedLoader(Context context, String path, int concept) {
        super(context);
        this.path = path;
        this.concept = concept;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Concept> loadInBackground() {

        ClarifaiClient client = new ClarifaiBuilder("fe6ea9cd9ee24fed8b602541de3d032f")
                .client(new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .build()
                )
                .buildSync();

        List<ClarifaiOutput<Concept>> predictionResults = null;

        if (concept == 0) {

            try {

                predictionResults =
                        client.getDefaultModels().generalModel() // You can also do client.getModelByID("id") to get your custom models
                                .predict()
                                .withInputs(
                                        ClarifaiInput.forImage(new File(path)))
                                .executeSync()
                                .get();
            } catch (Exception e) {
                Log.d("Loader", "RequestFailed");
            }

        } else if (concept == 1) {
            try {
                predictionResults =
                        client.getDefaultModels().apparelModel() // You can also do client.getModelByID("id") to get your custom models
                                .predict()
                                .withInputs(
                                        ClarifaiInput.forImage(new File(path)))
                                .executeSync()
                                .get();
            } catch (Exception e) {
                Log.d("Loader", "RequestFailed");
            }


        } else if (concept == 2) {
            try {
                predictionResults =
                        client.getDefaultModels().foodModel() // You can also do client.getModelByID("id") to get your custom models
                                .predict()
                                .withInputs(
                                        ClarifaiInput.forImage(new File(path)))
                                .executeSync()
                                .get();
            } catch (Exception e) {
                Log.d("Loader", "RequestFailed");
            }

        } else if (concept == 3) {
            try {
                predictionResults =
                        client.getDefaultModels().moderationModel() // You can also do client.getModelByID("id") to get your custom models
                                .predict()
                                .withInputs(
                                        ClarifaiInput.forImage(new File(path)))
                                .executeSync()
                                .get();
            } catch (Exception e) {
                Log.d("Loader", "RequestFailed");
            }


        } else if (concept == 4) {
            try {
                predictionResults =
                        client.getDefaultModels().travelModel() // You can also do client.getModelByID("id") to get your custom models
                                .predict()
                                .withInputs(
                                        ClarifaiInput.forImage(new File(path)))
                                .executeSync()
                                .get();
            } catch (Exception e) {
                Log.d("Loader", "RequestFailed");
            }

        } else if (concept == 5) {
            try {
                predictionResults =
                        client.getDefaultModels().weddingModel()// You can also do client.getModelByID("id") to get your custom models
                                .predict()
                                .withInputs(
                                        ClarifaiInput.forImage(new File(path)))
                                .executeSync()
                                .get();

            } catch (Exception e) {
                Log.d("Loader", "RequestFailed");
            }

        }
            if (predictionResults != null)
                return predictionResults.get(0).data();
            else
                return null;

    }
}