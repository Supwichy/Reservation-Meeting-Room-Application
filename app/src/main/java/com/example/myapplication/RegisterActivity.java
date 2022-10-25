package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    int action;
    String dupUser;
    DBHelper dbh;
    TextView inputName, inputSurname,inputUsername, inputPassword, inputRePassword;
    String name, surname,username, password, rePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_Yellow))); //เปลี่ยนสี Table App

        inputName = findViewById(R.id.inputName);
        inputSurname = findViewById(R.id.inputSurname);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        inputRePassword = findViewById(R.id.inputRepassword);

        dbh = new DBHelper(this);

    }
    public void submitReg (View view){

        name = inputName.getText().toString();
        surname = inputSurname.getText().toString();
        username = inputUsername.getText().toString();
        password = inputPassword.getText().toString();
        rePassword = inputRePassword.getText().toString();

        // เช็ค name && username not ค่าว่าง && surname
        if (!name.equals("") && !username.equals("") && !surname.equals("")) {
            // เช็ค password && rePassword not ค่าว่าง
            if (!password.equals("") && !rePassword.equals("")) {
                //แช็ค password && rePassword ตรงกันไหม
                if (password.equals(rePassword)) {

                    // ส่งข้อมูล
                    /*Intent regSuccess = new Intent(this, RegisterSuccess.class);
                    regSuccess.putExtra("name",name);
                    regSuccess.putExtra("username",username);
                    regSuccess.putExtra("password",password);
                    startActivity(regSuccess);*/

                    // ตรวจสอบ Username ซ้ำกับที่มีอยู่
                    Cursor retData = dbh.getMemberData();
                    if (retData.getCount()!=0){
                        while (retData.moveToNext()){

                            // ถ้า username ซ้ำ action = 0 เก็บค่า username ที่ซ้ำไปที่ dupUser และ จบการทำงาน loop
                            if (username.equals(retData.getString(3))){
                                action = 0;
                                dupUser = retData.getString(3);
                                break;
                                // ถ้า username ไม่ซ้ำ action = 1
                            } else { action = 1; }
                        }

                        // ต้อง action = 1 ถึงจะส่งข้อมูล ถ้าไม่ ให้แสดง username ที่ซ้ำ ด้วยตัวแปร dupUser
                        if (action==1){ addMemData(name, surname, username, password);
                        } else { showInfo("ลงทะเบียนไม่สำเร็จ", "ชื่อผู้ใช้ "+dupUser+" ถูกใช้งานไปแล้ว"); }

                    } else { // ถ้าไม่มีข้อมูลในตาราง ให้เพิ่มข้อมูลได้เลย
                        addMemData(name, surname, username, password);

                    }

                } else { Toast.makeText(RegisterActivity.this, "รหัสผ่านไม่ตรงกัน", Toast.LENGTH_SHORT).show(); }
            } else { Toast.makeText(RegisterActivity.this,"กรุป้อนรหัสผ่านให้ครบถ้วน", Toast.LENGTH_SHORT).show(); }
        } else { Toast.makeText(RegisterActivity.this,"กรุป้อนชื่อผู้ใช้ให้ครบถ้วน", Toast.LENGTH_SHORT).show(); }
    }

    // กลับหน้า Login
    public void clickBackToLogin(View view){
        finish();
    }

    // Method เพิ่มข้อมูล และแสดงข้อความ ถ้าสามารถเพิ่มข้อมูลได้
    private void addMemData(String name, String surname,String username, String password){
        boolean success = dbh.addMemberData(name, surname, username, password);
        if (success){
            showInfo("ลงทะเบียนเสร็จสิ้น!", "รายละเอียดการลงทะเบียน \nชื่อ : "+name+" "+surname+"\nชื่อผู้ใช้ : "+username+"\nรหัสผ่าน : "+password);
        }
    }

    private void showInfo(String title, String msg){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(true);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.show();
    }

}