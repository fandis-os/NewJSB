package com.ngs_tech.newjsb.sub_act;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ngs_tech.newjsb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PengToll extends AppCompatActivity {

    private static final String HttpGetUserData = "http://114.7.131.86/toll_api/index.php/api/User/";

    private static final String HttpCekInputBatang = "http://114.7.131.86/toll_api/index.php/api/jsbcekbatang/";
    private static final String HttpCekInputKandeman = "http://114.7.131.86/toll_api/index.php/api/CekKandeman/";
    private static final String HttpCekInputWeleri = "http://114.7.131.86/toll_api/index.php/api/CekWeleri/";
    private static final String HttpCekInputKendal = "http://114.7.131.86/toll_api/index.php/api/CekKendal/";
    private static final String HttpCekInputKaliwungu = "http://114.7.131.86/toll_api/index.php/api/CekKaliwungu/";
    private static final String HttpCekInputKalikangkung = "http://114.7.131.86/toll_api/index.php/api/CekKalikangkung/";

    private static final String HttpPostEtol = "http://114.7.131.86/toll_api/index.php/api/jsbetolbatang";

    private static final String HttpPostKendal = "http://114.7.131.86/toll_api/index.php/api/PostTollKendal";
    private static final String HttpPostKandeman = "http://114.7.131.86/toll_api/index.php/api/PostTollKandeman";
    private static final String HttpPostKalikangkung = "http://114.7.131.86/toll_api/index.php/api/PostTollKalikangkung";
    private static final String HttpPostKaliwungu = "http://114.7.131.86/toll_api/index.php/api/PostTollKaliwungu";
    private static final String HttpPostWeleri = "http://114.7.131.86/toll_api/index.php/api/PostTollWeleri";

    private String idGerbang, getIdGerbang;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id=null;
    private String getStatus=null;
    private String getStatusTanggal=null;

    private EditText etGol1, etGol2, etGol3, etGol4, etGol5;
//    private EditText etTujuan;
    private Spinner spinAsalGerbang, spinJenisEtol, spinTujuanGerbang;

    private TextView tvBtnPengtolEtol;
    private ProgressBar progresInput;

    private TextView tvIDGERBANG;

    String namaMonth = "";
    String tglFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peng_toll);

        AndroidNetworking.initialize(getApplicationContext());

        firebaseFirestore   =FirebaseFirestore.getInstance();
        firebaseAuth        =FirebaseAuth.getInstance();
        user_id             =firebaseAuth.getCurrentUser().getUid();

        tvIDGERBANG     = findViewById(R.id.status_gerbang);
        progresInput    = findViewById(R.id.progres_insert);
//        etTujuan        = findViewById(R.id.et_tujuan);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat sdftgl = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat sdfth = new SimpleDateFormat("yyyy", Locale.getDefault());

        String currentMont = sdf.format(new Date());
        String currentDay = sdftgl.format(new Date());
        String currentTahun = sdfth.format(new Date());

        spinTujuanGerbang = findViewById(R.id.spinner_tujuan_gerbang);

        if (currentMont.equals("01")){
            namaMonth = "Jan";
            tglFormat = currentDay+"-"+namaMonth+"-"+currentTahun;
        }else if (currentMont.equals("02")){
            namaMonth = "Feb";
            tglFormat = currentDay+"-"+namaMonth+"-"+currentTahun;
        }else if (currentMont.equals("03")){
            namaMonth = "Mar";
            tglFormat = currentDay+"-"+namaMonth+"-"+currentTahun;
        }else if (currentMont.equals("04")){
            namaMonth = "Apr";
            tglFormat = currentDay+"-"+namaMonth+"-"+currentTahun;
        }else if (currentMont.equals("05")){
            namaMonth = "Mei";
            tglFormat = currentDay+"-"+namaMonth+"-"+currentTahun;
        }else if (currentMont.equals("06")){
            namaMonth = "Jun";
            tglFormat = currentDay+"-"+namaMonth+"-"+currentTahun;
        }else if (currentMont.equals("07")){
            namaMonth = "Jul";
            tglFormat = currentDay+"-"+namaMonth+"-"+currentTahun;
        }else if (currentMont.equals("08")){
            namaMonth = "Agu";
            tglFormat = currentDay+"-"+namaMonth+"-"+currentTahun;
        }else if (currentMont.equals("09")){
            namaMonth = "Sep";
            tglFormat = currentDay+"-"+namaMonth+"-"+currentTahun;
        }else if (currentMont.equals("10")){
            namaMonth = "Okt";
            tglFormat = currentDay+"-"+namaMonth+"-"+currentTahun;
        }else if (currentMont.equals("11")){
            namaMonth = "Nov";
            tglFormat = currentDay+"-"+namaMonth+"-"+currentTahun;
        }else if (currentMont.equals("12")){
            namaMonth = "Des";
            tglFormat = currentDay+"-"+namaMonth+"-"+currentTahun;
        }

