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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.act.PictureSPM;
import com.ngs_tech.newjsb.models.SpmSatuModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SpmSelainSatu extends RecyclerView.Adapter<SpmSelainSatu.ViewHolder> {

    private Context context;
    private List<SpmSatuModel> list;

    public SpmSelainSatu(Context context, List<SpmSatuModel> spmsatuModelList) {
        this.context    = context;
        this.list       = spmsatuModelList;
    }

    @NonNull
    @Override
    public SpmSelainSatu.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_spm_selain_satu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SpmSelainSatu.ViewHolder holder, int position) {
        SpmSatuModel spmSatuModel = list.get(position);
        holder.tvId.setText(spmSatuModel.getId());
        holder.tvIdPenomoran.setText(spmSatuModel.getPenomoran());
        holder.tvIndikator.setText(spmSatuModel.getIndikator());
        holder.tvSubIndikator.setText("  - "+spmSatuModel.getSub_indikator());

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
                Intent intent;
                switch (checkedId){
                    case R.id.radio_merah:
                        holder.penilaian = "1";
                        intent = new Intent(context, PictureSPM.class);
                        intent.putExtra("id_gambar", holder.tvId.getText().toString());
                        intent.putExtra("id_gerbang", holder.idGerbang);
                        intent.putExtra("uuid", holder.user_id);
                        context.startActivity(intent);
                        break;
                    case R.id.radio_kuning:
                        holder.penilaian = "2";
                        intent = new Intent(context, PictureSPM.class);
                        intent.putExtra("id_gambar", holder.tvId.getText().toString());
                        intent.putExtra("id_gerbang", holder.idGerbang);
                        intent.putExtra("uuid", holder.user_id);
                        context.startActivity(intent);
                        break;
                    case R.id.radio_hijau:
                        holder.penilaian = "3";
                        intent = new Intent(context, PictureSPM.class);
                        intent.putExtra("id_gambar", holder.tvId.getText().toString());
                        intent.putExtra("id_gerbang", holder.idGerbang);
                        intent.putExtra("uuid", holder.user_id);
                        context.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        holder.imgSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linList.setBackgroundColor(Color.LTGRAY);
                AndroidNetworking.get(holder.HttpUrlInput+holder.user_id+"/"+holder.idGerbang+"/"+holder.tvId.getText().toString())
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                AndroidNetworking.post(holder.HttpUrlEdit)
                                        .addBodyParameter("uuid", holder.user_id)
                                        .addBodyParameter("id_list", holder.tvId.getText().toString())
                                        .addBodyParameter("id_gerbang", holder.idGerbang)
                                        .addBodyParameter("kon_saat_ini", holder.etKondisiSaatIni.getText().toString())
                                        .addBodyParameter("tolak_ukur", holder.etTolakUkur.getText().toString())
                                        .addBodyParameter("kriteria", holder.penilaian)
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                Toast.makeText(context, "Edit", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                Toast.makeText(context, "Gagal Edit", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }

                            @Override
                            public void onError(ANError anError) {
                                AndroidNetworking.post(holder.HttpUrlInput)
                                        .addBodyParameter("uuid", holder.user_id)
                                        .addBodyParameter("id_gerbang", holder.idGerbang)
                                        .addBodyParameter("id_list", holder.tvId.getText().toString())
                                        .addBodyParameter("kon_saat_ini", holder.etKondisiSaatIni.getText().toString())
                                        .addBodyParameter("tolak_ukur", holder.etTolakUkur.getText().toString())
                                        .addBodyParameter("kriteria", holder.penilaian)
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                Toast.makeText(context, "Input", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                Toast.makeText(context, "Gagal Input", Toast.LENGTH_LONG).show();
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
        private String user_id=null;

        private LinearLayout linList;
        private TextView tvId, tvIdPenomoran, tvIndikator, tvSubIndikator;
        private EditText etKondisiSaatIni, etTolakUkur;
        private RadioGroup rdInfoKondisi;
        private ImageView imgSubmit;

        private String penilaian = "";
        private String idGerbang = "";

        private String HttpGetUserData  = "http://114.7.131.86/toll_api/index.php/api/User/";

        private String HttpUrlInput     = "http://114.7.131.86/toll_api/index.php/api/PostSpm/";

        private String HttpUrlEdit      = "http://114.7.131.86/toll_api/index.php/api/PutSpm/";

//        private Spinner spinJalur;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linList                 = itemView.findViewById(R.id.lin_list);

            tvId                    = itemView.findViewById(R.id.tv_id);
            tvIdPenomoran           = itemView.findViewById(R.id.tv_id_penomoran);
            tvIndikator             = itemView.findViewById(R.id.tv_indikator);
            tvSubIndikator          = itemView.findViewById(R.id.tv_sub_indikator);

            etKondisiSaatIni        = itemView.findViewById(R.id.et_kon_saat_ini);
            etTolakUkur             = itemView.findViewById(R.id.et_tolak_ukur);

            rdInfoKondisi   = itemView.findViewById(R.id.radio_kondisi);

            imgSubmit   = itemView.findViewById(R.id.img_submit);

            AndroidNetworking.initialize(context);
            firebaseAuth         = FirebaseAuth.getInstance();
            user_id              = firebaseAuth.getCurrentUser().getUid();

//            spinJalur           = itemView.findViewById(R.id.spinner_jalur);
//            ArrayAdapter<CharSequence> adapterJalur = ArrayAdapter.createFromResource(context, R.array.spin_jalur, R.layout.spinner_text);
//            adapterJalur.setDropDownViewResource(R.layout.simple_spinner_dropdown);
//            spinJalur.setAdapter(adapterJalur);
        }
    }
}
