package com.example.audiosensor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME="saved_recording01.db";
    public static final int DATABASE_VERSION=1;
    public static final String TABLE_NAME="saved_recording_table";

    public static final String COULUM_NAME="name";
    public static final String COULUM_PATH="path";
    public static final String COULUM_LENGTH="length";
    public static final String COULUM_TIME_ADDED="time_added";
    private static final String COMA_SEP=",";


    private static final String SQLITE_CREATE_TABLE= "CREATE TABLE "+TABLE_NAME+" ("+"id INTEGER PRIMARY KEY"+
            " AUTOINCREMENT"+COMA_SEP+COULUM_NAME+" TEXT"+COMA_SEP+
            COULUM_PATH+" TEXT"+COMA_SEP+
            COULUM_LENGTH+" TEXT"+COMA_SEP+
            COULUM_TIME_ADDED+" INTEGER"+")";
    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
    }

    public boolean addRecording(RecordingItem reordingItem)
    {
        try{
            SQLiteDatabase db=getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(COULUM_NAME,reordingItem.getName());
            contentValues.put(COULUM_PATH,reordingItem.getPath());
            contentValues.put(COULUM_LENGTH,reordingItem.getLength());
            contentValues.put(COULUM_TIME_ADDED,reordingItem.getTime_added());

            db.insert(TABLE_NAME,null,contentValues);
            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLITE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

    }
    public ArrayList<RecordingItem> getAllAudio(){
        ArrayList<RecordingItem> arrayList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.rawQuery("SELECT* FROM "+TABLE_NAME,null);

        if(cursor!=null)
        {
            while(cursor.moveToNext())
            {

                String name=cursor.getString(1);
                String path=cursor.getString(2);
                int length=(int) cursor.getLong(3);
                long timeAdded=cursor.getLong(4);
                RecordingItem recordingItem=new RecordingItem(name,path,length,timeAdded);

                arrayList.add(recordingItem);
            }
            cursor.close();
            return  arrayList;

        }

        return null;
    }



}
