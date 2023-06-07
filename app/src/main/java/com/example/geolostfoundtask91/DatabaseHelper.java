package com.example.geolostfoundtask91;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    static final String DATABASE_NAME = "GEOLostFoundDBTask91";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_Table = "Items";
    static final String item_ID = "ID";
    static final String item_Type = "Type";
    static final String item_Name = "Name";
    static final String item_Phone = "Phone";
    static final String item_Description = "Description";
    static final String item_Date = "Date";
    static final String item_Location = "Location";

    public DatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Create the Database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table Items(" +
                "ID INTEGER primary key, " +
                "item_Type TEXT NOT NULL, " +
                "item_Name TEXT NOT NULL," +
                "item_Phone TEXT NOT NULL," +
                "item_Description TEXT NOT NULL," +
                "item_Date TEXT NOT NULL," +
                "item_Location TEXT NOT NULL," +
                "item_Latlng TEXT NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int ii) {
        db.execSQL("drop Table if exists Items");
    }

    // This method is used to add the user input to the database
    public Boolean insertData(String item_Type, String item_Name, String item_Phone, String item_Description, String item_Date, String item_Location, String item_Latlng){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put("item_ID", item_ID);
        contentValues.put("item_Type", item_Type);
        contentValues.put("item_Name", item_Name);
        contentValues.put("item_Phone", item_Phone);
        contentValues.put("item_Description", item_Description);
        contentValues.put("item_Date", item_Date);
        contentValues.put("item_Location", item_Location);
        contentValues.put("item_Latlng", item_Latlng);
        long result = db.insert("Items", null, contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // This method returns data from the database
    public Cursor getdata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Items", null);
        return cursor;
    }

    // This method is called when deleting an item from the database, it matches on the ID of an item which is the primary key
    void deleteItem(String selectedID){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("Items", selectedID + "=" + item_ID, null) ;
        if(result == -1)
        {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
