package com.example.apppegawai;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class Activity_Add extends AppCompatActivity {

    com.rengwuxian.materialedittext.MaterialEditText et_nopeg,et_nama,et_alamat,et_notelp;
    String nopeg,nama,alamat,notelp;
    Button btn_submit;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et_nopeg          = findViewById(R.id.et_nopeg);
        et_nama             = findViewById(R.id.et_nama);
        et_alamat           = findViewById(R.id.et_alamat);
        et_notelp             = findViewById(R.id.et_notelp);
        btn_submit          = findViewById(R.id.btn_submit);

        progressDialog      = new ProgressDialog(this);

        btn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Menambahkan Data...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                nopeg = et_nopeg.getText().toString();
                nama = et_nama.getText().toString();
                alamat = et_alamat.getText().toString();
                notelp = et_notelp.getText().toString();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        validasiData();
                    }
                },1000);
            }
        });

    }

    void validasiData(){
        if(nopeg.equals("") || nama.equals("") || alamat.equals("") || notelp.equals("")){
            progressDialog.dismiss();
            Toast.makeText(Activity_Add.this, "Periksa kembali data yang anda masukkan !", Toast.LENGTH_SHORT).show();
        }else {
            kirimData();
        }
    }

    void kirimData(){
        AndroidNetworking.post("http://localhost/api-pegawai/tambahPegawai.php") //sesuaikan dengan api server kalian
                .addBodyParameter("no_pegawai",""+nopeg)
                .addBodyParameter("nama",""+nama)
                .addBodyParameter("alamat",""+alamat)
                .addBodyParameter("no_telepon",""+notelp)
                .setPriority(Priority.MEDIUM)
                .setTag("Tambah Data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekTambah",""+response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            Toast.makeText(Activity_Add.this, ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(Activity_Add.this)
                                        .setMessage("Berhasil Menambahkan Data !")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_OK,i);
                                                Activity_Add.this.finish();
                                            }
                                        })
                                        .show();
                            }
                            else{
                                new AlertDialog.Builder(Activity_Add.this)
                                        .setMessage("Gagal Menambahkan Data !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                Activity_Add.this.finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ErrorTambahData",""+anError.getErrorBody());
                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.menu_back){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}