package com.example.skattjakt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class infopage extends AppCompatActivity {
    public static final String DIFFICULTY = "com.example.skattjakt.DIFFICULTY";
    int difficulty;
    boolean settchange = false;
    boolean icons = true;
    boolean nightmode = false;
    float zoom;
    SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infopage);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MapsActivity.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.textView);
        String[] messarr = message.split(",",5);
        textView.setText(messarr[0]);
        difficulty = parseInt(messarr[1]);
        icons = parseBoolean(messarr[2]);
        nightmode = parseBoolean(messarr[3]);
        zoom = parseFloat(messarr[4]);
        RadioButton radiobutton2;
        RadioButton radiobutton3;
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        if(icons){
            radiobutton2 = findViewById(R.id.radioButton6);
        }
        else{
            radiobutton2 = findViewById(R.id.radioButton5);
        }
        radiobutton2.setChecked(true);
        if(nightmode){
            radiobutton3 = findViewById(R.id.radioButton8);
        }
        else{
            radiobutton3 = findViewById(R.id.radioButton7);
        }
        radiobutton3.setChecked(true);
        seekBar.setProgress(difficulty-5);
    }
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            settchange =true;
        }
    };

    public void openOldIntent(View view){
        if(settchange){
            difficultySwitch(view);
        }
        else {
            MapsActivity.clickedinfo = false;
            MapsActivity.nightmode = nightmode;
            MapsActivity.icons = icons;
            this.finish();
        }
    }
    @Override
    public void onBackPressed(){
        MapsActivity.clickedinfo = false;
        MapsActivity.nightmode = nightmode;
        MapsActivity.icons = icons;
        this.finish();
    }
    public void stats(View view){
        Intent intent = new Intent ( this,Stats.class);
        startActivity(intent);
    }
    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();


        switch(view.getId()) {
            case R.id.radioButton5:
                if (checked)
                    icons = false;
                break;
            case R.id.radioButton6:
                if (checked)
                    icons = true;
                break;
            case R.id.radioButton7:
                if (checked)
                    nightmode = false;
                break;
            case R.id.radioButton8:
                if (checked)
                    nightmode = true;
                break;
        }
    }
    public void difficultySwitch(View view){
        difficulty = 5+seekBar.getProgress();
        MapsActivity.firstActivity.finish();
        //förhindrar memory leak genom att nulla variabeln
        MapsActivity.firstActivity = null;
        Intent intent = new Intent ( this,MapsActivity.class);
        String send = difficulty+","+icons+","+nightmode+","+zoom;
        intent.putExtra(DIFFICULTY, send);
        startActivity(intent);
        this.finish();

    }
}
