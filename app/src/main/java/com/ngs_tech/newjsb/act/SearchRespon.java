package com.ngs_tech.newjsb.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.adapters.AdapterResponSPM;
import com.ngs_tech.newjsb.models.HasilSpmModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchRespon extends AppCompatActivity {

    private RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<HasilSpmModel> hasilSpmModelist;
    private RecyclerView.Adapter adapter;

    private String urlSearch = "http://114.7.131.86/toll_api/index.php/api/GetResponSpmSearch/";

    private String getTGL ="";
    private String getBLN ="";
    private String getTHN ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_respon);

        Intent intent = getIntent();
        getTGL = intent.getStringExtra("tgl");
        getBLN = intent.getStringExtra("bln");
        getTHN = intent.getStringExtra("thn");

        mList                   = findViewById(R.id.recycle_alat_tol);
        hasilSpmModelist        = new ArrayList<>();
        adapter                 = new AdapterResponSPM(getApplicationContext(), hasilSpmModelist);
        linearLayoutManager     = new LinearLayoutManager(SearchRespon.this);
        dividerItemDecoration   = new DividerItemDecoration(mList.getContext(),linearLayoutManager.getOrientation());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        getData(getTGL, getBLN, getTHN);

    }

    private void getData(String getTGL, String getBLN, String getTHN) {
        final ProgressDialog progressDialog = new ProgressDialog(SearchRespon.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.get(urlSearch+getTGL+"/"+getBLN+"/"+getTHN)
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
                            Toast.makeText(SearchRespon.this, "Data Belum Ada...", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
    }
}
