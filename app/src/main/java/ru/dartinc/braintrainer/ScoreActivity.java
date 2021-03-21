package ru.dartinc.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class ScoreActivity extends AppCompatActivity {

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        textViewResult = findViewById(R.id.textViewResult);
        Intent intent = getIntent();
        if(intent!=null && intent.hasExtra("result")) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int max = preferences.getInt("max",0);
            textViewResult.setText(String.format(Locale.getDefault(),"Ваш результат : %s\nЛучший результат: %s",intent.getStringExtra("result"),max));
        }
    }

    public void onClickStartNewGame(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}