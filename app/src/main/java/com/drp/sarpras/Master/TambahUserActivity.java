package com.drp.sarpras.Master;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import com.idescout.sql.SqlScoutServer;
import com.drp.sarpras.Bantu;
import com.drp.sarpras.R;
import com.drp.sarpras.RequestHandler;
import com.drp.sarpras.konfigurasi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TambahUserActivity extends AppCompatActivity {
    SQLiteDatabase db;

    EditText txt_frm_nama_user, txt_frm_username, txt_frm_password, txt_frm_no_telp;
    Button btn_simpan_user;
    Spinner spin_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_user);
        txt_frm_nama_user = (EditText)findViewById(R.id.txt_frm_nama_user);
        txt_frm_username = (EditText)findViewById(R.id.txt_frm_username);
        txt_frm_password = (EditText)findViewById(R.id.txt_frm_password);
        txt_frm_no_telp = (EditText)findViewById(R.id.txt_frm_no_telp);
        spin_role = (Spinner)findViewById(R.id.spinner_role);
        btn_simpan_user = (Button)findViewById(R.id.btn_simpan_user);

        initDB();
        btn_simpan_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_data();
            }
        });

    }


    private void initDB(){
        try {
            db = openOrCreateDatabase(Bantu.DBNAME, MODE_PRIVATE, null);
            db.execSQL(Bantu.sql_user);
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(), "Terjadi Kesalahan init Database!", Toast.LENGTH_SHORT).show();
            Log.e("SQL ERROR", e.getMessage());
        }
    }

    private void add_data(){
        if(
            txt_frm_nama_user.getText().toString().equals("") ||
            txt_frm_username.getText().toString().equals("") ||
            txt_frm_password.getText().toString().equals("") ||
            txt_frm_no_telp.getText().toString().equals("")
            ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Silahkan lengkkapi data!")
                    .setNegativeButton("Retry", null).create().show();
        }else{
            String sql_insert = "INSERT INTO User (NAMA_USER, USERNAME, PASSWORD, NO_TELP, ROLE) VALUES(" +
                    " '"+txt_frm_nama_user.getText().toString()+"', " +
                    " '"+txt_frm_username.getText().toString()+"', " +
                    " '"+txt_frm_password.getText().toString()+"'," +
                    " '"+txt_frm_no_telp.getText().toString()+"'," +
                    " '"+spin_role.getSelectedItem().toString()+"')";
            try {
                db.execSQL(sql_insert);
                Toast.makeText(getApplicationContext(), "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                finish();
            }catch (SQLException e){
                String er[] = e.getMessage().split(" ");
                String msg = "";
                if(er[4].equals("unique")){
                    msg = "\nKODE BARANG sudah ada!";
                }
                Toast.makeText(getApplicationContext(), "Data gagal ditambahkan!"+msg, Toast.LENGTH_SHORT).show();
                Log.e("SQL ERROR", e.getMessage());
            }
        }
    }

    //Dibawah ini merupakan perintah untuk Menambahkan Pegawai (CREATE)
    private void addEmployee(){

        final String nama = txt_frm_nama_user.getText().toString();
        final String username = txt_frm_username.getText().toString();
        final String pass = txt_frm_password.getText().toString();
        final String no_telp = txt_frm_no_telp.getText().toString();
        final String role = spin_role.getSelectedItem().toString();

        class AddEmployee extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TambahUserActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TambahUserActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_NAMA,nama);
                params.put(konfigurasi.KEY_EMP_POSISI,username);
                params.put(konfigurasi.KEY_EMP_GAJIH,pass);
                params.put(konfigurasi.KEY_EMP_GAJIH,no_telp);
                params.put(konfigurasi.KEY_EMP_GAJIH,role);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }

}