//        Toast.makeText(this, currentMont+" - "+currentDay, Toast.LENGTH_LONG).show();

        AndroidNetworking.get(HttpGetUserData+user_id)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                idGerbang   = jsonObject.getString("status_gerbang");
                                tvIDGERBANG.setText(idGerbang);
                            }

                            ArrayAdapter<CharSequence> adapterAsal = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_asal_menuju_batang, R.layout.spinner_text);
                            adapterAsal.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                            spinAsalGerbang.setAdapter(adapterAsal);

                            spinAsalGerbang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    final String getAsalG = spinAsalGerbang.getSelectedItem().toString();

                                    if (getAsalG.equals("Kandeman")){
                                        ArrayAdapter<CharSequence> adapterTujuan = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_asal_kandeman, R.layout.spinner_text);
                                        adapterTujuan.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                                        spinTujuanGerbang.setAdapter(adapterTujuan);
                                    }else if (getAsalG.equals("Weleri")){
                                        ArrayAdapter<CharSequence> adapterTujuan = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_asal_weleri, R.layout.spinner_text);
                                        adapterTujuan.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                                        spinTujuanGerbang.setAdapter(adapterTujuan);
                                    }else if (getAsalG.equals("Kendal")){
                                        ArrayAdapter<CharSequence> adapterTujuan = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_asal_kendal, R.layout.spinner_text);
                                        adapterTujuan.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                                        spinTujuanGerbang.setAdapter(adapterTujuan);
                                    }else if (getAsalG.equals("Kaliwungu")){
                                        ArrayAdapter<CharSequence> adapterTujuan = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_asal_kaliwungu, R.layout.spinner_text);
                                        adapterTujuan.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                                        spinTujuanGerbang.setAdapter(adapterTujuan);
                                    }else if (getAsalG.equals("Kalikangkung")){
                                        ArrayAdapter<CharSequence> adapterTujuan = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_asal_kalikangkung, R.layout.spinner_text);
                                        adapterTujuan.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                                        spinTujuanGerbang.setAdapter(adapterTujuan);
                                    }

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            spinTujuanGerbang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    final String getTujuan = spinTujuanGerbang.getSelectedItem().toString();
                                    if (getTujuan.equals("Kandeman")){
                                        getIdGerbang = "2";
                                    }else if (getTujuan.equals("Weleri")){
                                        getIdGerbang = "3";
                                    }else if (getTujuan.equals("Kendal")){
                                        getIdGerbang = "4";
                                    }else if (getTujuan.equals("Kaliwungu")){
                                        getIdGerbang = "5";
                                    }else if (getTujuan.equals("Kalikangkung")){
                                        getIdGerbang = "6";
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });



