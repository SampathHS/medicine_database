package com.example.testproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


//SQLite database part..........


public class DBHelper extends SQLiteOpenHelper {
    //table name medicine Details
    private static final String TABLE_NAME = "MedicineDetails";

    //table attributes that contains medicine name,date and time
    private static final String COL1 = "MDName";
    private static final String COL2 = "Date";
    private static final String COL3 = "Time";



    public DBHelper(Context context)
    {

        //it create the database with version 1
        super(context, TABLE_NAME,null,1);
    }


    //create table in Database
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL queries
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +COL1 + " TEXT, " + COL2 + " TEXT, " + COL3 + " TEXT)";
        db.execSQL(createTable);
    }


    //drop table in Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //SQL queries
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }


    //tuples (records of medicine ,rows)
    //methods to insert the data...
    public Boolean insertData(String MDName, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();//sql database
        ContentValues contentValues = new ContentValues();//object content values inside our table
        contentValues.put(COL1, MDName);//medicine name
        contentValues.put(COL2, date);//date
        contentValues.put(COL3, time);//time of day


        long result = db.insert(TABLE_NAME,null,contentValues);

        if(result == -1){
            return false;//insertion has failed
        }
        else
            {
            return true;
        }
    }
}
