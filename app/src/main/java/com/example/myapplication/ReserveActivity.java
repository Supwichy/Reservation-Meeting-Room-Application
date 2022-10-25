package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;


public class ReserveActivity extends AppCompatActivity {

    DBHelper dbh;
    CalendarView reserveDate;
    ChipGroup groupTime;
    Chip selectTime;
    String time, getMemberID;
    int dateDay, dateMonth, dateYear;
    int action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_Yellow))); //เปลี่ยนสี Table App

        dbh = new DBHelper(this);
        reserveDate = findViewById(R.id.reserveCalendar);
        groupTime = findViewById(R.id.groupSelectTime);

        // รับข้อมูล ID จาก หน้า เมนู ที่ส่งมาจากหน้า Login
        Intent getID = getIntent();
        getMemberID = getID.getStringExtra("memberID");

        // เช็คการกดบน Calendar
        reserveDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                // i2 = วัน
                // i1 = เดือน-1 (ถอยหลังไป 1 เดือน)
                // i = ปี
                dateDay = i2;
                dateMonth = (i1+1);
                dateYear = (i);
            }
        });

        // เช็คการกดที่ ChipGroup
        groupTime.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, int checkedId) {
                checkedId = groupTime.getCheckedChipId();
                selectTime = findViewById(checkedId);
                time = selectTime.getText().toString();
            }
        });

    }

    // ปุ่มยืนยันการจอง
    public void clickSubmitReserve (View view){

        // เช็ควันที่ต้องไม่ = 0 กัน error
        if (dateYear != 0) {

            // เช็คเวลา ไม่เป็นค่าว่าง (ต้องเลือกเวลาก่อน)
            if (selectTime != null) {

                // ก่อนจะส่ง ข้อมูล ดึงข้อมูลการจอง มาเช็คก่อน ว่าซ้ำกันไหม
                Cursor retData = dbh.getReserveData();
                // 0 = day
                // 1 = month
                // 2 = year
                // 3 = time
                // 4 = member.name
                // 5 = member.surname
                if (retData.getCount()==0) {
                    action = 1;
                } else {
                    while (retData.moveToNext()) {
                        // เช็ควันที่ตรงกับฐานข้อมูล
                        if (dateDay==retData.getInt(0) && dateMonth==retData.getInt(1) && dateYear==retData.getInt(2)) {
                            // ถ้า วัน เดือน ปี ตรงกับฐานข้อมูล ให้เช็คเวลา
                            if (time.equals(retData.getString(3))) {
                                action = 0;
                                break;
                            } else { // วัน เดือน ปี ตรงกับฐานข้อมูล แต่!!!!เวลาไม่ตรง ให้เพิ่มข้อมูลได้
                                action = 1;
                            }
                        } else { // วัน เดือน ปี ไม่ตรงกับฐานข้อมูล ให้เพิ่มข้อมูลได้
                            action = 1;
                        }
                    }
                }

                // action 1 คือเพิ่มข้อมูล 0 คือแจ้ง error
                if (action==1){
                    addReserveData(dateDay, dateMonth, dateYear, time, Integer.parseInt(getMemberID));
                }
                else if (action==0){
                    submitError(dateDay, dateMonth, dateYear, selectTime);
                }

                // แสดงข้อผิดพลาด ถ้าไม่เลือกเวลา
            } else {
                Toast.makeText(ReserveActivity.this, "กรุณาเลือกเวลา", Toast.LENGTH_SHORT).show();
            }
            // แสดงข้อผิดพลาด ถ้าไม่เลือกวันที่
        } else {
            Toast.makeText(ReserveActivity.this,"กรุณาเลือกวันที่", Toast.LENGTH_SHORT).show();
        }
    }

    // ปุ่มกลับหน้ารายการ
    public void clickBackToMenu(View view){
        finish();
    }

    // Method แสดงข้อผิดพลาด เมื่อวันที่และเวลาจองซ้ำกัน
    // submitError(dateDay,dateMonth,dateYear,selectTime);
    private void submitError(int dateDay,int dateMonth,int dateYear, Chip selectTime){
        String title = "วันและเวลาดังกล่าวมีผู้อื่นจองแล้ว";
        String msg = "วันที่ "+dateDay+"/"+dateMonth+"/"+dateYear+"\nเวลา "+selectTime.getText().toString()+"\nโปรดเลือกวันหรือเวลาใหม่";
        showInfo(title,msg); // เรียกใช้ Method แสดงผลแบบ Dialog
    }

    // Method เพิ่มข้อมูล to database
    private void addReserveData(int day, int month, int year, String time, int memberID){
        boolean success = dbh.addReserveData(day, month, year, time, memberID);
        if (success){
            showInfo("จองห้องประชุมเสร็จสิ้น!", "รายละเอียดการจองห้องประชุม \nวันที่ "+day+"\nเดือน "+month+"\nปี "+year+"\nเวลา "+time);
        }
    }

    // Method แสดงผลแบบ Dialog
    private void showInfo(String title, String msg){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(true);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.show();
    }

}
