package com.example.android_lab_7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "location";
    public static final String ID_COL = "_id";
    public static final String LATITUDE_COL = "latitude";
    public static final String LONGITUDE_COL = "longitude";
    public static final String TIME_COL = "time";
    public static final String ADDRESS_COL = "address";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LATITUDE_COL + " REAL NOT NULL,"
                + LONGITUDE_COL + " REAL NOT NULL,"
                + TIME_COL + " CHAR(64) NOT NULL,"
                + ADDRESS_COL + " CHAR(128) NOT NULL)";
        
        db.execSQL(query);
    }

    public void addNewLocation(double latitude, double longitude, String time, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        
        values.put(LATITUDE_COL, latitude);
        values.put(LONGITUDE_COL, longitude);
        values.put(TIME_COL, time);
        values.put(ADDRESS_COL, address);
        
        db.insert(TABLE_NAME, null, values);
        
        db.close();
    }

    public Cursor readLocations() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(String.format("SELECT * from %s ORDER BY %s DESC LIMIT %d", TABLE_NAME, ID_COL, 25), null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

