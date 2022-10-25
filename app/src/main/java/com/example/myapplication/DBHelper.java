package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.dynamicanimation.animation.SpringAnimation;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_name = "ReserveSystem.db";
    public static final String table_member = "Member";
    public static final String table_reserve = "ReserveList";

    // ตาราง Member
    public static final String idMember = "memberID"; // PK
    public static final String nameMember = "name";
    public static final String surnameMember = "surname";
    public static final String usernameMember = "username";
    public static final String passwordMember = "password";

    // ตาราง RserveList
    public static final String idReserve = "reserveID"; // PK
    public static final String dayReserve = "day";
    public static final String monthReserve = "month";
    public static final String yearReserve = "year";
    public static final String timeReserve = "time";
    public static final String idMemberReserve = "memberID"; // FK

    public DBHelper(@Nullable Context context) { super(context, DB_name, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // สร้างตาราง Member
        db.execSQL(
                "CREATE TABLE "+table_member+" ("+
                idMember+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                nameMember+" TEXT, "+
                surnameMember+" TEXT, "+
                usernameMember+" TEXT,"+
                passwordMember+" TEXT)");

        // สร้างตาราง ReserveList
        db.execSQL("CREATE TABLE "+table_reserve+" ("+
                idReserve+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                dayReserve+" INTEGER, "+
                monthReserve+" INTEGER, "+
                yearReserve+" INTEGER, "+
                timeReserve+" TEXT, "+
                idMemberReserve+" INTEGER REFERENCES "+table_member+" ("+idMember+") )");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Member"+table_member);
        db.execSQL("DROP TABLE IF EXISTS ReserveList"+table_reserve);
        onCreate(db);
    }


    // เพิ่มข้อมูลเข้าตาราง Member
    public boolean addMemberData (String name,String surname, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues conVal = new ContentValues();
        conVal.put(nameMember,name);
        conVal.put(surnameMember,surname);
        conVal.put(usernameMember,username);
        conVal.put(passwordMember,password);
        long result = db.insert(table_member,null,conVal);
        if(result==0){
            return false;
        } else {
            return true;
        }
    }

    // เรียกข้อมูลทั้งหมด จากตาราง Member
    public Cursor getMemberData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor retData = db.rawQuery("SELECT * FROM "+table_member, null);
        return retData;
    }

    // เรียกข้อมูล จากตาราง Member แต่!!! โดยเงื่อนไข username ต้องเท่ากับ ข้อมูลที่ป้อนเข้ามา
    public Cursor getMemberAuthData(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor retData = db.rawQuery("SELECT "+
                usernameMember+", "+
                passwordMember+", "+
                nameMember+", "+
                surnameMember+", "+
                idMember+
                " FROM "+table_member+" WHERE username = ?",new String[]{username});
        return retData;
    }
 
    // เพิ่มข้อมูลเข้า ตาราง ReserveList
    public boolean addReserveData(int day, int month, int year, String time, int memberID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues conVal = new ContentValues();
        conVal.put(dayReserve,day);
        conVal.put(monthReserve,month);
        conVal.put(yearReserve,year);
        conVal.put(timeReserve,time);
        conVal.put(idMemberReserve,memberID);
        long result = db.insert(table_reserve,null,conVal);
        if(result==0){
            return false;
        } else {
            return true;
        }
    }

    // เรียกข้อมูลจาก ตาราง Reserve เชื่อม FK จาก ตาราง Member
    // ด้วยเงื่อนไข memberID จาก ReserveList ต้องตรงกับ memberID จาก ตาราง Member
    // SELECT ตาราง1.คอลั่ม, ตาราง2.คอลั่ม FROM ตาราง1 JOIN ตาราง2 ON ตาราง1.FK = ตาราง2.FK
    public Cursor getReserveData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor retData = db.rawQuery("SELECT "+
                dayReserve+", "+
                monthReserve+", "+
                yearReserve+", "+
                timeReserve+", "+
                table_member+"."+nameMember+", "+
                table_member+"."+surnameMember+
                " FROM "+table_reserve+" JOIN "+table_member+
                " ON "+table_reserve+"."+idMemberReserve+" = "+table_member+"."+idMember, null);
        return retData;
    }

    // แสดงข้อมูล โดยรับค่า ชื่อ ตาราง เข้ามา
    public Cursor getAllData(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor retData = db.rawQuery("SELECT * FROM "+tableName,null );
        return retData;
    }

    // แสดงข้อมูล เงื่อนไข ID
    // (ชื่อตาราง, ชื่อคอลั่ม, ID)
    public Cursor getAllDataWithID(String tableName, String colID, String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor retData = db.rawQuery("SELECT * FROM "+tableName+" WHERE "+colID+"="+ID, null);
        return retData;
    }

    // อัพเดทข้อมูล ตาราง Member เงื่อนไข ID
    public boolean updateMemberData(String name,String surname, String username, String password, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues conVal = new ContentValues();
        conVal.put(nameMember,name);
        conVal.put(surnameMember,surname);
        conVal.put(usernameMember,username);
        conVal.put(passwordMember,password);
        long result = db.update(table_member, conVal,"memberID = ?", new String[]{id});
        if (result==0){
            return false;
        } else {
            return true;
        }
    }

    // อัพเดทข้อมูล ตาราง ReserveList เงื่อนไข ID
    public boolean updateReserveData(String day,String month, String year, String time, Integer memberID, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        // ContentValues เก็บค่าคล้ายๆกับ StringBuffer
        ContentValues conVal = new ContentValues();
        conVal.put(dayReserve,day);
        conVal.put(monthReserve,month);
        conVal.put(yearReserve,year);
        conVal.put(timeReserve,time);
        conVal.put(idMemberReserve,memberID);
        long result = db.update(table_reserve, conVal,"reserveID = ?", new String[]{id});
        if (result==0){
            return false;
        } else {
            return true;
        }
    }

    // ลบข้อมูล เงื่อนไข ID และชื่อตาราง
    public boolean deleteData(String tableName, String colID,String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(tableName, colID+" = ?", new String[]{id});
        if (result==0){
            return false;
        } else {
            return true;
        }
    }

}
