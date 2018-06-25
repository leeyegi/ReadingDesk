package com.example.yegilee.readingdesk;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SM_Database {

    DatabaseHelper mHelper;

    //database name
    private static final String DATABASE_NAME = "ReadingDesk.db";

    Context mContext=null;

    //table 1 name and field
    private static final String DATABASE_TABLE_NAME = "readingdesk";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_HHMMSS = "hhmmss";
    public static final String COLUMN_NAME_TIME="time";
    public static final String COLUMN_NAME_DATE = "date";


    public SM_Database(Context context)
    {
        mHelper = new DatabaseHelper(context);
    }

    SimpleDateFormat day;
    String date_val;
    SimpleDateFormat time;
    String time_val;


    public void currentDataTime(){
        // Gets the current system time in milliseconds
        long time_tmp = System.currentTimeMillis();
         day = new SimpleDateFormat("yy-MM-dd");
         date_val = day.format(new Date(time_tmp));

         time = new SimpleDateFormat("hh:mm:ss");
         time_val = time.format(new Date(time_tmp));
    }

    /*
     * Insert new record to database
     */
    public void INSERT(String hhmmss)
    {
        currentDataTime();

        Log.e("Insert",hhmmss);
        Log.e("Insert",time_val);
        Log.e("Insert",date_val);

        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO ").append(DATABASE_TABLE_NAME).append(" VALUES ");
        sb.append("(").append("null").append(", ");
        sb.append("'").append(hhmmss).append("', ");
        sb.append("'").append(date_val).append("', ");
        sb.append("'").append(time_val).append("'");
        sb.append("); ");

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(sb.toString());

        mHelper.close();

    }


    /*
     * Update existed record in database
     */
    /*
    public void UPDATE(int id, String note)
    {
        // Gets the current system time in milliseconds
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE ").append(DATABASE_TABLE_NAME).append(" SET ");
        sb.append(COLUMN_NAME_NOTE).append("=");
        sb.append("'").append(note).append("' ");
        sb.append("WHERE id=").append(id);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(sb.toString());
        mHelper.close();
    }*/

    /*
     * Delete existed record in database
     */
    /*
    public void DELETE(int id)
    {
        try
        {
            StringBuffer sb = new StringBuffer();
            sb.append("DELETE FROM ").append(DATABASE_TABLE_NAME).append(" WHERE ");
            sb.append(COLUMN_NAME_ID).append("=").append(id).append(";");
            SQLiteDatabase db = mHelper.getWritableDatabase();
            db.execSQL(sb.toString());
            mHelper.close();
        }
        catch(Exception ex)
        {
        }
    }*/

    /*
     * Query to get list of note in database
     */
    public ArrayList<ReadingDesk> query(String currentdate) {
        ArrayList<ReadingDesk> list = new ArrayList<ReadingDesk>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor;

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(DATABASE_TABLE_NAME).append(" WHERE date='").append(currentdate).append("' ORDER BY id DESC");
       // sb.append("SELECT * FROM ").append(DATABASE_TABLE_NAME).append(" ORDER BY id DESC");

        cursor = db.rawQuery(sb.toString(), null);

        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String hhmmss = cursor.getString(1);
            String date=cursor.getString(2);
            String time = cursor.getString(3);

            list.add(new ReadingDesk(Integer.parseInt(id), hhmmss, date, time));
        }

        cursor.close();
        mHelper.close();

        return list;
    }

    /*
     * Clear all data in table if it's existed
     */
    /*
    public void DROP()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM ").append(DATABASE_TABLE_NAME).append(";");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(sb.toString());
        mHelper.close();
    }
    */

    class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        // Declare table with 4 column: (id - int auto increment, title - String , note - String, date - integer)
        public void onCreate(SQLiteDatabase db) {


            db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_NAME + " ("
                    + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME_HHMMSS + " TEXT,"
                    + COLUMN_NAME_DATE + " TEXT,"
                    +COLUMN_NAME_TIME+ " TEXT"
                    + ");");


        }


        // Drop table if it's existed
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            StringBuffer sb = new StringBuffer();
            sb.append("DROP TABLE IF EXISTS ").append(DATABASE_NAME).append("");

            db.execSQL(sb.toString());
            onCreate(db);
        }
    }

}