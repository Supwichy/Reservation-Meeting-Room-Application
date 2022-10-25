package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    DBHelper dbh;
    String getMemberID, reUsername, reName, reSurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_Yellow))); //เปลี่ยนสี Table App

        dbh = new DBHelper(this);

        // รับค่าจากหน้า Login id,username,name,surname
        Intent reUser = getIntent();
        getMemberID = reUser.getStringExtra("memberID");
        reUsername = reUser.getStringExtra("username");
        reName = reUser.getStringExtra("name");
        reSurname = reUser.getStringExtra("surname");

        // แสดงรายละเอียดของผู้ล็อคอิน
        showInfo("ยินดีต้อนรับ","คุณ "+reName+" "+reSurname+"\nชื่อผู้ใช้ "+reUsername);

    }

    // ไปหน้าจอง
    public void clickReserve (View view){
        Intent reserve = new Intent(this,ReserveActivity.class);
        reserve.putExtra("memberID",getMemberID);
        startActivity(reserve);
    }

    // แสดงรายการจอง
    public void clickShowReserveList (View view){

        Cursor retData = dbh.getReserveData();
        if (retData.getCount()==0){
            showInfo("แจ้งเตือน!","ไม่พบข้อมูลการจองห้องในฐานข้อมูล");
        } else {
            // StringBuffer เก็บค่า String แบบ ต่อกันได้
            StringBuffer dataBuff = new StringBuffer();

            // loop หยิบข้อมูล
            // .getString(เลขคอลั่ม)
            while (retData.moveToNext()){
                dataBuff.append("วันที่ : "+retData.getString(0)+"\n");
                dataBuff.append("เดือน : "+retData.getString(1)+"\n");
                dataBuff.append("ปี : "+retData.getString(2)+"\n");
                dataBuff.append("เวลา : "+retData.getString(3)+"\n");
                dataBuff.append("ผู้จอง : "+retData.getString(4)+" "+retData.getString(5)+"\n");
                dataBuff.append("________________________"+"\n");
            }
            showInfo("รายการจองห้องประชุม",dataBuff.toString());
        }
    }

    // ออกจากระบบ
    public void clickLogout (View view){
        finish();
    }

    private void showInfo(String title, String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(true);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.show();
    }
}