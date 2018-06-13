package com.example.jinhoh.coinsimulation;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText edtID, edtPW;
    Button btnLogin, btnMemberForm;


    DBHelper myHelper;
    SQLiteDatabase mydb;
    Cursor cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHelper = new DBHelper(this, "member", null, 1);

        edtID = (EditText) findViewById(R.id.edtID);
        edtPW = (EditText) findViewById(R.id.edtPW);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnMemberForm = (Button) findViewById(R.id.btnMemberForm);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mydb = myHelper.getWritableDatabase(); //DB열기
                String id = edtID.getText().toString();
                String password = edtPW.getText().toString();
                String sql = "select * from member where member_id=?";
                String[] args = {id};

                try {
                    cr = mydb.rawQuery(sql, args);
                    if (id.equals("") || password.equals("")) {
                        Toast.makeText(getApplicationContext(), "회원 정보를 확인해 주세요", Toast.LENGTH_LONG).show();
                        edtID.setText("");
                        edtPW.setText("");
                    }
                    while (cr.moveToNext()) {
                        if (cr.getString(0).equals(id) == false) {
                            Toast.makeText(getApplicationContext(), "아이디를 확인해 주세요", Toast.LENGTH_LONG).show();
                            edtID.setText("");
                            edtPW.setText("");
                        } else if(cr.getString(1).equals(password) == false){
                            Toast.makeText(getApplicationContext(), "비밀번호를 확인해 주세요", Toast.LENGTH_LONG).show();
                            edtID.setText("");
                            edtPW.setText("");
                        }
                        if (cr.getString(0).equals(id) && cr.getString(1).equals(password)) {
                            Toast.makeText(getApplicationContext(), cr.getString(0) + "님 어서오세요", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(getApplicationContext(), coin_main_Activity.class);
                            in.putExtra("id", id);
                            startActivity(in);
                            finish();
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "ㅇㅇㅇ회원 정보를 확인해 주세요", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    edtID.setText("");
                    edtPW.setText("");


                } finally {
                    mydb.close();
                    cr.close();
                }
            }
        });

        btnMemberForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), member_form_Activity.class);
                startActivity(in);
            }
        });
    }
}