//                            if (idGerbang.equals("1")){
//                                ArrayAdapter<CharSequence> adapterAsal = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_asal_menuju_batang, R.layout.spinner_text);
//                                adapterAsal.setDropDownViewResource(R.layout.simple_spinner_dropdown);
//                                spinAsalGerbang.setAdapter(adapterAsal);
//                                etTujuan.setText("Batang");
//                            }else if (idGerbang.equals("2")){
//                                ArrayAdapter<CharSequence> adapterAsal = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_asal_menuju_kandeman, R.layout.spinner_text);
//                                adapterAsal.setDropDownViewResource(R.layout.simple_spinner_dropdown);
//                                spinAsalGerbang.setAdapter(adapterAsal);
//                                etTujuan.setText("Kandeman");
//                            }else if (idGerbang.equals("3")){
//                                ArrayAdapter<CharSequence> adapterAsal = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_asal_menuju_weleri, R.layout.spinner_text);
//                                adapterAsal.setDropDownViewResource(R.layout.simple_spinner_dropdown);
//                                spinAsalGerbang.setAdapter(adapterAsal);
//                                etTujuan.setText("Weleri");
//                            }else if (idGerbang.equals("4")){
//                                ArrayAdapter<CharSequence> adapterAsal = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_asal_menuju_kendal, R.layout.spinner_text);
//                                adapterAsal.setDropDownViewResource(R.layout.simple_spinner_dropdown);
//                                spinAsalGerbang.setAdapter(adapterAsal);
//                                etTujuan.setText("Kendal");
//                            }else if (idGerbang.equals("5")){
//                                ArrayAdapter<CharSequence> adapterAsal = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_asal_menuju_kaliwungu, R.layout.spinner_text);
//                                adapterAsal.setDropDownViewResource(R.layout.simple_spinner_dropdown);
//                                spinAsalGerbang.setAdapter(adapterAsal);
//                                etTujuan.setText("Kaliwungu");
//                            }else if (idGerbang.equals("6")){
//                                ArrayAdapter<CharSequence> adapterAsal = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_asal_menuju_kalikangkung, R.layout.spinner_text);
//                                adapterAsal.setDropDownViewResource(R.layout.simple_spinner_dropdown);
//                                spinAsalGerbang.setAdapter(adapterAsal);
//                                etTujuan.setText("Kalikangkung");
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

        etGol1 = findViewById(R.id.et_gol_1);
        etGol2 = findViewById(R.id.et_gol_2);
        etGol3 = findViewById(R.id.et_gol_3);
        etGol4 = findViewById(R.id.et_gol_4);
        etGol5 = findViewById(R.id.et_gol_5);

        spinAsalGerbang     = findViewById(R.id.spinner_asal_gerbang);
        spinJenisEtol       = findViewById(R.id.spinner_jns_etol);

        tvBtnPengtolEtol = findViewById(R.id.btn_submit_pengtol_etol);

        ArrayAdapter<CharSequence> adapterJenisETOL = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spin_jenis_etol, R.layout.spinner_text);
        adapterJenisETOL.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinJenisEtol.setAdapter(adapterJenisETOL);

        tvBtnPengtolEtol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getG1 = etGol1.getText().toString();
                final String getG2 = etGol2.getText().toString();
                final String getG3 = etGol3.getText().toString();
                final String getG4 = etGol4.getText().toString();
                final String getG5 = etGol5.getText().toString();

                final String getSpinGerbangAsal = spinAsalGerbang.getSelectedItem().toString();
                final String getSpinJenisEtol   = spinJenisEtol.getSelectedItem().toString();

                progresInput.setVisibility(View.VISIBLE);

//                BATANG
                if (idGerbang.equals("1")){
                    AndroidNetworking.get(HttpCekInputBatang+getSpinGerbangAsal+"/"+getSpinJenisEtol)
                            .setPriority(Priority.LOW)
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        for (int i=0; i<response.length(); i++){
                                            JSONObject jsonObject = response.getJSONObject(i);
                                            getStatus = jsonObject.getString("status");
                                        }
                                        if (getStatus.equals("success")){
                                            StringRequest stringRequest = new StringRequest(Request.Method.PUT, HttpPostEtol, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getApplicationContext(), "Inserted...", Toast.LENGTH_LONG).show();
                                                    progresInput.setVisibility(View.GONE);
                                                    etGol1.setText(null);
                                                    etGol2.setText(null);
                                                    etGol3.setText(null);
                                                    etGol4.setText(null);
                                                    etGol5.setText(null);
                                                    etGol1.requestFocus();
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_LONG).show();
                                                    progresInput.setVisibility(View.GONE);
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams(){
                                                    Map<String, String> params = new HashMap<String, String>();

                                                    params.put("gerbang", getSpinGerbangAsal);
                                                    params.put("jenis_etol", getSpinJenisEtol);
                                                    params.put("gol_1", getG1);
                                                    params.put("gol_2", getG2);
                                                    params.put("gol_3", getG3);
                                                    params.put("gol_4", getG4);
                                                    params.put("gol_5", getG5);

                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueuee = Volley.newRequestQueue(getApplicationContext());
                                            requestQueuee.add(stringRequest);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });
                }
