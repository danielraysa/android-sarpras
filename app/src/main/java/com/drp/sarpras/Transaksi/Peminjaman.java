package com.drp.sarpras.Transaksi;

import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.drp.sarpras.Adapter.BarangAdapter;
import com.drp.sarpras.Adapter.PeminjamanAdapter;
import com.drp.sarpras.Adapter.SiswaAdapter;
import com.drp.sarpras.Bantu;
import com.drp.sarpras.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Peminjaman extends AppCompatActivity {
    SQLiteDatabase db;

    EditText txt_frm_penjualan_kode, txt_frm_penjualan_tanggal, txt_frm_penjualan_customer;
    TableLayout tbl_det_barang;
    Button btn_transaksi_tambah_barang, btn_transaksi_simpan, btn_cetak, btn_cari_siswa;
    TextView txt_frm_penjualan_total;
    DatePickerDialog.OnDateSetListener OnDateListener;

    Double grandTotal = 0.0;
    int nm_trans = 0;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    UUID udd;
    Boolean konek_printer = false;

    Integer idpinjam;
    String myDate;
    ArrayList<String[]> dataSiswa = new ArrayList<String[]>();
    ArrayList<String[]> dataBarang = new ArrayList<String[]>();
    ListView list_pinjam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peminjaman);
        //txt_frm_penjualan_kode = (EditText)findViewById(R.id.txt_frm_penjualan_kode);
        txt_frm_penjualan_tanggal = (EditText)findViewById(R.id.txt_frm_peminjaman_tanggal);
        txt_frm_penjualan_customer = (EditText)findViewById(R.id.txt_frm_nis);
        tbl_det_barang = (TableLayout)findViewById(R.id.tbl_det_barang);
        btn_transaksi_tambah_barang = (Button)findViewById(R.id.btn_transaksi_tambah_barang);
        btn_transaksi_simpan = (Button)findViewById(R.id.btn_transaksi_simpan);
        //txt_frm_penjualan_total = (TextView)findViewById(R.id.txt_frm_penjualan_total);
        btn_cari_siswa = (Button)findViewById(R.id.frm_btn_siswa);
        //list_pinjam = (ListView)findViewById(R.id.list_view_daftar_barang);
        myDate =  new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        txt_frm_penjualan_tanggal.setText(myDate);

        //txt_frm_penjualan_customer.requestFocus();


        txt_frm_penjualan_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Peminjaman.this, SelectDataSiswa.class);
                startActivity(a);
            }
        });

        initDB();
        get_data();
        //gen_id_trans();
        Bantu.detailPeminjaman.clear();
        get_detail();
        btn_transaksi_tambah_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itnt = new Intent(getApplicationContext(), SelectDataBarang.class);
                startActivity(itnt);
            }
        });
        btn_transaksi_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan_transaksi();
            }
        });
        txt_frm_penjualan_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar ca = Calendar.getInstance();
                int year = ca.get(Calendar.YEAR);
                int month = ca.get(Calendar.MONTH);
                int day = ca.get(Calendar.DAY_OF_MONTH);
                /*
                DatePickerDialog dialog = new DatePickerDialog(
                        Peminjaman.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        OnDateListener,
                        year, month, day); */
                DatePickerDialog dialog = new DatePickerDialog(
                        Peminjaman.this,
                        android.R.style.Theme_DeviceDefault_Dialog,
                        OnDateListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //dialog.getWindow();
                dialog.show();
            }
        });
        OnDateListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = dayOfMonth +"/"+ month +"/"+ year;
                txt_frm_penjualan_tanggal.setText(date);
            }
        };
        //Bantu.detailPeminjaman.clear();

        btn_cari_siswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cari_siswa();
            }
        });

        btn_cetak = (Button)findViewById(R.id.btn_cetak);

    }
    /*
    private void gen_id_trans(){
        String num = "0000000"+String.valueOf(nm_trans);
        String nt = "PNJ-"+num.substring(num.length()-5, num.length());
        txt_frm_penjualan_kode.setText(nt);
        Log.e("GEN - ", num);
        Log.e("GEN - ", nt);
    }
    */

    private void initDB(){
        try {
            db = openOrCreateDatabase(Bantu.DBNAME, MODE_PRIVATE, null);
            db.execSQL(Bantu.sql_peminjaman);
            db.execSQL(Bantu.sql_peminjaman_detail);

        }catch (SQLException e){
            Toast.makeText(getApplicationContext(), "Terjadi Kesalahan init Database!", Toast.LENGTH_SHORT).show();
            Log.e("SQL ERROR", e.getMessage());
        }
    }

    private void get_data(){
        Cursor c = db.rawQuery("SELECT * FROM Peminjaman", null);
        nm_trans = c.getCount()+1;
    }

    private void get_data_siswa(String cari){
        Cursor c = db.rawQuery("SELECT * FROM Siswa WHERE NAMA_SISWA LIKE '%"+cari+"%'", null);
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

    private void get_data_barang(String cari){
        Cursor c = db.rawQuery("SELECT * FROM Barang WHERE NAMA_BARANG LIKE '%"+cari+"%' OR JENIS_BARANG LIKE '%"+cari+"%' ", null);
        dataBarang.clear();
        while (c.moveToNext()){
            String[] rets = new String[3];
            rets[0] = c.getString(c.getColumnIndex("ID_BARANG"));
            rets[1] = c.getString(c.getColumnIndex("NAMA_BARANG"));
            rets[2] = c.getString(c.getColumnIndex("JENIS_BARANG"));
            dataBarang.add(rets);
        }
    }

    public void cari_siswa(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(Peminjaman.this);
        LayoutInflater inflter = getLayoutInflater();
        final View DialogView = inflter.inflate(R.layout.activity_select_data_siswa, null);

        builder.setView(DialogView)
                .setCancelable(true)
                .setNegativeButton("Batal", null);
        EditText cari_supplier_nama = (EditText)DialogView.findViewById(R.id.cari_siswa_nama);
        final ListView list_siswa = (ListView)DialogView.findViewById(R.id.list_daftar_siswa);
        get_data_siswa("");

        cari_supplier_nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt_cari_siswa(s.toString(), list_siswa, DialogView);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        SiswaAdapter sca = new SiswaAdapter(DialogView.getContext(), dataSiswa);
        list_siswa.setAdapter(sca);
        final AlertDialog opt = builder.create();
        opt.show();
        list_siswa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                txt_frm_penjualan_customer.setText(dataSiswa.get(i)[0]);
                //txt_frm_barang_id_supplier.setText(dataSupplier.get(i)[0]);
                opt.dismiss();
            }
        });
    }

    public void txt_cari_siswa(String cari, ListView list, View v){
        get_data_siswa(cari);
        SiswaAdapter sca = new SiswaAdapter(v.getContext(), dataSiswa);
        list.setAdapter(sca);
    }

    private void simpan_transaksi(){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        if(     txt_frm_penjualan_tanggal.getText().toString().equals("") ||
                txt_frm_penjualan_customer.getText().toString().equals("") ||
                Bantu.detailPeminjaman.isEmpty()
                ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Silahkan lengkkapi data!")
                    .setNegativeButton("Retry", null).create().show();
        }else{

            String sql = "INSERT INTO Peminjaman (NIS, TANGGAL_PINJAM, TANGGAL_KEMBALI, REAL_KEMBALI, STATUS) VALUES(" +
                    " '"+txt_frm_penjualan_customer.getText().toString()+"',"+
                    " '"+myDate+"',"+
                    " '"+txt_frm_penjualan_tanggal.getText().toString()+"',"+
                    " '',"+
                    " 'Pinjam'"+
                    ");";
            try{
                db.execSQL(sql);

                Cursor cd = db.rawQuery("SELECT * FROM Peminjaman WHERE NIS = '"+txt_frm_penjualan_customer.getText().toString()+"' AND TANGGAL_PINJAM = '"+myDate+"' AND TANGGAL_KEMBALI = '"+txt_frm_penjualan_tanggal.getText().toString()+"'", null);
                dataSiswa.clear();
                while (cd.moveToNext()){
                    //String[] ret = new String[4];
                    idpinjam = cd.getInt(cd.getColumnIndex("ID_PEMINJAMAN"));
                }

                for(int i = 0; i<Bantu.detailPeminjaman.size(); i++){
                    String sql_det = "INSERT INTO Peminjaman_Detail (ID_PEMINJAMAN, ID_BARANG) VALUES(" +
                            ""+idpinjam+", "+
                            "'"+Bantu.detailPeminjaman.get(i)[0]+"') ";
                    try{
                        db.execSQL(sql_det);

                        //Double hrg = Double.parseDouble(Bantu.detailPenjualan.get(i)[2]);
                        //Double st = Double.parseDouble(Bantu.detailPenjualan.get(i)[4]);
                        //msg += Bantu.detailPenjualan.get(i)[1]+" \n \t\t "+Bantu.detailPenjualan.get(i)[2];
                        //msg += " \t\t "+formatRupiah.format((double)hrg)+" \t "+formatRupiah.format((double)st)+"\n";
                    }catch (Exception e){
                        Log.e("DETAIL EROOR", "GAGAL DITAMBAH!");
                        Log.e("DETAIL EROOR", sql_det);
                    }
                }
                /*
                msg += "---------------------------------------------\n";
                msg += "\t\t\t\t\t Grand Total : "+formatRupiah.format((double)grandTotal)+"\n";

                msg += "\n\n\t***** Terimakasih Atas Kunjungan Anda *****\n";
                msg += "\t\t\t\t\t"+Bantu.karyawan+"\n\n";
                Log.e("NOTA", msg);
                Toast.makeText(getApplicationContext(), "Transaksi Berhasil disimpan", Toast.LENGTH_SHORT).show();

                try {
                    if(konek_printer){
                        mmOutputStream.write(msg.getBytes());
                    }
                    //Toast.makeText(getApplicationContext(), "Data Sent", Toast.LENGTH_LONG).show();
                    //closeBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                */
                Bantu.detailPeminjaman.clear();
                Toast.makeText(getApplication(), "Data berhasil disimpan ", Toast.LENGTH_SHORT).show();
                finish();
            }catch (SQLException e){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Terjadi Kesalahan, Data Transaksi Gagal Disimpan!")
                        .setNegativeButton("Retry", null).create().show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_detail();
    }

    private void get_detail() {
        //tbl_det_barang.removeAllViews();
        //Locale localeID = new Locale("in", "ID");
        //NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int co = tbl_det_barang.getChildCount();
        for(int i = 1; i<co; i++){
            View child = tbl_det_barang.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        //grandTotal = 0.0;
        for(int i = 0; i<Bantu.detailPeminjaman.size(); i++){
            final TableRow row = new TableRow(this);
            final TextView tno = new TextView(this);
            final TextView tid = new TextView(this);
            final TextView tna = new TextView(this);

            //row.setPadding(0,10,0,10);
            tid.setTextColor(getResources().getColor(R.color.black));
            tno.setTextColor(getResources().getColor(R.color.black));
            tna.setTextColor(getResources().getColor(R.color.black));

            //tjm.setPadding(18,0,18,0);
            //thg.setPadding(18,0,18,0);
            //tot.setPadding(18,0,18,0);
            tid.setGravity(Gravity.CENTER);
            tno.setGravity(Gravity.CENTER);
            tna.setGravity(Gravity.CENTER);
            //Double hrg = Double.parseDouble(Bantu.detailPenjualan.get(i)[2]);
            //Double st = Double.parseDouble(Bantu.detailPenjualan.get(i)[4]);
            //grandTotal += st;
            Log.e("DATA ", String.valueOf(i)+" "+Bantu.detailPeminjaman.get(i)[0]+" "+Bantu.detailPeminjaman.get(i)[1]);
            tno.setText(String.valueOf(i+1));
            tid.setText(Bantu.detailPeminjaman.get(i)[0]);
            tna.setText(Bantu.detailPeminjaman.get(i)[1]);

            //row.addView(tid);
            row.addView(tno);
            row.addView(tid);
            row.addView(tna);
            tbl_det_barang.addView(row);
            final int index = i;
            row.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(Peminjaman.this);
                    LayoutInflater inflter = getLayoutInflater();
                    final View DialogView = inflter.inflate(R.layout.list_ubah_barang, null);
                    //final View DialogView = inflter.inflate(R.layout.activity_select_data_barang, null);
                    builder.setView(DialogView)
                            .setCancelable(true)
                            .setTitle("Ubah Barang \n"+Bantu.detailPeminjaman.get(index)[1]);
                    EditText cari_barang_nama = (EditText)DialogView.findViewById(R.id.ubah_barang_nama);
                    //EditText cari_barang_nama = (EditText)DialogView.findViewById(R.id.cari_barang_nama);
                    //final ListView list_daf_barang = (ListView)DialogView.findViewById(R.id.list_daftar_barang);
                    final ListView list_daf_barang = (ListView)DialogView.findViewById(R.id.list_ubah_barang);

                    get_data_barang("");

                    cari_barang_nama.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            txt_cari_siswa(s.toString(), list_daf_barang, DialogView);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    BarangAdapter ba = new BarangAdapter(DialogView.getContext(), dataBarang);
                    list_daf_barang.setAdapter(ba);
                    final AlertDialog opt = builder.create();
                    opt.show();
                    list_daf_barang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                            tbl_det_barang.removeView(row);
                            //txt_frm_penjualan_customer.setText(dataBarang.get(i)[0]);
                            //txt_frm_barang_id_supplier.setText(dataSupplier.get(i)[0]);

                            String[] barangbaru = new String[2];
                            final String idb = dataBarang.get(i)[0];
                            final String nmb = dataBarang.get(i)[1];
                            barangbaru[0] = idb;
                            barangbaru[1] = nmb;
                            Bantu.detailPeminjaman.set(index, barangbaru);
                            //tbl_det_barang.removeAllViews();

                            tno.setText(String.valueOf(i));
                            tid.setText(Bantu.detailPeminjaman.get(i)[0]);
                            tna.setText(Bantu.detailPeminjaman.get(i)[1]);
                            row.addView(tno);
                            row.addView(tid);
                            row.addView(tna);
                            tbl_det_barang.addView(row);
                            opt.dismiss();

                        }
                    });
                    /*
                    final EditText txt_jumlah_barang = (EditText)DialogView.findViewById(R.id.txt_jumlah_barang);
                    txt_jumlah_barang.setText(Bantu.detailPenjualan.get(index)[3]);
                    builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int min_stok = get_min_stok(Bantu.detailPenjualan.get(index)[0]);
                            String[] det = Bantu.detailPenjualan.get(index);
                            int jml = Integer.parseInt(txt_jumlah_barang.getText().toString());
                            Double hrg = Double.parseDouble(det[2]);
                            Double st = hrg*jml;
                            det[3] = String.valueOf(jml);
                            det[4] = String.valueOf(st);
                            if(jml>=min_stok){
                                Toast.makeText(getApplicationContext(), "Stok Tidak Mencukupi!", Toast.LENGTH_LONG).show();
                            }else{
                                Bantu.detailPenjualan.set(index, det);
                                builder.create().dismiss();
                                get_detail();
                            }
                        }
                    });
                    */
                    builder.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bantu.detailPenjualan.remove(index);
                            builder.create().dismiss();
                            get_detail();
                        }
                    });

                    //builder.create().show();
                }
            });
        }
        //txt_frm_penjualan_total.setText(formatRupiah.format((double)grandTotal));
    }

    /*
    private void get_detail(){
        //Cursor c = db.rawQuery("SELECT * FROM Barang WHERE STATUS = 'Aktif'", null);
        dataBarang.clear();
        for(int i = 0; i<Bantu.detailPeminjaman.size(); i++){
            //while (c.moveToNext()){
            String[] ret = new String[3];
            ret[0] = String.valueOf(i+1);
            ret[1] = Bantu.detailPeminjaman.get(i)[0];
            ret[2] = Bantu.detailPeminjaman.get(i)[1];
            dataBarang.add(ret);
        }
        PeminjamanAdapter ca = new PeminjamanAdapter(getApplicationContext(), dataBarang);
        list_pinjam.setAdapter(ca);
    } */
}
