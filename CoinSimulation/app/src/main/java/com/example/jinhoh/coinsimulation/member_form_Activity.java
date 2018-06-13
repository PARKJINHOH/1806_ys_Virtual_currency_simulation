package com.example.jinhoh.coinsimulation;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class member_form_Activity extends AppCompatActivity {

    EditText edtMemberID, edtMemberPW, edtMemberPWCheck;
    Button btnMemberOK, btnMemberCancel;

    DBHelper myHelper;
    DBCoinHelper coinHelper;
    SQLiteDatabase mydb, coindb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_form);

        myHelper = new DBHelper(this, "member", null, 1);
        coinHelper = new DBCoinHelper(this, "coin", null, 1);

        edtMemberID = (EditText) findViewById(R.id.edtMemberID);
        edtMemberPW = (EditText) findViewById(R.id.edtMemberPW);
        edtMemberPWCheck = (EditText) findViewById(R.id.edtMemberPWCheck);
        btnMemberOK = (Button) findViewById(R.id.btnMemberOK);
        btnMemberCancel = (Button) findViewById(R.id.btnMemberCancel);

        btnMemberCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMemberOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydb = myHelper.getWritableDatabase(); //DB열기
                coindb = coinHelper.getWritableDatabase(); //DB열기
                String id = edtMemberID.getText().toString();
                String password = edtMemberPW.getText().toString();
                String ckpassword = edtMemberPWCheck.getText().toString();

                try {
                    if (password.equals(ckpassword)) {
                        String sql = "insert into member values(?,?);";
                        Object[] args = {id, password};
                        mydb.execSQL(sql, args);
                        mydb.close();
                        try {
                            String coinsql = "insert into coin(coin_id) values(?);";
                            Object[] argscoin = {id};
                            coindb.execSQL(coinsql, argscoin);
                            coindb.close();
                        } catch (Exception e) {

                            coindb.close();
                            Toast.makeText(getApplicationContext(), "DB생성 실패", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(getApplicationContext(), id + "님 회원가입을 축하드립니다", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        edtMemberPW.setText("");
                        edtMemberPWCheck.setText("");
                        Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
                        mydb.close();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "동일한 사용자가 있습니다.", Toast.LENGTH_LONG).show();
                }


            }
        });


    }
}
