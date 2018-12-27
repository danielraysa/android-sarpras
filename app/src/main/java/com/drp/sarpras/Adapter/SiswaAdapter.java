package com.drp.sarpras.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.drp.sarpras.R;

import java.util.ArrayList;

public class SiswaAdapter extends BaseAdapter {
    Context context;
    ArrayList<String[]> list;
    LayoutInflater inflter;

    public SiswaAdapter(Context applicationContext, ArrayList<String[]> siswa_list){
        this.context = applicationContext;
        this.list = siswa_list;
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
            view = inflter.inflate(R.layout.list_siswa, null);
            TextView txt_nama = (TextView)view.findViewById(R.id.txt_list_siswa_nama);
            TextView txt_nis = (TextView)view.findViewById(R.id.txt_list_siswa_nis);
            TextView txt_telp = (TextView)view.findViewById(R.id.txt_list_siswa_telp);
            txt_nama.setText(list.get(i)[1]);
            txt_nis.setText("NIS : "+list.get(i)[0]);
            txt_telp.setText("No. Telp : "+list.get(i)[2]);
        }
        return view;
    }
}
