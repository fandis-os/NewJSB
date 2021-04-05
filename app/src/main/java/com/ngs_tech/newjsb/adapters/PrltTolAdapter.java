package com.ngs_tech.newjsb.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.act.PictureSPM;
import com.ngs_tech.newjsb.act.TakePicture;
import com.ngs_tech.newjsb.models.PeralatanTolModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PrltTolAdapter extends RecyclerView.Adapter<PrltTolAdapter.ViewHolder> {

    private Context context;
    private List<PeralatanTolModel> list;

    public PrltTolAdapter(Context context, List<PeralatanTolModel> alatTolModelList) {
        this.context    = context;
        this.list       = alatTolModelList;
    }

    @NonNull
    @Override
    public PrltTolAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_prlt_tol, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PrltTolAdapter.ViewHolder holder, int position) {
        PeralatanTolModel peralatanTolModel = list.get(position);

        holder.firebaseFirestore    = FirebaseFirestore.getInstance();
        holder.firebaseAuth         = FirebaseAuth.getInstance();
        holder.user_id              = holder.firebaseAuth.getCurrentUser().getUid();

        holder.tvNamaAlat.setText(peralatanTolModel.getNama_alat());
        holder.tvId.setText(peralatanTolModel.getId_alat());

        AndroidNetworking.get(holder.HttpGetUserData+holder.user_id)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                holder.idGerbang = jsonObject.getString("status_gerbang");
                                holder.tvIdGerbang.setText(holder.idGerbang);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });



        holder.rdInfoKondisi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_merah:
                        holder.kondisi="1";
//                        holder.
                        Intent intent = new Intent(context, PictureSPM.class);
                        intent.putExtra("id_gambar", holder.tvId.getText().toString());
                        intent.putExtra("id_gerbang", holder.tvIdGerbang.getText().toString());
                        intent.putExtra("uuid", holder.user_id);
                        context.startActivity(intent);
                        break;
                    case R.id.radio_kuning:
                        holder.kondisi="2";
                        break;
                    case R.id.radio_hijau:
                        holder.kondisi="3";
                        break;
                    default:
                        break;
                }
            }
        });

        holder.imgSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.etKeterangan.getText().toString();
                holder.tvId.getText().toString();
//                Toast.makeText(context, holder.kondisi, Toast.LENGTH_LONG).show();
                holder.relList.setBackgroundColor(Color.LTGRAY);

                AndroidNetworking.get(holder.HttpUrl+"/"+holder.user_id+"/"+holder.idGerbang+"/"+holder.tvId.getText().toString())
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                AndroidNetworking.post(holder.HttpUrlEdit)
                                        .addBodyParameter("uuid", holder.user_id)
                                        .addBodyParameter("id_alat", holder.tvId.getText().toString())
                                        .addBodyParameter("kondisi", holder.kondisi)
                                        .addBodyParameter("keterangan", holder.etKeterangan.getText().toString())
                                        .addBodyParameter("id_gerbang", holder.tvIdGerbang.getText().toString())
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                Toast.makeText(context, "Berhasil Edit", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                Toast.makeText(context, "Gagal Edit", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }

                            @Override
                            public void onError(ANError anError) {
                                AndroidNetworking.post(holder.HttpUrl)
                                        .addBodyParameter("uuid", holder.user_id)
                                        .addBodyParameter("id_alat", holder.tvId.getText().toString())
                                        .addBodyParameter("kondisi", holder.kondisi)
                                        .addBodyParameter("keterangan", holder.etKeterangan.getText().toString())
                                        .addBodyParameter("id_gerbang", holder.tvIdGerbang.getText().toString())
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                Toast.makeText(context, "Inserted...", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                Toast.makeText(context, "Failed...", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private FirebaseAuth firebaseAuth;
        private FirebaseFirestore firebaseFirestore;
        private String user_id=null;
        private String idGerbang;
        private String statusSubmit;

        private TextView tvMerah, tvKuning, tvHijau;

        private TextView tvNamaAlat, tvStatus;
        private TextView tvIdAlat, tvId, tvIdGerbang;
        private RadioGroup rdInfoKondisi;

        private ImageView imgSubmit;
        private EditText etKeterangan;

        private RelativeLayout relList;

        private String kondisi = "";
        private int jumlah;

        private String HttpUrl = "http://114.7.131.86/toll_api/index.php/api/PostKondisi";
        private String HttpUrlEdit = "http://114.7.131.86/toll_api/index.php/api/PutKondisi";
        private String HttpGetUserData = "http://114.7.131.86/toll_api/index.php/api/User/";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMerah     = itemView.findViewById(R.id.tv_merah);
            tvKuning    = itemView.findViewById(R.id.tv_kuning);
            tvHijau     = itemView.findViewById(R.id.tv_hijau);

            tvNamaAlat  = itemView.findViewById(R.id.tv_nama_alat);
            tvIdGerbang = itemView.findViewById(R.id.tv_id_gerbang);
            tvIdAlat    = itemView.findViewById(R.id.tv_id_alat);
            tvId        = itemView.findViewById(R.id.tv_id);
            tvStatus    = itemView.findViewById(R.id.tv_status);

            etKeterangan    = itemView.findViewById(R.id.et_keterangan);

            imgSubmit   = itemView.findViewById(R.id.img_submit);

            rdInfoKondisi   = itemView.findViewById(R.id.radio_kondisi);

            relList = itemView.findViewById(R.id.rel_list);

            AndroidNetworking.initialize(context);
        }
    }
}
