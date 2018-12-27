package com.drp.sarpras;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    // ViewHolder akan mendeskripisikan item view yang ditempatkan di dalam RecyclerView.
    TextView tv1, tv2; //deklarasi textview
    ImageView imageView;  //deklarasi imageview

    public RecyclerViewHolder(View itemView) {

        super(itemView);

        tv1 = (TextView) itemView.findViewById(R.id.daftar_judul);
        tv2 = (TextView) itemView.findViewById(R.id.artist);
        //menampilkan text dari widget CardView pada id daftar_judul
        imageView = (ImageView) itemView.findViewById(R.id.daftar_icon);
        //menampilkan gambar atau icon pada widget Cardview pada id daftar_icon
    }
}