//                KANDEMAN
                else if (getIdGerbang.equals("2")){
                    AndroidNetworking.get(HttpCekInputKandeman+getSpinGerbangAsal+"/"+getSpinJenisEtol+"/"+tglFormat)
                            .setPriority(Priority.LOW)
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        for (int i=0; i<response.length(); i++){
                                            JSONObject jsonObject = response.getJSONObject(i);
                                            getStatus = jsonObject.getString("status");
                                        }
                                        if (getStatus.equals("success")){
                                            StringRequest stringRequest = new StringRequest(Request.Method.PUT, HttpPostKandeman, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getApplicationContext(), "Update...", Toast.LENGTH_LONG).show();
                                                    progresInput.setVisibility(View.GONE);
                                                    etGol1.setText(null);
                                                    etGol2.setText(null);
                                                    etGol3.setText(null);
                                                    etGol4.setText(null);
                                                    etGol5.setText(null);
                                                    etGol1.requestFocus();
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getApplicationContext(), "Please try again...", Toast.LENGTH_LONG).show();
                                                    progresInput.setVisibility(View.GONE);

                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams(){
                                                    Map<String, String> params = new HashMap<String, String>();

                                                    // Adding All values to Params.
                                                    params.put("tanggal", tglFormat);
                                                    params.put("gerbang", getSpinGerbangAsal);
                                                    params.put("jenis_etol", getSpinJenisEtol);
                                                    params.put("gol_1", getG1);
                                                    params.put("gol_2", getG2);
                                                    params.put("gol_3", getG3);
                                                    params.put("gol_4", getG4);
                                                    params.put("gol_5", getG5);

                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueuee = Volley.newRequestQueue(getApplicationContext());
                                            requestQueuee.add(stringRequest);
                                        }else if (getStatus.equals("fail")){
                                            AndroidNetworking.post(HttpPostKandeman)
                                                    .addBodyParameter("bulan", namaMonth)
                                                    .addBodyParameter("tanggal", tglFormat)
                                                    .addBodyParameter("gerbang", getSpinGerbangAsal)
                                                    .addBodyParameter("jenis_etol", getSpinJenisEtol)
                                                    .addBodyParameter("gol_1", getG1)
                                                    .addBodyParameter("gol_2", getG2)
                                                    .addBodyParameter("gol_3", getG3)
                                                    .addBodyParameter("gol_4", getG4)
                                                    .addBodyParameter("gol_5", getG5)
                                                    .setPriority(Priority.MEDIUM)
                                                    .build()
                                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONArray response) {
                                                            Toast.makeText(getApplicationContext(), "Inserted...", Toast.LENGTH_LONG).show();
                                                            progresInput.setVisibility(View.GONE);
                                                            etGol1.setText(null);
                                                            etGol2.setText(null);
                                                            etGol3.setText(null);
                                                            etGol4.setText(null);
                                                            etGol5.setText(null);
                                                            etGol1.requestFocus();
                                                        }

                                                        @Override
                                                        public void onError(ANError anError) {
                                                            Toast.makeText(getApplicationContext(), "Please try again...", Toast.LENGTH_LONG).show();
                                                            progresInput.setVisibility(View.GONE);
                                                        }
                                                    });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });
                }
