package com.android.indie.school.mylocalizationdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ConstantPref {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvLangSelection)
    TextView tvLangSelection;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.btnExample)
    Button btnExample;
    @BindView(R.id.imgFlag)
    ImageView imgFlag;
    @BindView(R.id.llPicker)
    LinearLayout llPicker;

    private String myLang = "en";
    private String myFlagCode = null;

    private final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (savedInstanceState != null) {
            setMyFlagCode(savedInstanceState.getString(FLAG_KEY));
            setMyLang(savedInstanceState.getString(LANG_KEY));
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        spinner.setOnItemSelectedListener(this);
        btnExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyNewLocale(getMyLang());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (i) {
            case 0: {
                setMyLang(null);
                setMyFlagCode("us");
            }
            break;

            case ID_ENGLISH: {
                setMyLang(CODE_ENGLISH);
                setMyFlagCode("us");
            }
            break;

            case ID_FRENCH: {
                setMyLang(CODE_FRENCH);
                setMyFlagCode("fr");
            }
            break;

            case ID_DEUTCH: {
                setMyLang(CODE_DEUTCH);
                setMyFlagCode("de");
            }
            break;

            case ID_BAHASA: {
                setMyLang(CODE_BAHASA);
                setMyFlagCode("id");
            }
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String getMyLang() {
        return myLang;
    }

    public void setMyLang(String myLang) {
        this.myLang = myLang;
    }

    public String getMyFlagCode() {
        return myFlagCode;
    }

    public void setMyFlagCode(String myFlagCode) {
        this.myFlagCode = myFlagCode;
    }

    private void showToast(String code) {
        if (code == null) {
            Toast.makeText(MainActivity.this, "No Language Selected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "You Pick Lang : " + code, Toast.LENGTH_SHORT).show();
        }
    }

    private void savePreference(final String lang, final String flagCode) {
        sharedPreferences.edit()
                .putString(LANG_KEY, lang)
                .putString(FLAG_KEY, flagCode)
                .apply();
    }

    private void applyNewLocale(final String lang) {
        showToast(lang);
        if (lang != null) {
            savePreference(lang, getMyFlagCode());
            MyApp.getInstance().updateLanguage(this);
            changeFlagIcon(getMyFlagCode());
            doRecreateActivity();
        }
    }

    private void doRecreateActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.recreate();
        } else {
            Intent refresh = new Intent(this, MainActivity.class);
            finish();
            startActivity(refresh);
        }
    }

    private void changeFlagIcon(final String code) {
        if (code == null)
            return;

        try {
            imgFlag.setImageResource(this.getResources().getIdentifier("drawable/"+code, null, this.getPackageName()));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(FLAG_KEY, myFlagCode);
        outState.putString(LANG_KEY, myLang);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setMyFlagCode(savedInstanceState.getString(FLAG_KEY));
        setMyLang(savedInstanceState.getString(LANG_KEY));
    }

    @Override
    protected void onResume() {
        setMyFlagCode(sharedPreferences.getString(FLAG_KEY, "us"));
        setMyLang(sharedPreferences.getString(LANG_KEY, CODE_ENGLISH));
        changeFlagIcon(getMyFlagCode());
        MyApp.getInstance().updateLanguage(this);
        super.onResume();
    }
}
