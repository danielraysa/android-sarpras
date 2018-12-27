package com.drp.sarpras.Master;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.drp.sarpras.Adapter.SiswaAdapter;
import com.drp.sarpras.Adapter.UserAdapter;
import com.drp.sarpras.Bantu;
import com.drp.sarpras.R;

import java.util.ArrayList;

public class DataSiswaActivity extends AppCompatActivity {
    //sambalvio.000webhostapp.com, id3107472_anel, anel2204, id3107472_ppsi
    SQLiteDatabase db;

    ArrayList<String[]> dataSiswa = new ArrayList<String[]>();
    ImageButton btn_add_siswa;
    ListView list_siswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_siswa);
        btn_add_siswa = (ImageButton)findViewById(R.id.btn_add_siswa);
        list_siswa = (ListView)findViewById(R.id.list_siswa);

        btn_add_siswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itnt = new Intent(getApplicationContext(), TambahSiswaActivity.class);
                startActivity(itnt);
            }
        });
    }

    private void initDB(){
        try {
            db = openOrCreateDatabase(Bantu.DBNAME, MODE_PRIVATE, null);
            db.execSQL(Bantu.sql_siswa);
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(), "Terjadi Kesalahan init Database!", Toast.LENGTH_SHORT).show();
            Log.e("SQL ERROR", e.getMessage());
        }
    }


    private void get_data(){
        Cursor c = db.rawQuery("SELECT * FROM Siswa", null);
        dataSiswa.clear();
        while (c.moveToNext()){
            String[] ret = new String[4];
            ret[0] = c.getString(c.getColumnIndex("NIS"));
            ret[1] = c.getString(c.getColumnIndex("NAMA_SISWA"));
            ret[2] = c.getString(c.getColumnIndex("NO_TELP"));
            ret[3] = c.getString(c.getColumnIndex("ALAMAT"));
            dataSiswa.add(ret);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDB();
        get_data();

        SiswaAdapter ca = new SiswaAdapter(getApplicationContext(), dataSiswa);
        list_siswa.setAdapter(ca);
        list_siswa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent itnt = new Intent(getApplicationContext(), SiswaEditActivity.class);
                itnt.putExtra("detail_barang", dataSiswa.get(i));
                startActivity(itnt);
            }
        });
    }
}
