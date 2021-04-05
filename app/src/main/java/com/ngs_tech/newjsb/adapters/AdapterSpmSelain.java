package com.ngs_tech.newjsb.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.act.PictureSPM;
import com.ngs_tech.newjsb.models.SpmSatuModel;
import com.ngs_tech.newjsb.utils.GenerateImageID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AdapterSpmSelain extends RecyclerView.Adapter<AdapterSpmSelain.ViewHolder> {

    private Context context;
    private List<SpmSatuModel> list;

    public AdapterSpmSelain(Context context, List<SpmSatuModel> spmsatuModelList) {
        this.context    = context;
        this.list       = spmsatuModelList;
    }

    @NonNull
    @Override
    public AdapterSpmSelain.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_spm_selain_satu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSpmSelain.ViewHolder holder, int position) {
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

                switch (checkedId){
                    case R.id.radio_merah:
                        holder.penilaian = "1";
                        break;
                    case R.id.radio_hijau:
                        holder.penilaian = "3";
                        break;
                    default:
                        break;
                }
            }
        });

        holder.imgSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.getIDGambarBaru = GenerateImageID.randomString(10);
                AndroidNetworking.post(holder.HttpUrlInput)
                        .addBodyParameter("uuid", holder.user_id)
                        .addBodyParameter("id_gerbang", holder.idGerbang)
                        .addBodyParameter("id_list", holder.tvId.getText().toString())
                        .addBodyParameter("kon_saat_ini", holder.etKondisiSaatIni.getText().toString())
                        .addBodyParameter("tolak_ukur", holder.etTolakUkur.getText().toString())
                        .addBodyParameter("kriteria", holder.penilaian)
                        .addBodyParameter("id_gambar", holder.getIDGambarBaru)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Intent intent;
                                if (holder.penilaian.equals("1")){
                                    intent = new Intent(context, PictureSPM.class);
                                    intent.putExtra("id_alat", holder.tvId.getText().toString());
                                    intent.putExtra("id_gerbang", holder.idGerbang);
                                    intent.putExtra("uuid", holder.user_id);
                                    intent.putExtra("id_gambar", holder.getIDGambarBaru);
                                    context.startActivity(intent);
                                }else {
                                    intent = new Intent(context, PictureSPM.class);
                                    intent.putExtra("id_alat", holder.tvId.getText().toString());
                                    intent.putExtra("id_gerbang", holder.idGerbang);
                                    intent.putExtra("uuid", holder.user_id);
                                    intent.putExtra("id_gambar", holder.getIDGambarBaru);
                                    context.startActivity(intent);
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(context, "Gagal Input", Toast.LENGTH_LONG).show();
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
        private String getIDGambarBaru = "";

        private String penilaian = "";
        private String idGerbang = "";

        private String HttpGetUserData  = "http://114.7.131.86/toll_api/index.php/api/User/";

        private String HttpUrlInput     = "http://114.7.131.86/toll_api/index.php/api/PostSpm/";

        private String HttpUrlEdit      = "http://114.7.131.86/toll_api/index.php/api/PutSpm/";

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
        }
    }
}
