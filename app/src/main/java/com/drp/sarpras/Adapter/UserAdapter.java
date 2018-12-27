package com.drp.sarpras.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.drp.sarpras.R;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {
    Context context;
    ArrayList<String[]> list;
    LayoutInflater inflter;

    public UserAdapter(Context applicationContext, ArrayList<String[]> user_list){
        this.context = applicationContext;
        this.list = user_list;
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
            view = inflter.inflate(R.layout.list_user, null);
            TextView txt_nama = (TextView)view.findViewById(R.id.txt_list_user_nama);
            TextView txt_desc = (TextView)view.findViewById(R.id.txt_list_user_desc);
            TextView txt_role= (TextView)view.findViewById(R.id.txt_list_user_role);
            TextView txt_telp = (TextView)view.findViewById(R.id.txt_list_user_telp);
            txt_nama.setText(list.get(i)[1]);
            txt_desc.setText("Username : " +list.get(i)[2]);
            txt_role.setText("Role : " +list.get(i)[5]);
            txt_telp.setText("No Telp : "+list.get(i)[4]);
        }
        return view;
    }
}