//                WELERI
                else if (getIdGerbang.equals("3")){
                    AndroidNetworking.get(HttpCekInputWeleri+getSpinGerbangAsal+"/"+getSpinJenisEtol+"/"+tglFormat)
                            .setPriority(Priority.LOW)
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        for (int i=0; i<response.length(); i++){
                                            JSONObject jsonObject = response.getJSONObject(i);
                                            getStatus = jsonObject.getString("status");
                                        }
                                        if (getStatus.equals("success")){
                                            StringRequest stringRequest = new StringRequest(Request.Method.PUT, HttpPostWeleri, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getApplicationContext(), "Inserted...", Toast.LENGTH_LONG).show();
                                                    progresInput.setVisibility(View.GONE);
                                                    etGol1.setText(null);
                                                    etGol2.setText(null);
                                                    etGol3.setText(null);
                                                    etGol4.setText(null);
                                                    etGol5.setText(null);
                                                    etGol1.requestFocus();
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getApplicationContext(), "Please try again...", Toast.LENGTH_LONG).show();
                                                    progresInput.setVisibility(View.GONE);
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams(){
                                                    // Creating Map String Params.
                                                    Map<String, String> params = new HashMap<String, String>();

                                                    // Adding All values to Params.
                                                    params.put("tanggal", tglFormat);
                                                    params.put("gerbang", getSpinGerbangAsal);
                                                    params.put("jenis_etol", getSpinJenisEtol);
                                                    params.put("gol_1", getG1);
                                                    params.put("gol_2", getG2);
                                                    params.put("gol_3", getG3);
                                                    params.put("gol_4", getG4);
                                                    params.put("gol_5", getG5);

                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueuee = Volley.newRequestQueue(getApplicationContext());
                                            requestQueuee.add(stringRequest);
                                        }else if (getStatus.equals("fail")){
                                            AndroidNetworking.post(HttpPostWeleri)
                                                    .addBodyParameter("bulan", namaMonth)
                                                    .addBodyParameter("tanggal", tglFormat)
                                                    .addBodyParameter("gerbang", getSpinGerbangAsal)
                                                    .addBodyParameter("jenis_etol", getSpinJenisEtol)
                                                    .addBodyParameter("gol_1", getG1)
                                                    .addBodyParameter("gol_2", getG2)
                                                    .addBodyParameter("gol_3", getG3)
                                                    .addBodyParameter("gol_4", getG4)
                                                    .addBodyParameter("gol_5", getG5)
                                                    .setPriority(Priority.MEDIUM)
                                                    .build()
                                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONArray response) {
                                                            Toast.makeText(getApplicationContext(), "Inserted...", Toast.LENGTH_LONG).show();
                                                            progresInput.setVisibility(View.GONE);
                                                            etGol1.setText(null);
                                                            etGol2.setText(null);
                                                            etGol3.setText(null);
                                                            etGol4.setText(null);
                                                            etGol5.setText(null);
                                                            etGol1.requestFocus();
                                                        }

                                                        @Override
                                                        public void onError(ANError anError) {
                                                            Toast.makeText(getApplicationContext(), "Please try again...", Toast.LENGTH_LONG).show();
                                                            progresInput.setVisibility(View.GONE);
                                                        }
                                                    });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });
                }
