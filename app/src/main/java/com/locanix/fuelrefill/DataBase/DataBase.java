package com.locanix.fuelrefill.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DATA_BASE = "FuelRefill";
    private String TABLE_NAME = "FuelRefillTable";
    private static final String COL_ID ="id";
    private static final String COL_VEHICLE_ID ="vehicle_id";
    private static final String COL_FUEL ="fuel";
    private static final String COL_REFILL_TIME ="Refill_time";
    private static final String COL_CAR_DRIVER ="car_driver";
    private static final String COL_BEFORE_REFILL ="beforeRefill";
    private static final String COL_AFTER_REFILL ="afterRefill";
    SQLiteDatabase db;
    private Context context;

    public DataBase(Context context) {
        super(context, DATA_BASE, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +  "("+COL_ID+" integer primary key  autoincrement,"+COL_VEHICLE_ID+" TEXT,"+COL_FUEL+" TEXT,"+COL_REFILL_TIME+" TEXT,"+COL_CAR_DRIVER+" TEXT,"+COL_BEFORE_REFILL+" TEXT,"+COL_AFTER_REFILL+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String vehicleId,String fuel,String refillTime,String carDriver,String before,String after){
        db=getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_VEHICLE_ID, vehicleId);
        contentValues.put(COL_FUEL, fuel);
        contentValues.put(COL_REFILL_TIME, refillTime);
        contentValues.put(COL_CAR_DRIVER, carDriver);
        contentValues.put(COL_BEFORE_REFILL, before);
        contentValues.put(COL_AFTER_REFILL, after);
        long result = db.insert(TABLE_NAME,null,contentValues);
        db.close();

        if(result<1) {
            return false;
        }else{
            return true;
        }
    }


    public Void deleteTable() {
        db = getWritableDatabase();
        db = getReadableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        return null;
    }

    public Cursor getData(){

        db = getReadableDatabase();
        Cursor data=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);

        return data;
    }
}
