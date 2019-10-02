package com.example.blani.secondbreakfast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.blani.secondbreakfast.Food;

/**
 * Created by Blani! on 26.05.2018.
 */

public class MyDBHandler extends SQLiteOpenHelper {

    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "secondBreakfast";
    public static final String TABLE_NAME = "products";
    private static final String UID="_id";
    public static final String COLUMN_NAME = "type";
    public static final String COLUMN_NAME2 = "quantity";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +UID+  " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_NAME2 + " INTEGER" + ");";
    private Context context;

    //initialize the database
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (Exception e){

        }

     }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public String showProducts() {

        String result = "";
        String query = "Select*FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {

            String result_1 = cursor.getString(1);
            int result_2 = cursor.getInt(2);
            result += result_1 + " " + String.valueOf(result_2) +
                    System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        return result;
    }

    public void addProduct(String type, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, type);
        values.put(COLUMN_NAME2, quantity);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Food findProductQuantity(String foodType) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = " + "'" + foodType + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Food product = new Food();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product.setType(cursor.getString(1));
            product.setQuantity(cursor.getInt(2));
            cursor.close();
        } else {
            product = null;
        }

        db.close();
        return product;
    }

    public boolean updateProductQuantity(String type, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME2, quantity);
        return db.update(TABLE_NAME, values, COLUMN_NAME + "='" + type + "'", null) > 0;
    }

    public boolean isTableEmpty(){
        boolean empty = true;
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        int countNo = cursor.getInt(0);
        if(countNo>0){
            empty = false;
        }
    return empty;
    }
}
