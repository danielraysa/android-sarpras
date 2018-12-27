package com.drp.sarpras.Master;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.drp.sarpras.Bantu;
import com.drp.sarpras.R;

public class TambahSiswaActivity extends AppCompatActivity {
    SQLiteDatabase db;

    EditText txt_frm_nis, txt_frm_nama_siswa, txt_frm_alamat_siswa, txt_frm_no_telp_siswa;
    Button btn_simpan_siswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_siswa);
        txt_frm_nis = (EditText)findViewById(R.id.txt_frm_nis);
        txt_frm_nama_siswa = (EditText)findViewById(R.id.txt_frm_nama_siswa);
        txt_frm_alamat_siswa = (EditText)findViewById(R.id.txt_frm_alamat_siswa);
        txt_frm_no_telp_siswa = (EditText)findViewById(R.id.txt_frm_no_telp_siswa);
        //txt_frm_barang_warna = (EditText)findViewById(R.id.txt_frm_barang_warna);
        //txt_frm_barang_size = (EditText)findViewById(R.id.txt_frm_barang_size);
        btn_simpan_siswa = (Button)findViewById(R.id.btn_simpan_siswa);

        initDB();
        btn_simpan_siswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_data();
            }
        });

    }


    private void initDB(){
        try {
            db = openOrCreateDatabase(Bantu.DBNAME, MODE_PRIVATE, null);
            db.execSQL(Bantu.sql_barang);
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(), "Terjadi Kesalahan init Database!", Toast.LENGTH_SHORT).show();
            Log.e("SQL ERROR", e.getMessage());
        }
    }

    private void add_data(){
        if(
                txt_frm_nis.getText().toString().equals("") ||
                        txt_frm_nama_siswa.getText().toString().equals("") ||
                        txt_frm_alamat_siswa.getText().toString().equals("") ||
                        txt_frm_no_telp_siswa.getText().toString().equals("")
                ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Silahkan lengkkapi data!")
                    .setNegativeButton("Retry", null).create().show();
        }else{
            String sql_insert = "INSERT INTO Siswa VALUES(" +
                    " '"+txt_frm_nis.getText().toString()+"', " +
                    " '"+txt_frm_nama_siswa.getText().toString()+"', " +
                    " '"+txt_frm_alamat_siswa.getText().toString()+"'," +
                    " '"+txt_frm_no_telp_siswa.getText().toString()+"'" +
                    " )";
            try {
                db.execSQL(sql_insert);
                Toast.makeText(getApplicationContext(), "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                finish();
            }catch (SQLException e){
                /*
                String er[] = e.getMessage().split(" ");
                String msg = "";
                if(er[4].equals("unique")){
                    msg = "\nKODE BARANG sudah ada!";
                } */
                Toast.makeText(getApplicationContext(), "Data gagal ditambahkan!", Toast.LENGTH_SHORT).show();
                Log.e("SQL ERROR", e.getMessage());
            }
        }
    }
}
