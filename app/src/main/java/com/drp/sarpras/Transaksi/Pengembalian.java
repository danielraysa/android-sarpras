package com.drp.sarpras.Transaksi;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.drp.sarpras.Adapter.BarangAdapter;
import com.drp.sarpras.Adapter.PeminjamanAdapter;
import com.drp.sarpras.Bantu;
import com.drp.sarpras.Master.BarangEditActivity;
import com.drp.sarpras.R;

import java.util.ArrayList;

public class Pengembalian extends AppCompatActivity {
    SQLiteDatabase db;

    ArrayList<String[]> dataPinjam = new ArrayList<String[]>();
    ImageButton btn_add_barang;
    ListView list_pinjam;
    Button hapus_barang, edit_barang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengembalian);

        list_pinjam = (ListView)findViewById(R.id.list_peminjaman);
    }

    private void initDB(){
        try {
            db = openOrCreateDatabase(Bantu.DBNAME, MODE_PRIVATE, null);
            db.execSQL(Bantu.sql_peminjaman);
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(), "Terjadi Kesalahan init Database!", Toast.LENGTH_SHORT).show();
            Log.e("SQL ERROR", e.getMessage());
        }
    }


    private void get_data(){
        Cursor c = db.rawQuery("SELECT * FROM Peminjaman p JOIN Siswa s ON p.NIS = s.NIS WHERE p.STATUS = 'Pinjam'", null);
        dataPinjam.clear();
        while (c.moveToNext()){
            String[] ret = new String[5];
            ret[0] = c.getString(c.getColumnIndex("ID_PEMINJAMAN"));
            ret[1] = c.getString(c.getColumnIndex("NIS"));
            ret[2] = c.getString(c.getColumnIndex("NAMA_SISWA"));
            ret[3] = c.getString(c.getColumnIndex("TANGGAL_PINJAM"));
            ret[4] = c.getString(c.getColumnIndex("TANGGAL_KEMBALI"));
            dataPinjam.add(ret);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDB();
        get_data();
        PeminjamanAdapter ca = new PeminjamanAdapter(getApplicationContext(), dataPinjam);
        list_pinjam.setAdapter(ca);

        list_pinjam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                Intent itnt = new Intent(getApplicationContext(), PengembalianDetail.class);
                itnt.putExtra("detail_peminjaman", dataPinjam.get(i));
                startActivity(itnt);
            }
        });

    }
}
