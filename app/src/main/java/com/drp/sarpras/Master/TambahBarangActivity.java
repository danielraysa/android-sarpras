package com.drp.sarpras.Master;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.drp.sarpras.Bantu;
import com.drp.sarpras.R;

import java.util.ArrayList;

public class TambahBarangActivity extends AppCompatActivity {

    SQLiteDatabase db;

    ArrayList<String[]> dataSupplier = new ArrayList<String[]>();
    EditText txt_frm_barang_kode, txt_frm_barang_nama, txt_frm_barang_jenis;
    Button btn_simpan_barang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);
        txt_frm_barang_kode = (EditText)findViewById(R.id.txt_frm_barang_kode);
        txt_frm_barang_nama = (EditText)findViewById(R.id.txt_frm_barang_nama);
        txt_frm_barang_jenis = (EditText)findViewById(R.id.txt_frm_barang_jenis);
        //txt_frm_barang_warna = (EditText)findViewById(R.id.txt_frm_barang_warna);
        //txt_frm_barang_size = (EditText)findViewById(R.id.txt_frm_barang_size);
        btn_simpan_barang = (Button)findViewById(R.id.btn_simpan_barang);


        initDB();
        btn_simpan_barang.setOnClickListener(new View.OnClickListener() {
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
                txt_frm_barang_kode.getText().toString().equals("") ||
                txt_frm_barang_nama.getText().toString().equals("") ||
                txt_frm_barang_jenis.getText().toString().equals("")

                ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Silahkan lengkkapi data!")
                    .setNegativeButton("Retry", null).create().show();
        }else{
            String sql_insert = "INSERT INTO Barang VALUES(" +
                    " '"+txt_frm_barang_kode.getText().toString()+"', " +
                    " '"+txt_frm_barang_nama.getText().toString()+"', " +
                    " '"+txt_frm_barang_jenis.getText().toString()+"'," +
                    " 'Aktif'" +
                    ")";
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

}
