package com.ngs_tech.newjsb.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.act.PictureClose;
import com.ngs_tech.newjsb.models.HasilSpmModel;

import org.json.JSONArray;

import java.util.List;

public class AdapterResponSPM extends RecyclerView.Adapter<AdapterResponSPM.ViewHolder> {

    private Context context;
    private List<HasilSpmModel> list;

    public AdapterResponSPM(Context context, List<HasilSpmModel> hasilSpmModels) {
        this.context    = context;
        this.list       = hasilSpmModels;
    }

    @NonNull
    @Override
    public AdapterResponSPM.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_respon_spm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterResponSPM.ViewHolder holder, int position) {
        AndroidNetworking.initialize(context);
        final HasilSpmModel hasilSpmModel = list.get(position);
        if (hasilSpmModel.getKriteria().equals("1")){
            holder.tvKriteria.setText("Tidak Memenuhi");
        }else if (hasilSpmModel.getKriteria().equals("3")){
            holder.tvKriteria.setText("Memenuhi");
        }

//        holder.tvKat.setText(hasilSpmModel.getNama_kat());
        holder.tvInd.setText(hasilSpmModel.getIndikator());
        holder.tvSub.setText(hasilSpmModel.getSub_indikator());
        holder.tvKondisi.setText(hasilSpmModel.getKon_saat_ini());
        holder.tvTolakUkur.setText(hasilSpmModel.getTolak_ukur());
        holder.tvSTA.setText(hasilSpmModel.getSta());
        holder.tvLajur.setText(hasilSpmModel.getLajur());
        holder.tvJalur.setText(hasilSpmModel.getJalur());

        holder.responKerjakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
//                Toast.makeText(context, "Respon", Toast.LENGTH_LONG).show();
                intent = new Intent(context, PictureClose.class);
                intent.putExtra("uuid", hasilSpmModel.getUuid());
                intent.putExtra("gambar", hasilSpmModel.getId_gambar());
                intent.putExtra("id_alat", hasilSpmModel.getId_list());
                intent.putExtra("id_gerbang", hasilSpmModel.getId_gerbang());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvInd, tvSub;
        private TextView tvKondisi;
        private TextView tvTolakUkur;
        private TextView tvKriteria, tvProses;
        private TextView tvSTA, tvLajur, tvJalur;

        private ImageView responKerjakan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvInd       = itemView.findViewById(R.id.tv_ind);
            tvSub       = itemView.findViewById(R.id.tv_sub);

            tvKondisi   = itemView.findViewById(R.id.tv_kondisi);

            tvTolakUkur = itemView.findViewById(R.id.tv_tolak_ukur);

            tvKriteria  = itemView.findViewById(R.id.tv_kriteria);
            tvProses    = itemView.findViewById(R.id.tv_status);

            tvSTA       = itemView.findViewById(R.id.tv_sta);
            tvLajur     = itemView.findViewById(R.id.tv_lajur);
            tvJalur     = itemView.findViewById(R.id.tv_jalur);

            responKerjakan = itemView.findViewById(R.id.img_submit);

        }
    }
}
