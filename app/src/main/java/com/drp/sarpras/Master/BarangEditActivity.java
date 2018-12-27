package com.drp.sarpras.Master;

import android.content.DialogInterface;
import android.content.Intent;
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

public class BarangEditActivity extends AppCompatActivity {

    SQLiteDatabase db;

    //ArrayList<String[]> dataSupplier = new ArrayList<String[]>();
    EditText txt_frm_barang_kode, txt_frm_barang_nama, txt_frm_barang_jenis;
    Button btn_edit_barang, btn_hapus_barang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang_edit);
        txt_frm_barang_kode = (EditText)findViewById(R.id.txt_frm_barang_kode);
        txt_frm_barang_nama = (EditText)findViewById(R.id.txt_frm_barang_nama);
        txt_frm_barang_jenis = (EditText)findViewById(R.id.txt_frm_barang_jenis);
        //txt_frm_barang_warna = (EditText)findViewById(R.id.txt_frm_barang_warna);
        //txt_frm_barang_size = (EditText)findViewById(R.id.txt_frm_barang_size);
        btn_edit_barang = (Button)findViewById(R.id.btn_edit_barang);
        btn_hapus_barang = (Button)findViewById(R.id.btn_hapus_barang);
        initDB();
        get_data();
        btn_edit_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_data();
            }
        });
        btn_hapus_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BarangEditActivity.this);
                builder.setMessage("Apakah Anda Yakin?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete_data();
                            }
                        }).create().show();
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

    private void get_data(){
        Intent itnt = getIntent();
        String[] data = itnt.getStringArrayExtra("detail_barang");
        txt_frm_barang_kode.setText(data[0]);
        txt_frm_barang_nama.setText(data[1]);
        txt_frm_barang_jenis.setText(data[2]);
        txt_frm_barang_kode.setEnabled(false);
    }

    private void update_data(){
        if(
                txt_frm_barang_kode.getText().toString().equals("") ||
                txt_frm_barang_nama.getText().toString().equals("") ||
                txt_frm_barang_jenis.getText().toString().equals("")
                ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Silahkan lengkkapi data!")
                    .setNegativeButton("Retry", null).create().show();
        }else{
            String sql_insert = "UPDATE Barang SET" +
                    " NAMA_BARANG = '"+txt_frm_barang_nama.getText().toString()+"', " +
                    " JENIS_BARANG = '"+txt_frm_barang_jenis.getText().toString()+"' " +
                    " WHERE ID_BARANG = '"+txt_frm_barang_kode.getText().toString()+"'";
            try {
                db.execSQL(sql_insert);
                Toast.makeText(getApplicationContext(), "Data berhasil diubah!", Toast.LENGTH_SHORT).show();
                finish();
            }catch (SQLException e){
                /*
                String er[] = e.getMessage().split(" ");
                String msg = "";
                if(er[4].equals("unique")){
                    msg = "\nID BARANG sudah ada!";
                } */
                //Toast.makeText(getApplicationContext(), "Data gagal diubah!"+msg, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Data gagal diubah!", Toast.LENGTH_SHORT).show();
                Log.e("SQL ERROR", e.getMessage());
            }
        }
    }

    private void delete_data(){
        if(txt_frm_barang_kode.getText().toString().equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Silahkan lengkkapi data!")
                    .setNegativeButton("Retry", null).create().show();
        }else{
            String sql_insert = "UPDATE Barang SET STATUS = 'Dihapus'" +
                    " WHERE ID_BARANG = '"+txt_frm_barang_kode.getText().toString()+"'";
            try {
                db.execSQL(sql_insert);
                Toast.makeText(getApplicationContext(), "Data berhasil dihapus!", Toast.LENGTH_SHORT).show();
                finish();
            }catch (SQLException e){
                Toast.makeText(getApplicationContext(), "Data gagal dihapus!", Toast.LENGTH_SHORT).show();
                Log.e("SQL ERROR", e.getMessage());
            }
        }
    }

}
