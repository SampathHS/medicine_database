//Ahhh we meet again you piece of shit, just how useless are you ...
package com.example.testproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "MedicineDetails";
    private static final String COL1 = "MDName";
    private static final String COL2 = "Date";
    private static final String COL3 = "Time";



    public DBHelper(Context context)
    {
        super(context, TABLE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +COL1 + " TEXT, " + COL2 + " TEXT, " + COL3 + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public Boolean insertData(String MDName,String date,int time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, MDName);
        contentValues.put(COL2, date);
        contentValues.put(COL3, time);

        Log.d("dbhelper","inserting data" + MDName + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME,null,contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
}
