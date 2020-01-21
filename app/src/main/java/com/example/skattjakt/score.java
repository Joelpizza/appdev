package com.example.skattjakt;

public class score {
    int _id;
    int _score;

    public score(){   }
    public score(int id, int score){
        this._id = id;
        this._score = score;

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

    public void setScore(int score){
        this._score = score;
    }


}
