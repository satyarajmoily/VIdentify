package com.howaboutthis.satyaraj.videntify;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ElementTone;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import java.util.List;

public class ToneAnalyzerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ElementTone>, View.OnClickListener{

    private TextView output;
    private String textToAnalyze;
    private EditText input;
    private ProgressBar progressBar;
    private CardView outputCardView;
    private LinearLayout sourceLayout;
    private Button analyzeButton;
    private TextView analyzingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tone_analyzer);

        input = findViewById(R.id.text_to_analyze);
        output = findViewById(R.id.analyzed_text_view);
        analyzeButton = findViewById(R.id.analyze_button);
        ImageView clear = findViewById(R.id.close);
        progressBar = findViewById(R.id.progressBar2);
        outputCardView = findViewById(R.id.analyze_card_view);
        sourceLayout = findViewById(R.id.source_text_canceler);
        analyzingTextView = findViewById(R.id.analyzing);
        sourceLayout.setVisibility(View.GONE);
        outputCardView.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Tone Analyzer");

        analyzeButton.setOnClickListener(this);
        clear.setOnClickListener(this);
        progressBar.setVisibility(View.GONE);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sourceLayout.setVisibility(View.VISIBLE);
                analyzeButton.setVisibility(View.VISIBLE);
                outputCardView.setVisibility(View.GONE);

                if (count == 0)
                    sourceLayout.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public Loader<ElementTone> onCreateLoader(int id, Bundle args) {
        return new ToneAnalyzerLoader(this,textToAnalyze);
    }

    @Override
    public void onLoadFinished(Loader<ElementTone> loader, ElementTone data) {

           progressBar.setVisibility(View.GONE);
           analyzingTextView.setVisibility(View.GONE);
            if (data!=null) {

                StringBuilder outputString = new StringBuilder();
            List<ToneCategory> toneCategories = data.getTones();
            for (ToneCategory nextCategory : toneCategories) {
                String heading = String.valueOf("Analysis for "+ nextCategory.getName());
                outputString.append(heading);
                outputString.append("\n");

                List<ToneScore> toneScores = nextCategory.getTones();
                for (ToneScore nextScore : toneScores) {
                    outputString.append("     ");
                    outputString.append(nextScore.getName());
                    outputString.append(" = ");
                    outputString.append((int)(nextScore.getScore() * 100));
                    outputString.append("%");
                    outputString.append("\n");
                }
                outputString.append("\n");
            }
            output.setText(outputString);
        }
        else
            outputCardView.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<ElementTone> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.analyze_button:
                analyze();
                break;
            case R.id.close:
                clear();
                break;
        }
    }

    private void clear() {
        input.setText("");
        outputCardView.setVisibility(View.GONE);
        analyzeButton.setVisibility(View.VISIBLE);
        output.setText("");
        getLoaderManager().destroyLoader(0);
        progressBar.setVisibility(View.GONE);
    }

    private void analyze() {
        textToAnalyze = input.getText().toString();
        if (textToAnalyze.matches(""))
            Toast.makeText(this,"Please enter something to analyze.",Toast.LENGTH_LONG).show();
        else {

            InputMethodManager imm = (InputMethodManager)
            getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(
                        input.getWindowToken(), 0);
            }
            outputCardView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            analyzeButton.setVisibility(View.GONE);
            analyzingTextView.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(0, null, this);
        }
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
                Intent intent = new Intent(ToneAnalyzerActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
                return super.onOptionsItemSelected(item);
    }
}
