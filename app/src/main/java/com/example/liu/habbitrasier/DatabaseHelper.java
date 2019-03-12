package com.example.liu.habbitrasier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by liu on 3/10/19.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "habit_table";

    //Table Details
    private static final String ColHabitID = "ID";
    private static final String ColHabitName = "habitName";
    private static final String ColStartDate = "startDate";
    private static final String ColEndDate = "endDate";
    private static final String ColFrequency = "frequency";
    private static final String ColDuration = "duration";
    private static final String ColNotification = "notification";
    private static final String ColDescription = "description";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColHabitName + " TEXT, " + ColStartDate + " TEXT, " + ColEndDate + " TEXT, " + ColFrequency + " TEXT, " + ColDuration + " TEXT, " + ColNotification + " TEXT, " + ColDescription + " TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME + "'");
    }

    public boolean addData(String item1, String item2, String item3, String item4, String item5, String item6, String item7) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColHabitName, item1);
        contentValues.put(ColStartDate, item2);
        contentValues.put(ColEndDate, item3);
        contentValues.put(ColFrequency, item4);
        contentValues.put(ColDuration, item5);
        contentValues.put(ColNotification, item6);
        contentValues.put(ColDescription, item7);

        Log.d(TAG, "addData: Adding Habit, " + item1 + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //If data is inserted incorrectly, it will return
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Cursor to return database data
    public Cursor getData() {
        Log.d(TAG, "getData: got into function.");
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d(TAG, "getData: got database.");
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        Log.d(TAG, "getData: made cursor and reutrn.");
        return data;
    }


    //Cursor to get item id
    public Cursor getItemID(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + ColHabitID + " FROM " + TABLE_NAME +
                " WHERE " + ColHabitName + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //Update name of item
    public void updateName(String newName, int id, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE '" + TABLE_NAME + "' WHERE " + ColHabitID +
                " = '" + id + "' " + " AND " + ColHabitName + " = '" +
                oldName + "'";
        db.execSQL(query);
    }

    //Delete food
    public void deleteHabit(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + ColHabitID + " = '" + id + "' AND " + ColHabitName +
                " = '" + name + "'";
        db.execSQL(query);
    }
}