package com.howaboutthis.satyaraj.videntify;


import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import clarifai2.dto.prediction.Concept;

public class detailedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private String path;
    private RecyclerView mRecyclerView;
    private ProgressDialog dialog;
    private int conceptPosition;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        ImageView selectedImage = findViewById(R.id.selected_image);
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog = new ProgressDialog(detailedActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setTitle("PICK A CONCEPT");

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        path = bundle.getString("PATH");

        final BitmapFactory.Options options = new BitmapFactory.Options();

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        selectedImage.setImageBitmap(bitmap);

        showList();

        final FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showList();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean  onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent intent = new Intent(detailedActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
                return super.onOptionsItemSelected(item);

    }


    private void showList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(detailedActivity.this);
        LayoutInflater inflater = detailedActivity.this.getLayoutInflater();

        ArrayList<String> values = new ArrayList<>();

        values.add("General - Anything in general");
        values.add("Apparel - Fashion-related items");
        values.add("Food - Food items and dishes");
        values.add("Moderation - Unwanted contents like drugs");
        values.add("Travel - Travel and hospitality");
        values.add("Wedding - Wedding-related concepts");

        @SuppressLint("InflateParams") View builderView = inflater.inflate(R.layout.list_view, null);

        final ListView listView = builderView.findViewById(R.id.list_view);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(detailedActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter1);
        builder.setTitle("What to detect");
        builder.setView(builderView);
        final AlertDialog alert = builder.create();
        alert.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                conceptPosition = position;
                alert.dismiss();
                String concept = (String) listView.getItemAtPosition(position);
                collapsingToolbar.setTitle(concept);

                getLoaderManager().restartLoader(0,null,detailedActivity.this);

            }
        });
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        if(id == 0) {
            dialog.setMessage("Please be patience, this can take a while depending on the image size.");
            dialog.setIndeterminate(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            try {
                dialog.show();
            } catch (Exception e) {
                dialog.dismiss();
            }
            return new TestInternetLoader(this);
        }

        else if (id == 1)
            return new detailedLoader(this, path,conceptPosition);

        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        int id = loader.getId();
        if (id == 0){
            boolean check = (boolean) data;
            if (check)
                getLoaderManager().restartLoader(1,null,this);
            else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                collapsingToolbar.setTitle("Pick a concept");

                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        }else if (id == 1) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (data != null){
                List<Concept> predictionResults = (List<Concept>) data;
                RecyclerView.Adapter mAdapter = new DetailedAdapter(predictionResults);
                mRecyclerView.setAdapter(mAdapter);
            }
           else
                Toast.makeText(this,"Please try again",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
