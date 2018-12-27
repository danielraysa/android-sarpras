package com.drp.sarpras;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    //deklarasi variabel reyclerview
    RecyclerView recyclerView;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDB();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //menampilkan reyclerview yang ada pada file layout dengan id reycler view

        AdminAdapter adapter = new AdminAdapter(this);
        OperatorAdapter adapter_new = new OperatorAdapter(this);

        Bundle b = getIntent().getExtras();

        if (b.getCharSequence("role").equals("Administrator")) {
            recyclerView.setAdapter(adapter);
            //menset nilai dari adapter
            recyclerView.setHasFixedSize(true);
            //menset setukuran
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else {
            recyclerView.setAdapter(adapter_new);
            //menset nilai dari adapter
            recyclerView.setHasFixedSize(true);

            //menset setukuran
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

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
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
