package com.drp.sarpras.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.drp.sarpras.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class BarangAdapter extends BaseAdapter {
    Context context;
    ArrayList<String[]> list;
    LayoutInflater inflter;

    public BarangAdapter(Context applicationContext, ArrayList<String[]> barang_list){
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
            view = inflter.inflate(R.layout.list_barang, null);
            TextView txt_nama = (TextView)view.findViewById(R.id.txt_list_barang_nama);
            TextView txt_kode = (TextView)view.findViewById(R.id.txt_list_barang_kode);
            TextView txt_desc = (TextView)view.findViewById(R.id.txt_list_barang_desc);

            txt_nama.setText(list.get(i)[1]);
            txt_desc.setText("Jenis Barang : "+list.get(i)[2]);
            txt_kode.setText("Kode Barang : "+list.get(i)[0]);
            //txt_desc.setText(list.get(i)[0]+" | "+list.get(i)[2]+" | "+list.get(i)[6]+" | "+list.get(i)[7]);

        }
        return view;
    }
}
