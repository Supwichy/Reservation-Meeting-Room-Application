package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;

public class AdminActivity extends AppCompatActivity {

    DBHelper dbh;
    TextView txtID, txt1, txt2, txt3, txt4, txt5;
    EditText inputID, input1, input2, input3, input4, input5;
    RadioGroup groupSelectTable;
    RadioButton selectTable;
    String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange))); //เปลี่ยนสี Table App

        dbh = new DBHelper(this);

        inputID = findViewById(R.id.edtInputID);
        input1 = findViewById(R.id.edtInput1);
        input2 = findViewById(R.id.edtInput2);
        input3 = findViewById(R.id.edtInput3);
        input4 = findViewById(R.id.edtInput4);
        input5 = findViewById(R.id.edtInput5);

        txtID = findViewById(R.id.textViewID);
        txt1 = findViewById(R.id.textView1);
        txt2 = findViewById(R.id.textView2);
        txt3 = findViewById(R.id.textView3);
        txt4 = findViewById(R.id.textView4);
        txt5 = findViewById(R.id.textView5);

        groupSelectTable = findViewById(R.id.groupSelectTable);

        // เช็ค การกดปุ่ม Radio
        groupSelectTable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                i = groupSelectTable.getCheckedRadioButtonId();
                selectTable = findViewById(i);

                // ถ้าเลือก Radio "ตาราง Member" ให้ ดึงข้อมูลจากตาราง Member
                // แล้ว ทำการนำชื่อคอลลั่มมาใส่ TextView และ EditText (hint)
                // EditText ช่อง 5 ทำให้ใส่ข้อมูลไม่ได้
                if (selectTable.getText().toString().equals("ตาราง Member")){
                    tableName = "Member";
                    Cursor retData = dbh.getAllData(tableName);
                    while (retData.moveToNext()){
                        txtID.setText(retData.getColumnName(0));
                        txt1.setText(retData.getColumnName(1));
                        txt2.setText(retData.getColumnName(2));
                        txt3.setText(retData.getColumnName(3));
                        txt4.setText(retData.getColumnName(4));
                        txt5.setText("");

                        inputID.setHint(retData.getColumnName(0));
                        input1.setHint(retData.getColumnName(1));
                        input2.setHint(retData.getColumnName(2));
                        input3.setHint(retData.getColumnName(3));
                        input4.setHint(retData.getColumnName(4));
                        input5.setHint("");
                        input5.setEnabled(false);

                        clearText();

                    }

                    // ถ้าเลือก Radio "ตาราง ReserveList" ให้ ดึงข้อมูลจากตาราง ReserveList
                    // แล้ว ทำการนำชื่อคอลลั่มมาใส่ TextView และ EditText (hint)
                    // EditText ช่อง 5 กลับมาใส่ข้อมูลได้ตามปกติ
                } else if (selectTable.getText().toString().equals("ตาราง ReserveList")){
                    tableName = "ReserveList";
                    Cursor retData = dbh.getAllData(tableName);
                    while (retData.moveToNext()){
                        txtID.setText(retData.getColumnName(0));
                        txt1.setText(retData.getColumnName(1));
                        txt2.setText(retData.getColumnName(2));
                        txt3.setText(retData.getColumnName(3));
                        txt4.setText(retData.getColumnName(4));
                        txt5.setText(retData.getColumnName(5));

                        inputID.setHint(retData.getColumnName(0));
                        input1.setHint(retData.getColumnName(1));
                        input2.setHint(retData.getColumnName(2));
                        input3.setHint(retData.getColumnName(3));
                        input4.setHint(retData.getColumnName(4));
                        input5.setHint(retData.getColumnName(5));
                        input5.setEnabled(true);

                        clearText();
                    }

                }
            }
        });
    }

    // กลับหน้าหลัก
    public void clickBackHome (View view){
        finish();
    }

    // เพิ่มข้อมูล
    public void clickAddData (View view){
        if (checkChooseTable()){
            if (checkFillInput(tableName)){
                if (tableName.equals("Member")){
                    boolean addData = dbh.addMemberData(input1.getText().toString(), input2.getText().toString(), input3.getText().toString(), input4.getText().toString());
                    showAddDataInfo(addData,tableName);
                } else if (tableName.equals("ReserveList")){
                    int intInput1 = Integer.parseInt(input1.getText().toString());
                    int intInput2 = Integer.parseInt(input2.getText().toString());
                    int intInput3 = Integer.parseInt(input3.getText().toString());
                    int intInput5 = Integer.parseInt(input5.getText().toString());
                    boolean addData= dbh.addReserveData(intInput1, intInput2, intInput3, input4.getText().toString(), intInput5);
                    showAddDataInfo(addData,tableName);
                }
            }
        }
    }

    // ปุ่ม ลบข้อมูล เงื่อนไข ID
    public void clickDelData (View view){
        if (checkChooseTable()){
            if (tableName.equals("Member")){
                boolean delData = dbh.deleteData(tableName, "memberID", inputID.getText().toString());
                showDelDataInfo(delData);
            }
            if (tableName.equals("ReserveList")){
                boolean delData = dbh.deleteData(tableName, "reserveID", inputID.getText().toString());
                showDelDataInfo(delData);
            }
        }
    }

    // ปุ่ม อัปเดตข้อมูล เงื่อนไข ID
    public void clickUpData (View view){
        if (checkChooseTable()){

            // เช็คให้กรอกข้อมูลให้ครบ
            if (checkFillInput(tableName)){

                if (tableName.equals("Member")){
                    boolean upData = dbh.updateMemberData(input1.getText().toString(), input2.getText().toString(), input3.getText().toString(), input4.getText().toString(), inputID.getText().toString());
                    showUpDataInfo(upData);
                } else if (tableName.equals("ReserveList")) {
                    int intInput5 = Integer.parseInt(input5.getText().toString() );
                    boolean upData = dbh.updateReserveData(input1.getText().toString(), input2.getText().toString(), input3.getText().toString(), input4.getText().toString(), intInput5,inputID.getText().toString());
                    showUpDataInfo(upData);
                }
            }
        }
    }

    // ปุ่ม แสดงข้อมูล
    public void clickGetAllData(View view){

        if (checkChooseTable()){

            Cursor retData = dbh.getAllData(tableName);
            StringBuffer dataBuff = new StringBuffer();
            if (tableName.equals("Member")){
                while (retData.moveToNext()){
                    dataBuff.append("memberID : "+retData.getString(0)+"\n");
                    dataBuff.append("name : "+retData.getString(1)+"\n");
                    dataBuff.append("surname : "+retData.getString(2)+"\n");
                    dataBuff.append("username : "+retData.getString(3)+"\n");
                    dataBuff.append("password : "+retData.getString(4)+"\n");
                    dataBuff.append("_________________________________\n\n");
                }
            } else if (tableName.equals("ReserveList")) {
                while (retData.moveToNext()){
                    dataBuff.append("reserveID : "+retData.getString(0)+"\n");
                    dataBuff.append("day : "+retData.getString(1)+"\n");
                    dataBuff.append("month : "+retData.getString(2)+"\n");
                    dataBuff.append("years : "+retData.getString(3)+"\n");
                    dataBuff.append("time : "+retData.getString(4)+"\n");
                    dataBuff.append("memberID : "+retData.getString(5)+"\n");
                    dataBuff.append("_________________________________\n\n");

                }
            }
            showInfo("ข้อมูลในตาราง "+tableName,dataBuff.toString());
        }

    }

    // ปุ่ม ดึงข้อมูล จาก ID
    public void clickFetchAllData(View view){

        if (checkChooseTable()) {

            // เช็คช่อง ID ต้องไม่เป็นว่าง
            if (!inputID.getText().toString().equals("")){
                if (tableName.equals("Member")){
                    Cursor retData = dbh.getAllDataWithID(tableName,"memberID",inputID.getText().toString());
                    if (retData.getCount()==0){
                        showInfo("ดึงข้อมูล : Error","ไม่พบ memberID ดังกล่าว");
                        clearText();
                    } else {
                        while (retData.moveToNext()) {
                            input1.setText(retData.getString(1));
                            input2.setText(retData.getString(2));
                            input3.setText(retData.getString(3));
                            input4.setText(retData.getString(4));
                        }
                    }
                } else if (tableName.equals("ReserveList")) {
                    Cursor retData = dbh.getAllDataWithID(tableName,"reserveID",inputID.getText().toString());
                    if (retData.getCount()==0){
                        showInfo("ดึงข้อมูล : Error","ไม่พบ reserveID ดังกล่าว");
                        clearText();
                    } else {
                        while (retData.moveToNext()) {
                            input1.setText(retData.getString(1));
                            input2.setText(retData.getString(2));
                            input3.setText(retData.getString(3));
                            input4.setText(retData.getString(4));
                            input5.setText(retData.getString(5));
                        }
                    }
                }
            } else { // Error ถ้าไม่ใส่ ID
                showInfo("ดึงข้อมูล : Error","โปรดระบุ ID");
            }
        }
    }

    // method แสดงว่า add Data ได้ไหม
    private void showAddDataInfo(Boolean addData, String tableName){
        if(addData){
            showInfo("เพิ่มข้อมูล : Success", "เพิ่มข้อมูลเข้าตาราง "+tableName+" สำเร็จ");
            clearText();
        } else {
            showInfo("เพิ่มข้อมูล : Error", "ไม่สามารถเพิ่มข้อมูลได้");
        }
    }

    // method แสดงว่า delete Data ได้ไหม
    private void showDelDataInfo (Boolean delData){
        if(delData){
            showInfo("ลบข้อมูล : Success", "ลบข้อมูลเสร็จสิ้น");
        } else {
            showInfo("ลบข้อมูล : Error", "ไม่สามารถลบข้อมูลได้\nโปรดระบุ ID ให้ถูกต้อง");
        }
    }

    // method แสดงว่า update Data ได้ไหม
    private void showUpDataInfo (Boolean upData){
        if (upData) {
            showInfo("อัปเดตข้อมูล : Success", "อัปเดตข้อมูลสำเร็จ");
            clearText();
        } else {
            showInfo("อัปเดตข้อมูล : Error", "ไม่สามารถอัปเดตข้อมูลได้\nโปรดระบุ ID ให้ถูกต้อง");
        }
    }

    // method เช็คว่าเลือก Radio ตาราง หรือยัง
    private boolean checkChooseTable(){
        if (tableName!=null){
            return true;
        } else {
            showInfo("Error","โปรดเลือกตาราง");
            return false;
        }
    }

    // method เช็คว่า ทุกช่อง จะต้องไม่เป็นค่าว่าง
    // ตาราง Member เช็คแค่ 4 ช่อง
    // ตาราง ReserveList เช็ค 5 ช่อง
    private boolean checkFillInput(String table){
        if (table.equals("Member")){
            if (input1.getText().toString().equals("") || input2.getText().toString().equals("") || input3.getText().toString().equals("") || input4.getText().toString().equals("")){
                showInfo("Error","ระบุข้อมูลให้ครบถ้วน");
                return false;
            } else {
                return true;
            }
        } else if (table.equals("ReserveList")){
            if (input1.getText().toString().equals("") || input2.getText().toString().equals("") || input3.getText().toString().equals("") || input4.getText().toString().equals("") || input5.getText().toString().equals("")){
                showInfo("Error","ระบุข้อมูลให้ครบถ้วน");
                return false;
            } else {
                return true;
            }
        }
        // ถ้าไม่ใส่ค่าเลย ก็ false
        return false;
    }

    private void showInfo(String title, String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(true);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.show();
    }

    private void clearText(){
        input1.setText("");
        input2.setText("");
        input3.setText("");
        input4.setText("");
        input5.setText("");
    }

}