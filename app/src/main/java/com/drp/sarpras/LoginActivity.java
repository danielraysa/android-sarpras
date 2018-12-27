package com.drp.sarpras;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.txtUser);
        password = (EditText)findViewById(R.id.txtPass);
        login = (Button) findViewById(R.id.btnLogin);
        initDB();
        initUser();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cekUser = username.getText().toString();
                String cekPass = password.getText().toString();
                /*
                if ((cekUser.equals("admin") && cekPass.equals("admin")) ||(cekUser.equals("operator") && cekPass.equals("operator")))  {
                    //Toast.makeText(LoginActivity.this, "Login", Toast.LENGTH_SHORT).show();
                    //Intent y = new Intent(LoginActivity.this,MainActivity.class);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    Bundle b = new Bundle();
                    b.putString("user", username.getText().toString());
                    b.putString("pass", password.getText().toString());
                    i.putExtras(b);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Password salah", Toast.LENGTH_SHORT).show();
                    //username.getText().clear();
                    //password.getText().clear();
                } */

                //String uname = txt_login_username.getText().toString();
                //String pass = txt_login_password.getText().toString();
                if(cekUser.equals("") || cekPass.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Isikan username/password!")
                            .setNegativeButton("Retry", null).create().show();
                }else{
                    Cursor c = db.rawQuery("SELECT * FROM User WHERE USERNAME = '"+cekUser+"' AND PASSWORD = '"+cekPass+"' ", null);
                    if(c.getCount()>0){
                        c.moveToFirst();
                        String nama = c.getString(c.getColumnIndex("NAMA_USER"));
                        String roles = c.getString(c.getColumnIndex("ROLE"));
                        //Bantu.id_karyawan = c.getString(c.getColumnIndex("ID_KARYAWAN"));
                        //Bantu.karyawan = c.getString(c.getColumnIndex("NAMA_KARYAWAN"));
                        Intent intt = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle b = new Bundle();
                        b.putString("role", roles);
                        intt.putExtras(b);
                        Toast.makeText(getApplication(), "Login Berhasil ! \nSelamat Datang "+nama, Toast.LENGTH_SHORT).show();
                        startActivity(intt);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("username/password salah!")
                                .setNegativeButton("Retry", null).create().show();
                    }
                }
            }
        });

    }

    private void initDB(){
        try {
            db = openOrCreateDatabase(Bantu.DBNAME, MODE_PRIVATE, null);
            db.execSQL(Bantu.sql_user);
            db.execSQL(Bantu.sql_barang);
            db.execSQL(Bantu.sql_siswa);


        }catch (SQLException e){
            Toast.makeText(getApplicationContext(), "Terjadi Kesalahan init Database!", Toast.LENGTH_SHORT).show();
            Log.e("SQL ERROR", e.getMessage());
        }
    }

    private void initUser(){
        Cursor c = db.rawQuery("SELECT * FROM User", null);
        if(c.getCount()<1){
            db.execSQL("INSERT INTO User VALUES(1, 'Administrator', 'admin', 'admin', '00000','Administrator');");
        }
    }
}
