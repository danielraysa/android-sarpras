package com.drp.sarpras.Master;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.drp.sarpras.Bantu;
import com.drp.sarpras.R;

public class UserEditActivity extends AppCompatActivity {

    SQLiteDatabase db;

    //ArrayList<String[]> dataSupplier = new ArrayList<String[]>();
    EditText txt_frm_nama_user, txt_frm_username, txt_frm_password, txt_frm_no_telp;
    Button btn_edit_user, btn_hapus_user;
    Spinner spin_role;
    Integer id_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        txt_frm_nama_user = (EditText)findViewById(R.id.txt_frm_nama_user);
        txt_frm_username = (EditText)findViewById(R.id.txt_frm_username);
        txt_frm_password = (EditText)findViewById(R.id.txt_frm_password);
        txt_frm_no_telp = (EditText)findViewById(R.id.txt_frm_no_telp);
        spin_role = (Spinner)findViewById(R.id.spinner_role_edit);
        btn_edit_user = (Button)findViewById(R.id.btn_edit_user);
        btn_hapus_user = (Button)findViewById(R.id.btn_hapus_user);
        initDB();
        get_data();
        btn_edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_data();
            }
        });
        btn_hapus_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UserEditActivity.this);
                builder.setMessage("Apakah Anda Yakin?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete_data();
                            }
                        }).create().show();
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
        Intent itnt = getIntent();
        String[] data = itnt.getStringArrayExtra("detail_barang");
        id_user = Integer.parseInt(data[0]);
        txt_frm_nama_user.setText(data[1]);
        txt_frm_username.setText(data[2]);
        txt_frm_password.setText(data[3]);
        txt_frm_no_telp.setText(data[4]);
        if (data[5].equals("Administrator")) {
            spin_role.setSelection(0, true);
        }
        else {
            spin_role.setSelection(1);
        }
        //txt_frm_barang_kode.setEnabled(false);
    }

    private void update_data(){
        if(
                txt_frm_nama_user.getText().toString().equals("") ||
                        txt_frm_username.getText().toString().equals("") ||
                        txt_frm_password.getText().toString().equals("")
                ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Silahkan lengkkapi data!")
                    .setNegativeButton("Retry", null).create().show();
        }else{
            String sql_insert = "UPDATE User SET" +
                    " NAMA_USER = '"+txt_frm_nama_user.getText().toString()+"', " +
                    " USERNAME = '"+txt_frm_username.getText().toString()+"', " +
                    " PASSWORD = '"+txt_frm_password.getText().toString()+"', " +
                    " NO_TELP = '"+txt_frm_no_telp.getText().toString()+"' " +
                    " WHERE ID_USER = "+id_user.toString()+"";
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
    }

    private void delete_data(){

            String sql_insert = "DELETE FROM User" +
                    " WHERE ID_USER = "+id_user.toString()+"";
            try {
                db.execSQL(sql_insert);
                Toast.makeText(getApplicationContext(), "Data berhasil dihapus!", Toast.LENGTH_SHORT).show();
                finish();
            }catch (SQLException e){
                Toast.makeText(getApplicationContext(), "Data gagal dihapus!", Toast.LENGTH_SHORT).show();
                Log.e("SQL ERROR", e.getMessage());
            }

    }
}
