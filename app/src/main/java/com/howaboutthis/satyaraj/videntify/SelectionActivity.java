package com.howaboutthis.satyaraj.videntify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class SelectionActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Pick An API");
        CardView visualRecognition = findViewById(R.id.visual_recognition);
        CardView textToneAnalyzer = findViewById(R.id.text_tone_analyzer);
        CardView haveConversation = findViewById(R.id.have_a_conversation);
        CardView languageTranslator = findViewById(R.id.language_translator);

        visualRecognition.setOnClickListener(this);
        textToneAnalyzer.setOnClickListener(this);
        haveConversation.setOnClickListener(this);
        languageTranslator.setOnClickListener(this);

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
                Intent intent = new Intent(SelectionActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.visual_recognition:
                startActivity(new Intent(SelectionActivity.this,IdentifyActivity.class));
                break;

            case R.id.text_tone_analyzer:
                startActivity(new Intent(SelectionActivity.this,ToneAnalyzerActivity.class));
                break;

            case R.id.have_a_conversation:
                startActivity(new Intent(SelectionActivity.this, ConversationActivity.class));
                break;

            case R.id.language_translator:
                startActivity(new Intent(SelectionActivity.this, LanguageTranslatorActivity.class));
                break;
        }
    }
}
