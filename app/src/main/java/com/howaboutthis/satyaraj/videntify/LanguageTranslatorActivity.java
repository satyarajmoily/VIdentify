package com.howaboutthis.satyaraj.videntify;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;

public class LanguageTranslatorActivity extends AppCompatActivity implements View.OnClickListener {

    private static Language mSourceLanguage;
    private static Language mTargetLanguage;
    private int sourcePosition;
    private int targetPosition;
    private EditText inputEditText;
    private static Button translateButton;
    private static CardView translatedLanguage;
    private LinearLayout sourceLayout;
    private int tPos;
    private int sPos;
    private TextView sourceTextView;
    private String sourceLanguageSelected;
    private String targetLanguageSelected;
    private Spinner sourceLanguage;
    private Spinner targetLanguage;
    private static ProgressBar progressBar;
    private static TextView targetLanguageTextView;
    private static TextView translatedTextView;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_translator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        sourceLanguage = findViewById(R.id.source_language);
        targetLanguage = findViewById(R.id.target_language);
        translateButton = findViewById(R.id.translate_button);
        inputEditText = findViewById(R.id.translator_edit_text);
        sourceLayout = findViewById(R.id.source_language_canceler);
        sourceTextView = findViewById(R.id.source_language_text_view);
        ImageView swap = findViewById(R.id.swap);
        targetLanguageTextView = findViewById(R.id.target_language_text_view);
        translatedTextView = findViewById(R.id.translated_text_view);
        progressBar = findViewById(R.id.progressBar2);
        ImageView close = findViewById(R.id.close);
        setSupportActionBar(toolbar);
        setTitle("Language Translator");
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = getApplicationContext();
        translatedLanguage = findViewById(R.id.translated_card_view);
        translatedLanguage.setVisibility(View.GONE);
        sourceLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        tPos = targetLanguage.getSelectedItemPosition();
        sPos = sourceLanguage.getSelectedItemPosition();

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.languages, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceLanguage.setAdapter(adapter);
        targetLanguage.setAdapter(adapter);

        translateButton.setOnClickListener(this);
        close.setOnClickListener(this);
        swap.setOnClickListener(this);

        sourceLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sourcePosition = position;
                sourceLanguageSelected = parent.getItemAtPosition(position).toString();
                sourceTextView.setText(sourceLanguageSelected);
                translateButton.setVisibility(View.VISIBLE);
                translatedLanguage.setVisibility(View.INVISIBLE);


                if (position == 1)
                    mSourceLanguage = Language.ARABIC;
                else if (position == 2)
                       mSourceLanguage = Language.GERMAN;
                else if (position == 3)
                    mSourceLanguage = Language.ENGLISH;
                else if (position == 4)
                    mSourceLanguage = Language.SPANISH;
                else if (position == 5)
                    mSourceLanguage = Language.FRENCH;
                else if (position == 6)
                    mSourceLanguage = Language.ITALIAN;
                else if (position == 7)
                    mSourceLanguage = Language.JAPANESE;
                else if (position == 8)
                    mSourceLanguage = Language.KOREAN;
                else if (position == 9)
                    mSourceLanguage = Language.PORTUGUESE;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        targetLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                targetPosition = position;
                targetLanguageSelected = parent.getItemAtPosition(position).toString();
                translateButton.setVisibility(View.VISIBLE);
                translatedLanguage.setVisibility(View.INVISIBLE);
                if (sourcePosition == targetPosition)
                    targetLanguage.setSelection(0);

                else if (position == 1)
                    mTargetLanguage = Language.ARABIC;
                else if (position == 2)
                    mTargetLanguage = Language.GERMAN;
                else if (position == 3)
                    mTargetLanguage = Language.ENGLISH;
                else if (position == 4)
                    mTargetLanguage = Language.SPANISH;
                else if (position == 5)
                    mTargetLanguage = Language.FRENCH;
                else if (position == 6)
                    mTargetLanguage = Language.ITALIAN;
                else if (position == 7)
                    mTargetLanguage = Language.JAPANESE;
                else if (position == 8)
                    mTargetLanguage = Language.KOREAN;
                else if (position == 9)
                    mTargetLanguage = Language.PORTUGUESE;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                translateButton.setVisibility(View.VISIBLE);
                translatedLanguage.setVisibility(View.INVISIBLE);
                sourceLayout.setVisibility(View.VISIBLE);
                sourceTextView.setText(sourceLanguageSelected);

                if (count == 0)
                    sourceLayout.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                Intent intent = new Intent(LanguageTranslatorActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
                return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.translate_button:
                translate();
                break;

            case R.id.close:
                inputEditText.setText("");
                sourceLayout.setVisibility(View.GONE);
                translateButton.setVisibility(View.VISIBLE);
                translatedLanguage.setVisibility(View.INVISIBLE);
                break;

            case R.id.swap:
                tPos = targetLanguage.getSelectedItemPosition();
                sPos = sourceLanguage.getSelectedItemPosition();
                targetLanguage.setSelection(sPos);
                sourceLanguage.setSelection(tPos);

                break;

        }
    }

    private void translate() {
        String inputString = inputEditText.getText().toString();

        if (inputString.matches(""))
            Toast.makeText(this,"Please enter something",Toast.LENGTH_LONG).show();

        else if (sourcePosition == 0 || targetPosition == 0)
            Toast.makeText(this,"Please select a language",Toast.LENGTH_LONG).show();

        else {

            InputMethodManager imm = (InputMethodManager)
            getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(
                        inputEditText.getWindowToken(), 0);
            }
            translateButton.setVisibility(View.GONE);
            translatedLanguage.setVisibility(View.VISIBLE);
            targetLanguageTextView.setText(targetLanguageSelected);
            progressBar.setVisibility(View.VISIBLE);
            new TranslateLanguage().execute(inputString);
        }
    }

    public static class TranslateLanguage extends AsyncTask<String, Void,  String> {

        @Override
        protected  String doInBackground(String...input) {

            try {
                LanguageTranslator service = new LanguageTranslator();
                service.setUsernameAndPassword("765b4741-b42c-453e-a0a5-4b0560a87a07", "yfxFyMOJh0Mm");
                service.setEndPoint("https://gateway.watsonplatform.net/language-translator/api");

                TranslationResult translatedText = service
                        .translate(
                                input[0],
                                mSourceLanguage,
                                mTargetLanguage
                        ).execute();
                return translatedText.getFirstTranslation();
            }catch (Exception e){
                Log.d("TranslateLanguage","Error in background");
                return null;
            }
        }

        @Override
        protected void onPostExecute( String word) {
            translatedTextView.setText(word);
            progressBar.setVisibility(View.GONE);
            if (word == null) {
                Toast.makeText(mContext, "Please try again or check Internet Connectivity or language selected", Toast.LENGTH_LONG).show();
                translatedLanguage.setVisibility(View.GONE);
                translateButton.setVisibility(View.VISIBLE);
            }
        }
    }

}