//                KENDAL
                else if (getIdGerbang.equals("4")){
                    AndroidNetworking.get(HttpCekInputKendal+getSpinGerbangAsal+"/"+getSpinJenisEtol+"/"+tglFormat)
                            .setPriority(Priority.LOW)
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        for (int i=0; i<response.length(); i++){
                                            JSONObject jsonObject = response.getJSONObject(i);
                                            getStatus = jsonObject.getString("status");
                                        }
                                        if (getStatus.equals("success")){
                                            StringRequest stringRequest = new StringRequest(Request.Method.PUT, HttpPostKendal, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getApplicationContext(), "Inserted...", Toast.LENGTH_LONG).show();
                                                    progresInput.setVisibility(View.GONE);
                                                    etGol1.setText(null);
                                                    etGol2.setText(null);
                                                    etGol3.setText(null);
                                                    etGol4.setText(null);
                                                    etGol5.setText(null);
                                                    etGol1.requestFocus();
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getApplicationContext(), "Please try again...", Toast.LENGTH_LONG).show();
                                                    progresInput.setVisibility(View.GONE);
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams(){
                                                    Map<String, String> params = new HashMap<String, String>();

                                                    // Adding All values to Params.
                                                    params.put("tanggal", tglFormat);
                                                    params.put("gerbang", getSpinGerbangAsal);
                                                    params.put("jenis_etol", getSpinJenisEtol);
                                                    params.put("gol_1", getG1);
                                                    params.put("gol_2", getG2);
                                                    params.put("gol_3", getG3);
                                                    params.put("gol_4", getG4);
                                                    params.put("gol_5", getG5);

                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueuee = Volley.newRequestQueue(getApplicationContext());
                                            requestQueuee.add(stringRequest);
                                        }else if (getStatus.equals("fail")){
                                            AndroidNetworking.post(HttpPostKendal)
                                                    .addBodyParameter("bulan", namaMonth)
                                                    .addBodyParameter("tanggal", tglFormat)
                                                    .addBodyParameter("gerbang", getSpinGerbangAsal)
                                                    .addBodyParameter("jenis_etol", getSpinJenisEtol)
                                                    .addBodyParameter("gol_1", getG1)
                                                    .addBodyParameter("gol_2", getG2)
                                                    .addBodyParameter("gol_3", getG3)
                                                    .addBodyParameter("gol_4", getG4)
                                                    .addBodyParameter("gol_5", getG5)
                                                    .setPriority(Priority.MEDIUM)
                                                    .build()
                                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONArray response) {
                                                            Toast.makeText(getApplicationContext(), "Inserted...", Toast.LENGTH_LONG).show();
                                                            progresInput.setVisibility(View.GONE);
                                                            etGol1.setText(null);
                                                            etGol2.setText(null);
                                                            etGol3.setText(null);
                                                            etGol4.setText(null);
                                                            etGol5.setText(null);
                                                            etGol1.requestFocus();
                                                        }

                                                        @Override
                                                        public void onError(ANError anError) {
                                                            Toast.makeText(getApplicationContext(), "Please try again...", Toast.LENGTH_LONG).show();
                                                            progresInput.setVisibility(View.GONE);
                                                        }
                                                    });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });
                }
//                KALIWUNGU
                else if (getIdGerbang.equals("5")){
                    AndroidNetworking.get(HttpCekInputKaliwungu+getSpinGerbangAsal+"/"+getSpinJenisEtol+"/"+tglFormat)
                            .setPriority(Priority.LOW)
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        for (int i=0; i<response.length(); i++){
                                            JSONObject jsonObject = response.getJSONObject(i);
                                            getStatus = jsonObject.getString("status");
                                        }
                                        if (getStatus.equals("success")){
                                            StringRequest stringRequestt = new StringRequest(Request.Method.PUT, HttpPostKaliwungu, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getApplicationContext(), "Inserted...", Toast.LENGTH_LONG).show();
                                                    progresInput.setVisibility(View.GONE);
                                                    etGol1.setText(null);
                                                    etGol2.setText(null);
                                                    etGol3.setText(null);
                                                    etGol4.setText(null);
                                                    etGol5.setText(null);
                                                    etGol1.requestFocus();
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {

                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams(){
                                                    Map<String, String> params = new HashMap<String, String>();

                                                    // Adding All values to Params.
                                                    params.put("tanggal", tglFormat);
                                                    params.put("gerbang", getSpinGerbangAsal);
                                                    params.put("jenis_etol", getSpinJenisEtol);
                                                    params.put("gol_1", getG1);
                                                    params.put("gol_2", getG2);
                                                    params.put("gol_3", getG3);
                                                    params.put("gol_4", getG4);
                                                    params.put("gol_5", getG5);

                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueuee = Volley.newRequestQueue(getApplicationContext());
                                            requestQueuee.add(stringRequestt);
                                        }else if (getStatus.equals("fail")){
                                            AndroidNetworking.post(HttpPostKaliwungu)
                                                    .addBodyParameter("bulan", namaMonth)
                                                    .addBodyParameter("tanggal", tglFormat)
                                                    .addBodyParameter("gerbang", getSpinGerbangAsal)
                                                    .addBodyParameter("jenis_etol", getSpinJenisEtol)
                                                    .addBodyParameter("gol_1", getG1)
                                                    .addBodyParameter("gol_2", getG2)
                                                    .addBodyParameter("gol_3", getG3)
                                                    .addBodyParameter("gol_4", getG4)
                                                    .addBodyParameter("gol_5", getG5)
                                                    .setPriority(Priority.MEDIUM)
                                                    .build()
                                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONArray response) {
                                                            Toast.makeText(getApplicationContext(), "Inserted...", Toast.LENGTH_LONG).show();
                                                            progresInput.setVisibility(View.GONE);
                                                            etGol1.setText(null);
                                                            etGol2.setText(null);
                                                            etGol3.setText(null);
                                                            etGol4.setText(null);
                                                            etGol5.setText(null);
                                                            etGol1.requestFocus();
                                                        }

                                                        @Override
                                                        public void onError(ANError anError) {
                                                            Toast.makeText(getApplicationContext(), "Please try again...", Toast.LENGTH_LONG).show();
                                                            progresInput.setVisibility(View.GONE);
                                                        }
                                                    });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });
                }
