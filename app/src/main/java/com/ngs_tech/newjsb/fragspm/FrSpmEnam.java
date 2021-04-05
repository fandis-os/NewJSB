package com.ngs_tech.newjsb.fragspm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.adapters.AdapterSpmSelain;
import com.ngs_tech.newjsb.adapters.SpmSelainSatu;
import com.ngs_tech.newjsb.models.SpmSatuModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FrSpmEnam extends Fragment {

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<SpmSatuModel> spmsatuModelList;
    private RecyclerView.Adapter adapter;

    private String url  = "http://mudikqu.com/toll_api/index.php/api/ListSpm/6";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_spm_enam, container, false);

        AndroidNetworking.initialize(getContext());

        mList                   = view.findViewById(R.id.recycle_alat_tol);
        spmsatuModelList        = new ArrayList<>();
        adapter                 = new AdapterSpmSelain(getContext(), spmsatuModelList);
        linearLayoutManager     = new LinearLayoutManager(getActivity());
        dividerItemDecoration   = new DividerItemDecoration(mList.getContext(),linearLayoutManager.getOrientation());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        getData();

        return view;
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        AndroidNetworking.get(url)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0; i<response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                SpmSatuModel spmSatuModel = new SpmSatuModel();
                                spmSatuModel.setId(jsonObject.getString("id"));
                                spmSatuModel.setPenomoran(jsonObject.getString("penomoran"));
                                spmSatuModel.setId_kat(jsonObject.getString("id_kat"));
                                spmSatuModel.setNama_kat(jsonObject.getString("nama_kat"));
                                spmSatuModel.setIndikator(jsonObject.getString("indikator"));
                                spmSatuModel.setSub_indikator(jsonObject.getString("sub_indikator"));

                                spmsatuModelList.add(spmSatuModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), anError.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }
}
