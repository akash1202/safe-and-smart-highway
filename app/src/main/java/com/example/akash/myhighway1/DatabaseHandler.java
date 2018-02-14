package com.example.akash.myhighway1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.akash.myhighway1.Friends.MyContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishal on 2/11/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {   // DATABASE for handle Contact of friends
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="ContactManager";
    public static final String TABLE_NAME="Contacts";

    //all column of table
    public String KEY_NAME="name";
    public String KEY_NUMBER="number";
    public String KEY_IMAGE="image";

    public DatabaseHandler(Context context){                //Constructor for create DatabaseHandler
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_QUARY="CREATE TABLE "+TABLE_NAME+"("+KEY_NAME+" TEXT,"
                +KEY_NUMBER+" TEXT UNIQUE,"+KEY_IMAGE+" TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE_QUARY);  //created Table for contacts in sqlite database
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop Table if exists
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        //create Table again
        onCreate(sqLiteDatabase);
    }

    public void addContact(MyContact Contact){      //for add new contact in database
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",Contact.getName());
        values.put("number",Contact.getNumber());
        values.put("image",Contact.getImage());
        db.insert(TABLE_NAME,null,values);          //adding new Row in database table
        db.close();
    }
    public MyContact getContact(String number){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,new String[]{KEY_NAME,KEY_NUMBER,KEY_IMAGE},KEY_NUMBER+"=?"
                ,new String[]{number},null,null,null,null);
        //return Contact
        return  new MyContact(cursor.getString(0),cursor.getString(1),cursor.getString(2));
    }
    public List<MyContact> getAllContacts(){
        List<MyContact> ContactList=new ArrayList<>();      //created list of contact

        String SELECT_QUERY="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(SELECT_QUERY,null);       //without any argument

        if(cursor.moveToFirst()){
            do{
                MyContact TempContact=new MyContact(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                ContactList.add(TempContact);
            }while (cursor.moveToNext());
        }
        return ContactList;
    }
    public int getContactsCount(){
        String COUNT_QUERY="SELECT * FROM "+TABLE_NAME;  //query for Retrieve All Contacts
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(COUNT_QUERY,null);
        int NO_OF_CONTACT=cursor.getCount();
        cursor.close();
        return  NO_OF_CONTACT;
    }
    public void deleteContact(MyContact contact){
        //deleting single row with contact
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_NUMBER+"=?",new String[]{contact.getNumber()});
        db.close();
    }

}
