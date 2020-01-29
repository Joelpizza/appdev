package com.example.skattjakt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class infopage extends AppCompatActivity {
    public static final String DIFFICULTY = "com.example.skattjakt.DIFFICULTY";
    int difficulty;
    boolean settchange = false;
    boolean icons = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infopage);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MapsActivity.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.textView);
        String[] messarr = message.split(",",3);
        difficulty = parseInt(messarr[1]);
        icons=parseBoolean(messarr[2]);
        textView.setText(messarr[0]);
        RadioButton radiobutton;
        RadioButton radiobutton2;
        if(difficulty==1){
            radiobutton = findViewById(R.id.radioButton);
        }
        else if(difficulty==2){
            radiobutton = findViewById(R.id.radioButton2);
        }
        else if(difficulty==3){
            radiobutton = findViewById(R.id.radioButton3);
        }
        else{
            radiobutton = findViewById(R.id.radioButton4);
        }
        radiobutton.setChecked(true);
        if(icons){
            radiobutton2 = findViewById(R.id.radioButton6);
        }
        else{
            radiobutton2 = findViewById(R.id.radioButton5);
        }
        radiobutton2.setChecked(true);

    }
    public void openOldIntent(View view){
        if(settchange){
            difficultySwitch(view);
        }
        else {
            this.finish();
        }
    }
    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        settchange = true;
        switch(view.getId()) {
            case R.id.radioButton:
                if (checked)
                    difficulty = 1;
                    break;
            case R.id.radioButton2:
                if (checked)
                    difficulty = 2;
                    break;
            case R.id.radioButton3:
                if (checked)
                    difficulty = 3;
                break;
            case R.id.radioButton4:
                if (checked)
                    difficulty = 4;
                break;
            case R.id.radioButton5:
                if (checked)
                    icons = false;
                break;
            case R.id.radioButton6:
                if (checked)
                    icons = true;
                break;
        }
    }
    public void difficultySwitch(View view){
        MapsActivity.firstActivity.finish();
        //förhindrar memory leak genom att nulla variabeln
        MapsActivity.firstActivity = null;
        Intent intent = new Intent ( this,MapsActivity.class);
        String send = difficulty+","+icons;
        intent.putExtra(DIFFICULTY, send);
        startActivity(intent);
        this.finish();

    }
}