//                KALIKANGKUNG
                else if (getIdGerbang.equals("6")){
                    AndroidNetworking.get(HttpCekInputKalikangkung+getSpinGerbangAsal+"/"+getSpinJenisEtol+"/"+tglFormat)
                            .setPriority(Priority.LOW)
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        for (int i=0; i<response.length(); i++){
                                            JSONObject jsonObject = response.getJSONObject(i);
                                            getStatus = jsonObject.getString("status");
                                        }
                                        if (getStatus.equals("success")){
                                            StringRequest stringRequestt = new StringRequest(Request.Method.PUT, HttpPostKalikangkung, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getApplicationContext(), "Inserted...", Toast.LENGTH_LONG).show();
                                                    progresInput.setVisibility(View.GONE);
                                                    etGol1.setText(null);
                                                    etGol2.setText(null);
                                                    etGol3.setText(null);
                                                    etGol4.setText(null);
                                                    etGol5.setText(null);
                                                    etGol1.requestFocus();
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getApplicationContext(), "Please try again...", Toast.LENGTH_LONG).show();
                                                    progresInput.setVisibility(View.GONE);
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams(){
                                                    // Creating Map String Params.
                                                    Map<String, String> params = new HashMap<String, String>();

                                                    // Adding All values to Params.
                                                    params.put("tanggal", tglFormat);
                                                    params.put("gerbang", getSpinGerbangAsal);
                                                    params.put("jenis_etol", getSpinJenisEtol);
                                                    params.put("gol_1", getG1);
                                                    params.put("gol_2", getG2);
                                                    params.put("gol_3", getG3);
                                                    params.put("gol_4", getG4);
                                                    params.put("gol_5", getG5);

                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueuee = Volley.newRequestQueue(getApplicationContext());
                                            requestQueuee.add(stringRequestt);
                                        }else  if (getStatus.equals("fail")){
                                            AndroidNetworking.post(HttpPostKalikangkung)
                                                    .addBodyParameter("bulan", namaMonth)
                                                    .addBodyParameter("tanggal", tglFormat)
                                                    .addBodyParameter("gerbang", getSpinGerbangAsal)
                                                    .addBodyParameter("jenis_etol", getSpinJenisEtol)
                                                    .addBodyParameter("gol_1", getG1)
                                                    .addBodyParameter("gol_2", getG2)
                                                    .addBodyParameter("gol_3", getG3)
                                                    .addBodyParameter("gol_4", getG4)
                                                    .addBodyParameter("gol_5", getG5)
                                                    .setPriority(Priority.MEDIUM)
                                                    .build()
                                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONArray response) {
                                                            Toast.makeText(getApplicationContext(), "Inserted...", Toast.LENGTH_LONG).show();
                                                            progresInput.setVisibility(View.GONE);
                                                            etGol1.setText(null);
                                                            etGol2.setText(null);
                                                            etGol3.setText(null);
                                                            etGol4.setText(null);
                                                            etGol5.setText(null);
                                                            etGol1.requestFocus();
                                                        }

                                                        @Override
                                                        public void onError(ANError anError) {
                                                            Toast.makeText(getApplicationContext(), "Please try again...", Toast.LENGTH_LONG).show();
                                                            progresInput.setVisibility(View.GONE);
                                                        }
                                                    });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });
                }

            }
        });

    }
}

