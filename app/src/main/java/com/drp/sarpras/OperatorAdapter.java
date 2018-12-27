package com.drp.sarpras;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.drp.sarpras.Master.DataUserActivity;
import com.drp.sarpras.Transaksi.Peminjaman;
import com.drp.sarpras.Transaksi.Pengembalian;

public class OperatorAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    //Song Song;
    //Song1 Song1;
    //Song2 Song2;
    private final Context context;
    String [] name={"Peminjaman","Pengembalian"};
    String [] artis={"Tambah data peminjaman","Pengembalian barang"};
    // menampilkan list item dalam bentuk text dengan tipe data string dengan variable name
    int[] picture = new int[]{R.drawable.book, R.drawable.folders};

    LayoutInflater inflater;
    public OperatorAdapter(Context context) {
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.operator_list, parent, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tv1.setText(name[position]);
        holder.tv2.setText(artis[position]);
        holder.tv1.setOnClickListener(clickListener);
        holder.tv2.setOnClickListener(clickListener);
        holder.imageView.setImageResource(picture[position]);
        holder.imageView.setOnClickListener(clickListener);
        holder.tv1.setTag(holder);
        holder.tv2.setTag(holder);
        holder.imageView.setTag(holder);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //member aksi saat cardView diklik berdasarkan posisi tertentu
            RecyclerViewHolder vholder = (RecyclerViewHolder) v.getTag();

            int position = vholder.getPosition();

            if(position==0){
                mChapie();
            }
            if(position==1){
                mstrange();
            }
        }
        void mChapie(){
            //FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            //Song=new Song();
            //manager.beginTransaction().add(R.id.main, Song).addToBackStack(null).commit();
            Intent i = new Intent(context,Peminjaman.class);
            context.startActivity(i);

        }
        void mstrange(){
            //FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            //Song1=new Song1();
            //manager.beginTransaction().add(R.id.main, Song1).addToBackStack(null).commit();
            Intent i = new Intent(context,Pengembalian.class);
            context.startActivity(i);
        }
    };

    @Override
    public int getItemCount() {

        return name.length;
    }
}
