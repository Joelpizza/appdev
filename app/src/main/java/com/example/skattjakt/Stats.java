package com.example.skattjakt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class Stats extends AppCompatActivity {
    public DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        db = new DatabaseHandler(this);
        List<score> scores = db.getAllDatescores();
        int totalscoredate[]=new int[scores.size()];
        int forloop = 0;
        String[] date = new String[scores.size()];
        for(score sc : scores) {
            List<score> dates = db.getDatescore(sc.getDate());
            for (score dt : dates){
                if(!Arrays.asList(date).contains(sc.getDate())){
                    totalscoredate[forloop] += dt.getScore();
                }
            }
            date[forloop] = sc.getDate();
            forloop++;
        }
        TextView[] textView = new TextView[10];
        textView[0] = findViewById(R.id.statist1);
        textView[1] = findViewById(R.id.statist2);
        textView[2] = findViewById(R.id.statist3);
        textView[3] = findViewById(R.id.statist4);
        textView[4] = findViewById(R.id.statist5);
        textView[5] = findViewById(R.id.statist6);
        textView[6] = findViewById(R.id.statist7);
        textView[7] = findViewById(R.id.statist8);
        textView[8] = findViewById(R.id.statist9);
        textView[9] = findViewById(R.id.statist10);

        for(int i=0;i<totalscoredate.length&&i<10;i++){
            if(totalscoredate[i]!=0){
                textView[i].setText(date[i]+" poäng: "+totalscoredate[i]);
            }
        }
    }
}
