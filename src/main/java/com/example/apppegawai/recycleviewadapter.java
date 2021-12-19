package com.example.apppegawai;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
public class recycleviewadapter extends RecyclerView.Adapter<recycleviewadapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<String> array_nopeg, array_nama, array_alamat, array_notelp;
    ProgressDialog progressDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_nopeg, tv_nama, tv_alamat, tv_notelp;
        public CardView cv_main;

        public MyViewHolder(View view) {
            super(view);
            cv_main = itemView.findViewById(R.id.cv_main);
            tv_nopeg = itemView.findViewById(R.id.tv_nopeg);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_alamat = itemView.findViewById(R.id.tv_alamat);
            tv_notelp = itemView.findViewById(R.id.tv_notelp);

            progressDialog = new ProgressDialog(mContext);
        }
    }

    public recycleviewadapter(Context mContext, ArrayList<String> array_nopeg, ArrayList<String> array_nama, ArrayList<String> array_alamat, ArrayList<String> array_notelp) {
        super();
        this.mContext = mContext;
        this.array_nopeg = array_nopeg;
        this.array_nama = array_nama;
        this.array_alamat = array_alamat;
        this.array_notelp = array_notelp;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.recyclerview, parent, false);
        return new recycleviewadapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_nopeg.setText(array_nopeg.get(position));
        holder.tv_nama.setText(array_nama.get(position));
        holder.tv_alamat.setText(array_alamat.get(position));
        holder.tv_notelp.setText(array_notelp.get(position));
        holder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Activity_Edit.class);
                i.putExtra("no_pegawai", array_nopeg.get(position));
                i.putExtra("nama", array_nama.get(position));
                i.putExtra("alamat", array_alamat.get(position));
                i.putExtra("no_telepon", array_notelp.get(position));
                ((MainActivity) mContext).startActivityForResult(i, 2);
            }
        });
        holder.cv_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder((MainActivity) mContext)
                        .setMessage("Ingin menghapus nomor pegawai " + array_nopeg.get(position) + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Menghapus...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                AndroidNetworking.post("http://localhost/api-pegawai/deletePegawai.php")
                                        .addBodyParameter("noinduk", "" + array_nopeg.get(position))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                progressDialog.dismiss();
                                                try {
                                                    Boolean status = response.getBoolean("status");
                                                    Log.d("status", "" + status);
                                                    String result = response.getString("result");
                                                    if (status) {
                                                        if (mContext instanceof MainActivity) {
                                                            ((MainActivity) mContext).scrollRefresh();
                                                        }
                                                    } else {
                                                        Toast.makeText(mContext, "" + result, Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                anError.printStackTrace();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return array_nopeg.size();
    }
}
