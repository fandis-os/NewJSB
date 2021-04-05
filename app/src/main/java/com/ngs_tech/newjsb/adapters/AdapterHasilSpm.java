package com.ngs_tech.newjsb.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.models.HasilSpmModel;
import com.ngs_tech.newjsb.models.SpmSatuModel;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterHasilSpm extends RecyclerView.Adapter<AdapterHasilSpm.ViewHolder> {

    private Context context;
    private List<HasilSpmModel> list;

    public AdapterHasilSpm(Context context, List<HasilSpmModel> hasilSpmModels) {
        this.context    = context;
        this.list       = hasilSpmModels;
    }

    @NonNull
    @Override
    public AdapterHasilSpm.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hasil_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHasilSpm.ViewHolder holder, int position) {
        HasilSpmModel hasilSpmModel = list.get(position);

        if (hasilSpmModel.getKriteria().equals("1")){
            holder.tvKriteria.setText("Tidak Memenuhi");
        }else if (hasilSpmModel.getKriteria().equals("3")){
            holder.tvKriteria.setText("Memenuhi");
        }

        if (hasilSpmModel.getStatus().equals("0")){
            holder.tvProses.setText("Temuan");
        }else if (hasilSpmModel.getStatus().equals("1")){
            holder.tvProses.setText("Dalam pengerjaan");
        }else if (hasilSpmModel.getStatus().equals("2")){
            holder.tvProses.setText("Telah diperbaiki");
        }else {
            holder.tvProses.setText("-");
        }

        holder.tvKat.setText(hasilSpmModel.getNama_kat());
        holder.tvInd.setText(hasilSpmModel.getIndikator());
        holder.tvSub.setText(hasilSpmModel.getSub_indikator());
        holder.tvKondisi.setText(hasilSpmModel.getKon_saat_ini());
        holder.tvTolakUkur.setText(hasilSpmModel.getTolak_ukur());
        holder.tvSTA.setText(hasilSpmModel.getSta());
        holder.tvLajur.setText(hasilSpmModel.getLajur());
        holder.tvJalur.setText(hasilSpmModel.getJalur());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvKat, tvInd, tvSub;
        private TextView tvKondisi;
        private TextView tvTolakUkur;
        private TextView tvKriteria, tvProses;
        private TextView tvSTA, tvLajur, tvJalur;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKat       = itemView.findViewById(R.id.tv_kat);
            tvInd       = itemView.findViewById(R.id.tv_ind);
            tvSub       = itemView.findViewById(R.id.tv_sub);

            tvKondisi   = itemView.findViewById(R.id.tv_kondisi);

            tvTolakUkur = itemView.findViewById(R.id.tv_tolak_ukur);

            tvKriteria  = itemView.findViewById(R.id.tv_kriteria);
            tvProses    = itemView.findViewById(R.id.tv_status);

            tvSTA       = itemView.findViewById(R.id.tv_sta);
            tvLajur     = itemView.findViewById(R.id.tv_lajur);
            tvJalur     = itemView.findViewById(R.id.tv_jalur);
        }
    }
}
