package com.example.apppegawai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class Activity_Edit extends AppCompatActivity {

    com.rengwuxian.materialedittext.MaterialEditText et_nopeg,et_nama,et_alamat,et_notelp;
    String nopeg,nama,alamat,notelp;
    Button btn_submit;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        et_nopeg            = findViewById(R.id.et_nopeg);
        et_nama             = findViewById(R.id.et_nama);
        et_alamat           = findViewById(R.id.et_alamat);
        et_notelp           = findViewById(R.id.et_notelp);
        btn_submit          = findViewById(R.id.btn_submit);

        progressDialog      = new ProgressDialog(this);

        getDataIntent();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Menambahkan Data...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                nopeg  = et_nopeg.getText().toString();
                nama   = et_nama.getText().toString();
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

    void getDataIntent(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            et_nopeg.setText(bundle.getString("no_pegawai"));
            et_nama.setText(bundle.getString("nama"));
            et_alamat.setText(bundle.getString("alamat"));
            et_notelp.setText(bundle.getString("no_telepon"));
        }else{
            et_nopeg.setText("");
            et_nama.setText("");
            et_alamat.setText("");
            et_notelp.setText("");
        }

    }

    void validasiData(){
        if(nopeg.equals("") || nama.equals("") || alamat.equals("") || notelp.equals("")){
            progressDialog.dismiss();
            Toast.makeText(Activity_Edit.this, "Periksa kembali data yang anda masukkan !", Toast.LENGTH_SHORT).show();
        }else {
            updateData();
        }
    }

    void updateData(){
        AndroidNetworking.post("http://localhost/api-pegawai/updatePegawai.php")
                .addBodyParameter("no_pegawai",""+nopeg)
                .addBodyParameter("nama",""+nama)
                .addBodyParameter("alamat",""+alamat)
                .addBodyParameter("no_telepon",""+ notelp)
                .setTag("Update Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("responEdit",""+response);
                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){
                                new AlertDialog.Builder(Activity_Edit.this)
                                        .setMessage("Berhasil Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_OK,i);
                                                Activity_Edit.this.finish();
                                            }
                                        })
                                        .show();
                            }else{
                                new AlertDialog.Builder(Activity_Edit.this)
                                        .setMessage("Gagal Mengupdate Data")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                Activity_Edit.this.finish();
                                            }
                                        })
                                        .show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
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