package com.ngs_tech.newjsb.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.adapters.AdapterHasilSpm;
import com.ngs_tech.newjsb.adapters.AdapterResponSPM;
import com.ngs_tech.newjsb.models.HasilSpmModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResponSPM extends AppCompatActivity {

    private RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<HasilSpmModel> hasilSpmModelist;
    private RecyclerView.Adapter adapter;

    private String url = "http://114.7.131.86/toll_api/index.php/api/GetResponSpm/";
    private String urlSearch = "http://114.7.131.86/toll_api/index.php/api/GetResponSpmSearch/";
    private FirebaseAuth firebaseAuth;
    private String user_id=null;

    private TextView tvTanggal;
    String currentTahun;
    Date currentTime;
    private Calendar cal;
    private int mYear, mMonth, mDay;

    private TextView tvTGL, tvBLN, tvTHN;
    private String getTGL ="";
    private String getBLN ="";
    private String getTHN ="";

    private ImageView imgSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respon_spm);

        mList                   = findViewById(R.id.recycle_alat_tol);
        hasilSpmModelist        = new ArrayList<>();
        adapter                 = new AdapterResponSPM(getApplicationContext(), hasilSpmModelist);
        linearLayoutManager     = new LinearLayoutManager(ResponSPM.this);
        dividerItemDecoration   = new DividerItemDecoration(mList.getContext(),linearLayoutManager.getOrientation());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        tvTanggal = findViewById(R.id.tv_tanggal);
        imgSearch = findViewById(R.id.img_search);

        tvTGL = findViewById(R.id.tvTGL);
        tvBLN = findViewById(R.id.tvBLN);
        tvTHN = findViewById(R.id.tvTHN);

        tvTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                mYear   = cal.get(Calendar.YEAR);
                mMonth  = cal.get(Calendar.MONTH);
                mDay    = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ResponSPM.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                        Date date = new Date(year, month, dayOfMonth-1);
                        String dayOfWeek = simpleDateFormat.format(date);

                        int jumlahMonth = month+1;
                        String namaMonth = "";
                        String getTanggal = "";

                        if(dayOfMonth < 10){
                            getTanggal = "0" + dayOfMonth;
                        }else {
                            getTanggal = "" + dayOfMonth;
                        }

                                    if (jumlahMonth==1){
                                        namaMonth = "Jan";
                                    }else if (jumlahMonth==2){
                                        namaMonth = "Feb";
                                    }else if (jumlahMonth==3){
                                        namaMonth = "Mar";
                                    }else if (jumlahMonth==4){
                                        namaMonth = "Apr";
                                    }else if (jumlahMonth==5){
                                        namaMonth = "Mei";
                                    }else if (jumlahMonth==6){
                                        namaMonth = "Jun";
                                    }else if (jumlahMonth==7){
                                        namaMonth = "Jul";
                                    }else if (jumlahMonth==8){
                                        namaMonth = "Agu";
                                    }else if (jumlahMonth==9){
                                        namaMonth = "Sep";
                                    }else if (jumlahMonth==10){
                                        namaMonth = "Okt";
                                    }else if (jumlahMonth==11){
                                        namaMonth = "Nov";
                                    }else if (jumlahMonth==12){
                                        namaMonth = "Des";
                                    }

                        tvTanggal.setText(getTanggal + "-" + namaMonth + "-" + year);
                                    tvBLN.setText(namaMonth);
                                    tvTGL.setText(getTanggal);
                                    tvTHN.setText(String.valueOf(year));

                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTGL = tvTGL.getText().toString();
                getBLN = tvBLN.getText().toString();
                getTHN = tvTHN.getText().toString();
                if (getTGL.equals("") || getBLN.equals("") || getTHN.equals("")){
                    Toast.makeText(ResponSPM.this, "Pilih tanggal terlebih dahulu", Toast.LENGTH_LONG).show();
                }else {
                    getData(getTGL, getBLN, getTHN);
                }
            }
        });



        getData(getTGL, getBLN, getTHN);

    }

    private void getData(String getTGL, String getBLN, String getTHN) {
        final ProgressDialog progressDialog = new ProgressDialog(ResponSPM.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (getTGL.equals("") || getBLN.equals("") || getTHN.equals("")){
            AndroidNetworking.get(url)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i=0; i<response.length(); i++){
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    HasilSpmModel hasilSpmModel = new HasilSpmModel();
                                    hasilSpmModel.setUuid(jsonObject.getString("uuid"));
                                    hasilSpmModel.setId_gerbang(jsonObject.getString("id_gerbang"));
                                    hasilSpmModel.setId_list(jsonObject.getString("id_list"));
                                    hasilSpmModel.setSta(jsonObject.getString("sta"));
                                    hasilSpmModel.setLajur(jsonObject.getString("lajur"));
                                    hasilSpmModel.setJalur(jsonObject.getString("jalur"));
                                    hasilSpmModel.setKon_saat_ini(jsonObject.getString("kon_saat_ini"));
                                    hasilSpmModel.setTolak_ukur(jsonObject.getString("tolak_ukur"));
                                    hasilSpmModel.setKriteria(jsonObject.getString("kriteria"));
                                    hasilSpmModel.setStatus(jsonObject.getString("status"));
                                    hasilSpmModel.setId_gambar(jsonObject.getString("id_gambar"));
                                    hasilSpmModel.setCreate_at(jsonObject.getString("create_at"));
                                    hasilSpmModel.setIndikator(jsonObject.getString("indikator"));
                                    hasilSpmModel.setSub_indikator(jsonObject.getString("sub_indikator"));
                                    hasilSpmModel.setNama_kat(jsonObject.getString("nama_kat"));
                                    hasilSpmModelist.add(hasilSpmModel);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(ResponSPM.this, "Data Belum Ada...", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
        }else {
            Intent intent = new Intent(ResponSPM.this, SearchRespon.class);
            intent.putExtra("tgl", getTGL);
            intent.putExtra("bln", getBLN);
            intent.putExtra("thn", getTHN);
            startActivity(intent);
        }
    }
}
