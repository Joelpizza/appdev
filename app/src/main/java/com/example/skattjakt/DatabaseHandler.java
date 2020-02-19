package com.example.skattjakt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "ScoresManager";
    private static final String TABLE_CONTACTS = "Scores";
    private static final String KEY_ID = "id";
    private static final String KEY_SCORE = "score";
    private static final String KEY_DATE = "date";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SCORE + " TEXT," + KEY_DATE + " DATE" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new Score
    void addscore(score Score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SCORE, Score.getScore());
        values.put(KEY_DATE, new Date().getTime());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get all Scores in a list view
    public List<score> getAllscores() {
        List<score> ScoreList = new ArrayList<score>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                score Score = new score();
                Score.setID(Integer.parseInt(cursor.getString(0)));
                Score.setScore(Integer.parseInt(cursor.getString(1)));

                ScoreList.add(Score);
            } while (cursor.moveToNext());
        }

        // return Score list
        return ScoreList;
    }
    public List<score> getAllDatescores() {
        List<score> ScoreList = new ArrayList<score>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                score Score = new score();
                Score.setID(Integer.parseInt(cursor.getString(0)));
                Score.setScore(Integer.parseInt(cursor.getString(1)));
                Score.setDate(cursor.getLong(2));

                ScoreList.add(Score);
            } while (cursor.moveToNext());
        }

        // return Score list
        return ScoreList;
    }
public List<score> getDatescore(Date dated){
    List<score> DateList = new ArrayList<score>();
    String selectQuery1 = "SELECT date FROM "+TABLE_CONTACTS;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor1 = db.rawQuery(selectQuery1, null);
    if (cursor1.moveToFirst()) {
        do {
            SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
            if(dformat.format(cursor1.getLong(0)).equals(dformat.format(dated))){

                String selectQuery = "SELECT SUM(score) FROM "+TABLE_CONTACTS +" WHERE "+KEY_DATE+" = '"+cursor1.getLong(0)+"'";
                //Log.i("score",selectQuery);

                Cursor cursor = db.rawQuery(selectQuery, null);

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        score Score = new score();
                        Score.setScore(Integer.parseInt(cursor.getString(0)));
                        DateList.add(Score);

                    } while (cursor.moveToNext());
                }
            }
        } while (cursor1.moveToNext());
    }
    return DateList;
}
    // code to update the single Score
    public int updatescore(score Score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SCORE, Score.getScore());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?", new String[] { String.valueOf(Score.getID()) });
    }

    // Deleting single score
    public void deletescore(score Score) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?", new String[] { String.valueOf(Score.getID()) });
        db.close();
    }

    // Getting Scores Count
    public int getscoresCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
