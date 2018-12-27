package com.drp.sarpras.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.drp.sarpras.MainActivity;
import com.drp.sarpras.R;
import com.drp.sarpras.Transaksi.Pengembalian;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Daniel Raysa Putra on 13/12/2018.
 */
public class PeminjamanAdapter extends BaseAdapter {
    Context context;
    ArrayList<String[]> list;
    LayoutInflater inflter;

    public PeminjamanAdapter(Context applicationContext, ArrayList<String[]> barang_list){
        this.context = applicationContext;
        this.list = barang_list;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if(!list.isEmpty()){
            view = inflter.inflate(R.layout.list_pinjaman, null);
            TextView nama = (TextView)view.findViewById(R.id.nama_peminjam);
            TextView tglpinjam = (TextView)view.findViewById(R.id.tanggal_pinjam);
            TextView tglkembali = (TextView)view.findViewById(R.id.tanggal_kembali);
            TextView txtstatus = (TextView) view.findViewById(R.id.status);

            nama.setText(list.get(i)[2]);
            tglpinjam.setText(list.get(i)[3]);
            tglkembali.setText(list.get(i)[4]);
            String tanggal = list.get(i)[4];
            String tanggalbaru = tanggal.replace("/","-");

            Calendar ca = Calendar.getInstance();
            String datenow = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date kembali = null;
            Date date_skrg = null;
            try {
                kembali = format.parse(tanggalbaru);
                date_skrg = format.parse(datenow);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date_skrg.after(kembali)) {
                txtstatus.setText("Terlambat");
                txtstatus.setTextColor(view.getResources().getColor(R.color.cardview123));
            }
            else{
                txtstatus.setText("Terpinjam");
            }
        }
        return view;
    }
}
