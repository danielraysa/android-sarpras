package com.drp.sarpras.Master;

//import android.support.design.widget.FloatingActionButton;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.Snackbar;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.drp.sarpras.Adapter.BarangAdapter;
import com.drp.sarpras.Adapter.SiswaAdapter;
import com.drp.sarpras.Adapter.UserAdapter;
import com.drp.sarpras.Bantu;
import com.drp.sarpras.R;

import java.util.ArrayList;

public class DataUserActivity extends AppCompatActivity {

    //sambalvio.000webhostapp.com, id3107472_anel, anel2204, id3107472_ppsi
    SQLiteDatabase db;

    ArrayList<String[]> dataUser = new ArrayList<String[]>();
    ImageButton btn_add_user;
    ListView list_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_user);
        btn_add_user = (ImageButton)findViewById(R.id.btn_add_user);
        list_user = (ListView)findViewById(R.id.list_user);

        btn_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itnt = new Intent(getApplicationContext(), TambahUserActivity.class);
                startActivity(itnt);
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


    private void get_data(){
        Cursor c = db.rawQuery("SELECT * FROM User", null);
        dataUser.clear();
        while (c.moveToNext()){
            String[] ret = new String[6];
            ret[0] = c.getString(c.getColumnIndex("ID_USER"));
            ret[1] = c.getString(c.getColumnIndex("NAMA_USER"));
            ret[2] = c.getString(c.getColumnIndex("USERNAME"));
            ret[3] = c.getString(c.getColumnIndex("PASSWORD"));
            ret[4] = c.getString(c.getColumnIndex("NO_TELP"));
            ret[5] = c.getString(c.getColumnIndex("ROLE"));
            dataUser.add(ret);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDB();
        get_data();

        UserAdapter ca = new UserAdapter(getApplicationContext(), dataUser);
        list_user.setAdapter(ca);
        list_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent itnt = new Intent(getApplicationContext(), UserEditActivity.class);
                itnt.putExtra("detail_barang", dataUser.get(i));
                startActivity(itnt);
            }
        });
    }
}
