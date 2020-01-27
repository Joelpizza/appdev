package com.example.skattjakt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class infopage extends AppCompatActivity {
    public static final String DIFFICULTY = "com.example.skattjakt.DIFFICULTY";
    int difficulty;
    Boolean diffchange = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infopage);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MapsActivity.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
    }
    public void openOldIntent(View view){
        if(diffchange){
            difficultySwitch(view);
        }
        else {
            this.finish();
        }
    }
    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        diffchange = true;
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
        }
    }
    public void difficultySwitch(View view){
        MapsActivity.firstActivity.finish();
        MapsActivity.firstActivity = null;
        Intent intent = new Intent ( this,MapsActivity.class);
        String send = ""+difficulty;
        intent.putExtra(DIFFICULTY, send);
        startActivity(intent);
        this.finish();

    }
}
