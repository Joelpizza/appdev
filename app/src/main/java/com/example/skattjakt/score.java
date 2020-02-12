package com.example.skattjakt;

public class score {
    int _id;
    int _score;
    String _date;

    public score(){   }
    public score(int id, int score, String date){
        this._id = id;
        this._score = score;
        this._date = date;

    }
    public score(int score){
        this._score = score;
    }
    public score(int score, String date){
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
    public String getDate(){
        return this._date;
    }
    public void setDate(String date){
        this._date = date;
    }

    public void setScore(int score){
        this._score = score;
    }


}
