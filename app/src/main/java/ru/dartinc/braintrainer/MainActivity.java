package ru.dartinc.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private TextView textViewOpinion0;
    private TextView textViewOpinion1;
    private TextView textViewOpinion2;
    private TextView textViewOpinion3;
    private TextView textViewTimer;
    private TextView textViewScore;
    private TextView textViewQuession;
    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 5;
    private int max = 30;
    private List<TextView> textViewList = new ArrayList<>();
    private int score;
    private int countAnswer;
    private boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getView();
        fillListTextView();
        startGame();
        CountDownTimer timer = new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(getTimeString(millisUntilFinished));
                if(millisUntilFinished<10000){
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }

            @Override
            public void onFinish() {
                gameOver = true;
                textViewTimer.setText(getTimeString(0L));
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = preferences.getInt("max",0);
                if(score>max){
                    Log.i("MyPreferences",Integer.toString(score));
                    preferences.edit().putInt("max",score).apply();
                }
                Intent intent = new Intent(MainActivity.this,ScoreActivity.class);
                intent.putExtra("result",textViewScore.getText().toString());
                startActivity(intent);
            }
        };
        timer.start();
    }

    private  String getTimeString(Long msec){
        long sec = msec/1000;
        long min = sec/60;
        sec = sec%60;
        return String.format(Locale.getDefault(),"%02d:%02d",min,sec);
    }

    private void fillTextView() {
        for (int i = 0; i < textViewList.size(); i++) {
            TextView textView = textViewList.get(i);
            if (i == rightAnswerPosition) {
                textView.setText(String.format("%s",rightAnswer));
            } else {
                textView.setText(String.format("%s",generateWrongAnswer()));
            }
        }
        textViewScore.setText(String.format("%s / %s", score, countAnswer));
        textViewQuession.setText(question);
    }

    private int generateWrongAnswer() {
        int result;
        do {
            result = (int) (Math.random() * max * 2 + 1) - (max - min);
        } while (result == rightAnswer);
        return result;
    }

    private void generateQuestion() {
        int a = (int) (Math.random() * (max - min + 1) + min);
        int b = (int) (Math.random() * (max - min + 1) + min);
        int mark = (int) (Math.random() * 2);
        isPositive = mark == 1;

        if (isPositive) {
            rightAnswer = a + b;
            question = String.format("%s + %s", a, b);
        } else {
            rightAnswer = a - b;
            question = String.format("%s - %s", a, b);
        }
        rightAnswerPosition = (int) (Math.random() * 4);
    }

    private void getView() {
        textViewOpinion0 = findViewById(R.id.textViewOpinion0);
        textViewOpinion1 = findViewById(R.id.textViewOpinion1);
        textViewOpinion2 = findViewById(R.id.textViewOpinion2);
        textViewOpinion3 = findViewById(R.id.textViewOpinion3);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewScore = findViewById(R.id.textViewScore);
        textViewQuession = findViewById(R.id.textViewQuestion);
    }

    private void fillListTextView() {
        textViewList.add(textViewOpinion0);
        textViewList.add(textViewOpinion1);
        textViewList.add(textViewOpinion2);
        textViewList.add(textViewOpinion3);
    }

    private void startGame() {
        generateQuestion();
        fillTextView();
    }

    public void onClickAnswer(View view) {
        if(!gameOver) {
            TextView textView = (TextView) view;
            if (Integer.parseInt(textView.getText().toString()) == rightAnswer) {
                score++;
                Toast.makeText(this, "Верно", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "НЕВЕРНО", Toast.LENGTH_SHORT).show();
            }
            countAnswer++;

            startGame();
        }
    }

}