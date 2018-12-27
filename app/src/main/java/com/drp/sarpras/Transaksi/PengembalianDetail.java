package com.drp.sarpras.Transaksi;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.drp.sarpras.Bantu;
import com.drp.sarpras.MainActivity;
import com.drp.sarpras.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Daniel Raysa Putra on 15/12/2018.
 */
public class PengembalianDetail extends AppCompatActivity {

    SQLiteDatabase db;

    ArrayList<String[]> detilPinjam = new ArrayList<String[]>();
    TextView nama, nis, tglpinjam, tglkembali;
    Button update;
    String id_pinjam;
    TableLayout tabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengembalian_detail);
        nama = (TextView)findViewById(R.id.nama_peminjam);
        nis = (TextView)findViewById(R.id.nis_peminjam);
        tglpinjam = (TextView)findViewById(R.id.tanggal_pinjam);
        tglkembali = (TextView)findViewById(R.id.tanggal_kembali);
        update = (Button)findViewById(R.id.update_kembali);
        tabel = (TableLayout)findViewById(R.id.table_new);
        initDB();
        get_data();
        detail_peminjaman();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PengembalianDetail.this);
                builder.setMessage("Apakah Anda Yakin?")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                update_data();
                                PengembalianDetail.this.finish();
                            }
                        })
                        .setNegativeButton("Batal", null)
                        .create().show();
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
        String[] data = itnt.getStringArrayExtra("detail_peminjaman");
        id_pinjam = data[0];
        nis.setText(data[1]);
        nama.setText(data[2]);
        tglpinjam.setText(data[3]);
        tglkembali.setText(data[4]);
        Cursor c = db.rawQuery("SELECT * FROM Peminjaman_Detail pd JOIN Barang b ON pd.ID_BARANG = b.ID_BARANG WHERE pd.ID_PEMINJAMAN = "+id_pinjam, null);
        detilPinjam.clear();
        while (c.moveToNext()){
            String[] ret = new String[3];
            ret[0] = c.getString(c.getColumnIndex("ID_PEMINJAMAN"));
            ret[1] = c.getString(c.getColumnIndex("ID_BARANG"));
            ret[2] = c.getString(c.getColumnIndex("NAMA_BARANG"));
            detilPinjam.add(ret);
        }
    }

    private void update_data(){
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int day = ca.get(Calendar.DAY_OF_MONTH);
        String sql_insert = "UPDATE Peminjaman SET" +
                " REAL_KEMBALI = 'Kembali', " +
                " STATUS = 'Kembali' " +
                " WHERE ID_PEMINJAMAN = "+id_pinjam+"";
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

    private void detail_peminjaman() {
        int co = tabel.getChildCount();
        for (int i = 1; i < co; i++) {
            View child = tabel.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        for (int i = 0; i < detilPinjam.size(); i++) {
            final TableRow row = new TableRow(this);
            final TextView tno = new TextView(this);

            final TextView tid = new TextView(this);
            final TextView tna = new TextView(this);

            //row.setPadding(0, 10, 0, 10);
            tid.setTextColor(getResources().getColor(R.color.black));
            tno.setTextColor(getResources().getColor(R.color.black));

            tna.setTextColor(getResources().getColor(R.color.black));

            tid.setGravity(Gravity.CENTER);
            tno.setGravity(Gravity.CENTER);
            tna.setGravity(Gravity.CENTER);

            //tno.setPadding(10, 0, 0, 0);
            //tid.setPadding(18, 0, 0, 0);
            //tna.setPadding(18, 0, 18, 0);

            //tid.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));
            //tno.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1));
            //tna.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 3));
            //tid.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,5f));
            Log.e("DATA ", String.valueOf(i) + " " + detilPinjam.get(i)[1] + " " + detilPinjam.get(i)[2]);
            tno.setText(String.valueOf(i + 1));
            tid.setText(detilPinjam.get(i)[1]);
            tna.setText(detilPinjam.get(i)[2]);

            //row.addView(tid);
            row.addView(tno);
            row.addView(tid);
            row.addView(tna);
            tabel.addView(row);

        }
    }
}
