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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.drp.sarpras.Adapter.BarangAdapter;
import com.drp.sarpras.Bantu;
import com.drp.sarpras.R;

import java.util.ArrayList;

public class DataBarangActivity extends AppCompatActivity {

    SQLiteDatabase db;

    ArrayList<String[]> dataBarang = new ArrayList<String[]>();
    ImageButton btn_add_barang;
    ListView list_barang;
    Button hapus_barang, edit_barang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_barang);
        btn_add_barang = (ImageButton)findViewById(R.id.btn_add_barang);
        list_barang = (ListView)findViewById(R.id.list_barang);
        //hapus_barang = (Button)findViewById(R.id.hapusButton);
        //edit_barang = (Button)findViewById(R.id.editButton);
        //initDB();
        //get_data();
        btn_add_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itnt = new Intent(getApplicationContext(), TambahBarangActivity.class);
                startActivity(itnt);
            }
        });

         /*
        edit_barang.setOnClickListener(new AdapterView.OnItemClickListener() {
            ba
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                list_barang.setAdapter(ca);
                Intent itnt = new Intent(getApplicationContext(), BarangEditActivity.class);
                itnt.putExtra("detail_barang", dataBarang.get(i));
                startActivity(itnt);
                }
            }
        ); */
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
        Cursor c = db.rawQuery("SELECT * FROM Barang WHERE STATUS = 'Aktif'", null);
        dataBarang.clear();
        while (c.moveToNext()){
            String[] ret = new String[3];
            ret[0] = c.getString(c.getColumnIndex("ID_BARANG"));
            ret[1] = c.getString(c.getColumnIndex("NAMA_BARANG"));
            ret[2] = c.getString(c.getColumnIndex("JENIS_BARANG"));
            dataBarang.add(ret);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDB();
        get_data();
        BarangAdapter ca = new BarangAdapter(getApplicationContext(), dataBarang);
        list_barang.setAdapter(ca);

        list_barang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                Intent itnt = new Intent(getApplicationContext(), BarangEditActivity.class);
                itnt.putExtra("detail_barang", dataBarang.get(i));
                startActivity(itnt);
            }
        });

    }
}
