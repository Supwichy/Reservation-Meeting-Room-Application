package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    DBHelper dbh;
    EditText inputUsername,inputPassword;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_Yellow))); //เปลี่ยนสี Table App

        dbh = new DBHelper(this);
        inputUsername = findViewById(R.id.edtInputUsername);
        inputPassword = findViewById(R.id.edtInputPassword);

    }

    public void login (View view){

        username = inputUsername.getText().toString();
        password = inputPassword.getText().toString();

        // เข้าหน้า Admin
        String admin = "admin";
        if (username.equals(admin) && password.equals(admin)){
            Intent adminPage = new Intent(this,AdminActivity.class);
            startActivity(adminPage);
            finish();
        }

        // username && password != ค่าว่าง
        if (!username.equals("")&&!password.equals("")){

            Cursor retData = dbh.getMemberAuthData(username);
            // เช็ค username แล้วไม่ตรงกับอันไหนเลย
            if (retData.getCount()==0){
                showInfo("ไม่พบชื่อผู้ใช้ในฐานข้อมูล","หากยังไม่มีบัญชีผู้ใช้ โปรดทำการลงทะเบียน");
            } else {
                while (retData.moveToNext()){
                    // เช็ค username ตรงกับ ฐานข้อมูลไหม
                    // 0 = username
                    // 1 = password
                    // 2 = name
                    // 3 = surname
                    // 4 = ID
                    if (username.equals(retData.getString(0))){
                        // เช็ค password
                        if (password.equals(retData.getString(1))){
                            Intent login = new Intent(this,MainMenuActivity.class);
                            login.putExtra("username",retData.getString(0));
                            login.putExtra("name",retData.getString(2));
                            login.putExtra("surname",retData.getString(3));
                            login.putExtra("memberID",retData.getString(4));
                            startActivity(login);
                            break;
                        } else {
                            // error เมื่อ username ตรง แต่ password ไม่ตรง
                            Toast.makeText(LoginActivity.this,"รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        } else {
            // error เมื่อ username กับ password เป็นค่าว่าง
            Toast.makeText(LoginActivity.this,"กรุณาป้อนชื่อผู้ใช้และรหัสผ่าน", Toast.LENGTH_SHORT).show();
        }
    }

    // ไปหน้า Register
    public void register(View view){
        Intent register = new Intent(this,RegisterActivity.class);
        startActivity(register);
    }

    private void showInfo(String title, String msg){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(true);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.show();
    }
}