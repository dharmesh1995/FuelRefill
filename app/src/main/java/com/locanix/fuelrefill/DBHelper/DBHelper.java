package com.locanix.fuelrefill.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.locanix.fuelrefill.Model.EntryFuel.FuelRefill;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "FuelRefill.db";
    private static final int DATABASE_VERSION = 1;

    // Table_FuelRefill TableColumns names
    private static final String _ID = "_Id";
    private static final String _FID = "F_Id";
    private static final String SCAN_CODE = "Scan_Code";
    private static final String VEHICLE_ID = "Vehicle_Id";
    private static final String F_DRIVER = "F_Driver";
    private static final String F_BEFORE_REFILL = "F_Before_Refill";
    private static final String F_AFTER_REFILL = "F_After_Refill";
    private static final String F_REFILL = "F_Refill";
    private static final String TIME_OF_REFILL = "Time_Of_Refill";

    // FuelRefill Database Table Name
    public static String TABLE_FUEL_REFILL = "Table_Fuel_Refill";

    Context context;

    //2nd table for appointment
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DATABASE_TABLE_FUEL_REFILL = "CREATE TABLE " + TABLE_FUEL_REFILL + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                _FID + " TEXT," +
                SCAN_CODE + " TEXT," +
                VEHICLE_ID + " TEXT," +
                F_DRIVER + " TEXT," +
                F_BEFORE_REFILL + " TEXT," +
                F_AFTER_REFILL + " TEXT," +
                F_REFILL + " TEXT," +
                TIME_OF_REFILL + " TEXT);";

        db.execSQL(CREATE_DATABASE_TABLE_FUEL_REFILL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUEL_REFILL);
    }

    // Methods For Table_Pills_Details_No
    public boolean insertNoPillsDetails(String scan_code) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SCAN_CODE, scan_code);

        long result = db.insert(TABLE_FUEL_REFILL, null, values);
        db.close();
        return result != -1;
    }

    public ArrayList<FuelRefill> getPillsNoRecords() {

        ArrayList<FuelRefill> timeInDays = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_FUEL_REFILL, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                FuelRefill timeInDay = new FuelRefill();
                timeInDay.set_id(cur.getInt(1));
                timeInDay.setScanCode(cur.getString(2));
                timeInDay.setVehicleId(cur.getString(3));
                timeInDay.setFDriver(cur.getString(4));
                timeInDay.setFBeforeRefill(cur.getString(5));
                timeInDay.setFAfterRefill(cur.getString(6));
                timeInDay.setFRefill(cur.getString(7));
                timeInDay.setTimeOfRefill(cur.getString(8));

                timeInDays.add(timeInDay);
            } while (cur.moveToNext());
            cur.close();
        }

        return timeInDays;
    }

    public String getScanCode() {
        String scanCode = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_FUEL_REFILL, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                scanCode = cur.getString(2);
                Log.e("LLL_Fuel: ", cur.getString(2));

            } while (cur.moveToNext());
            cur.close();
        }

        return scanCode;
    }

    public int getId() {
        int scanCode = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_FUEL_REFILL, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                scanCode = cur.getInt(0);
                Log.e("LLL_Fuel: ", String.valueOf(scanCode));

            } while (cur.moveToNext());
            cur.close();
        }

        return scanCode;
    }

    public int getIdMain() {
        int scanCode = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_FUEL_REFILL, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                scanCode = cur.getInt(0);
                Log.e("LLL_Fuel: ", String.valueOf(scanCode));

            } while (cur.moveToNext());
            cur.close();
        }

        return scanCode;
    }

    public boolean updateVehicleNo(String scan_code, int id, String vehicle_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(VEHICLE_ID, vehicle_id);

        db.update(TABLE_FUEL_REFILL, values, _FID + " = ? and " + SCAN_CODE + " = ? ", new String[]{String.valueOf(id), scan_code});

        return true;
    }

    public boolean updateFuelNoRecords(String id, String scan_code, String vehicle_id, String F_Driver, String F_Before_Refill, String F_After_Refill, String F_Refill, String Time_Of_Refill) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(_FID, String.valueOf(getIdMain()));
        values.put(VEHICLE_ID, vehicle_id);
        values.put(F_DRIVER, F_Driver);
        values.put(F_BEFORE_REFILL, F_Before_Refill);
        values.put(F_AFTER_REFILL, F_After_Refill);
        values.put(F_REFILL, F_Refill);
        values.put(TIME_OF_REFILL, Time_Of_Refill);

        db.update(TABLE_FUEL_REFILL, values, _ID + " = ? and " + SCAN_CODE + " = ? ", new String[]{String.valueOf(id), scan_code});

        return true;
    }

    // Delete all chats in the database
    public void deletePillsNoRecords(int r_id, String vehicleId, String timeOfRefill) {
        try {
            // Order of deletions is important when foreign key relationships exist.
            String deleteQuery = "DELETE FROM " + TABLE_FUEL_REFILL + " WHERE " + _ID +
                    "='" + r_id + "' AND " + VEHICLE_ID +
                    "='" + vehicleId + "'" + " AND " + TIME_OF_REFILL + "='" + timeOfRefill + "'";

            getWritableDatabase().execSQL(deleteQuery);
        } catch (Exception e) {
            Log.e("LLLLL_DB_DELETE", "deleteAllChats: " + e.getMessage());
        }
    }
}
