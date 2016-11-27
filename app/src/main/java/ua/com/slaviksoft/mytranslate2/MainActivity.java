package ua.com.slaviksoft.mytranslate2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import ua.com.slaviksoft.mytranslate2.Controllers.AppPreferences;
import ua.com.slaviksoft.mytranslate2.Controllers.LanguagesSpinnerAdapter;
import ua.com.slaviksoft.mytranslate2.GoogleTranslateApi.GoogleTranslate;
import ua.com.slaviksoft.mytranslate2.Model.Language;

public class MainActivity extends AppCompatActivity implements GoogleTranslate.OnLanguagesRequestListener, GoogleTranslate.OnTranslateRequestListener{

    private TextView textViewResult;
    private EditText editTextSource;
    private Spinner spinnerSourceLanguages;
    private Spinner spinnerTargetLanguages;
    private LanguagesSpinnerAdapter spinnerAdapterSource;
    private LanguagesSpinnerAdapter spinnerAdapterTarget;
    private ImageButton imageButtonTranslate;

    private Language sourceLanguage;
    private Language targetLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextSource = (EditText) findViewById(R.id.editTextSource);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        spinnerSourceLanguages = (Spinner) findViewById(R.id.spinnerSourceLanguages);
        spinnerTargetLanguages = (Spinner) findViewById(R.id.spinnerTargetLanguages);
        imageButtonTranslate = (ImageButton) findViewById(R.id.imageButtonTranslate);

        if (AppPreferences.isInternetConnectionActive(this)) {
            GoogleTranslate googleTranslate = new GoogleTranslate();
            googleTranslate.getSupportedLanguages(AppPreferences.getLocaleLanguage(), this);
        }else{
            setError(R.string.label_error_offline);
            imageButtonTranslate.setEnabled(false);
        }
    }

    @Override
    public void onLanguagesRequestSuccess(List<Language> languages) {
        updateSourceAdapter(languages);
        updateTargetAdapter(languages);
    }

    @Override
    public void onLanguagesRequestFailure(int error) {
        sourceLanguage = null;
        targetLanguage = null;
    }

    @Override
    public void OnTranslateRequestSuccess(String translatedText) {
        textViewResult.setText(translatedText);
        hideSoftKeyboard();
    }

    @Override
    public void OnTranslateRequestFailure(int error) {
        if (error == 1) textViewResult.setText(R.string.label_error_wrong_pairs);
            else setError(R.string.label_error_offline);
    }

    private void setError(int id){
        textViewResult.setText(id);
    }

    public void onTranslateClick(View view) {
        translate();
    }

    private void translate() {
        if (sourceLanguage == null) {
            setError(R.string.label_error_select_source);
            return;
        }
        if (targetLanguage == null) {
            setError(R.string.label_error_select_target);
            return;
        }
        if (editTextSource.getText().toString().equals("")){
            setError(R.string.label_error_text_is_empty);
            return;
        }

        AppPreferences.setSourceLanguage(this, sourceLanguage.getCode());
        AppPreferences.setTargetLanguage(this, targetLanguage.getCode());

        GoogleTranslate googleTranslate = new GoogleTranslate();
        googleTranslate.translate(sourceLanguage.getCode(), targetLanguage.getCode(), editTextSource.getText().toString(), this);

    }

    public void updateSourceAdapter(List<Language> languages){

        spinnerAdapterSource = new LanguagesSpinnerAdapter(MainActivity.this, android.R.layout.simple_spinner_item, languages);
        spinnerSourceLanguages.setAdapter(spinnerAdapterSource);
        spinnerSourceLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sourceLanguage = spinnerAdapterSource.getItem(position);
                translate();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sourceLanguage = null;
            }
        });

        // last selected language
        spinnerSourceLanguages.setSelection(spinnerAdapterSource.getPositionByLanguage(AppPreferences.getSourceLanguage(this)));

    }

    public void updateTargetAdapter(List<Language> languages){
        spinnerAdapterTarget = new LanguagesSpinnerAdapter(MainActivity.this, android.R.layout.simple_spinner_item, languages);
        spinnerTargetLanguages.setAdapter(spinnerAdapterTarget);
        spinnerTargetLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                targetLanguage = spinnerAdapterTarget.getItem(position);
                translate();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                targetLanguage = null;
            }
        });

        // last selected language
        spinnerTargetLanguages.setSelection(spinnerAdapterTarget.getPositionByLanguage(AppPreferences.getTargetLanguage(this)));
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

}
