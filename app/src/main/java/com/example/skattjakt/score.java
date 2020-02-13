package com.example.skattjakt;

import java.util.Date;

public class score {
    int _id;
    int _score;
    Long _date;

    public score(){   }
    public score(int id, int score, Long date){
        this._id = id;
        this._score = score;
        this._date = date;

    }
    public score(int score){
        this._score = score;
    }
    public score(int score, Long date){
        this._score = score;
        this._date = date;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public int getScore(){
        return this._score;
    }
    public Long getDate(){
        return this._date;
    }
    public void setDate(Long date){
        this._date = date;
    }

    public void setScore(int score){
        this._score = score;
    }


}
