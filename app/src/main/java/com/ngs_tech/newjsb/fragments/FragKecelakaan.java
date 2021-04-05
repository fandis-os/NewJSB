package com.ngs_tech.newjsb.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.act.TakePicture;
import com.ngs_tech.newjsb.utils.GenerateImageID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FragKecelakaan extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private Spinner spinJalur, spinJenisKclk, spinPenyebabKclk, spinJenisGol, spinDerek;

    private int mYear, mMonth, mDay;
    private TextView tvBulan, tvTanggal, tvHari, tvLokasi, tvSubmit;
    private EditText etJam, etSTA, etCuaca, etNopol, etKronologi, etPosisiakhir, etKorban, etLukaBerat, etKorbanMD, etKerugian, etPatroli, etPolisi;
    private String getBulan, getTanggal, getHari, getJam, getSTA, getJajur, getLatt, getLong, getCuaca, getJenisKendaraan, getNopol, getJnsKclk, getPenyebab, getKronologi, getPosisiakhir, getKorban, getLukaBerat, getKorbanMD, getKerugian, getPatroli, getDerek, getIdGambar, getLat, getLng, getPolisi;
    private Calendar cal;

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap gMap;
    private SupportMapFragment smf;
    private TextView tvLatt, tvLong;

    private static final String HttpPostKecelakaan = "http://114.7.131.86/toll_api/index.php/api/PostKecelakaan";
    private ProgressBar progresInput;

    private static final String HttpGetStatusKorban = "http://114.7.131.86/toll_api/index.php/api/GetStatusKorban/";
    private static final String HttpEditPie         = "http://114.7.131.86/toll_api/index.php/api/EditStatusKorban/";
    private static final String HttpPostPieAwal     = "http://114.7.131.86/toll_api/index.php/api/PostStatusKorbanAwal/";

    String currentTahun;
    Date currentTime;
    private String getJumlahBerdasarJnsKclk;
    private static final String HttpGetJenisKclk = "http://114.7.131.86/toll_api/index.php/api/GetJnsKecelakaan/";
    private static final String HttpEditPieJnsKclk = "http://114.7.131.86/toll_api/index.php/api/EditJumlahJnsKclk/";
    private static final String HttpPostPieJenisKclkAwal = "http://114.7.131.86/toll_api/index.php/api/PostStatusJenisKclkAwal/";

    private static final String HttpGetPenyebabKclk = "http://114.7.131.86/toll_api/index.php/api/GetPenyebabKclk/";
    private static final String HttpEditPiePenyebabKclk = "http://114.7.131.86/toll_api/index.php/api/EditJumlahPenyebabKclk/";
    private static final String HttpPostPiePenyebabKclkAwal = "http://114.7.131.86/toll_api/index.php/api/PostStatusPenyebabAwal/";

    private static final String HttpGetPiestatuskclk = "http://114.7.131.86/toll_api/index.php/api/GetJumlahKorban/";
    private static final String HttpEditPiestatuskclk = "http://114.7.131.86/toll_api/index.php/api/EditJumlahKorbanKclk/";
    private static final String HttpPostPiestatuskclk = "http://114.7.131.86/toll_api/index.php/api/PostStatusKorbanKsclkAwal/";

    private String getJumlahStatusKorban;
    private String idPenyebab;

    private LinearLayout linkr1, linkr2, linkr3, linkr4, linkr5;
    private Spinner spinJnKc1, spinJnKc2, spinJnKc3, spinJnKc4, spinJnKc5;
    private EditText etNopol1,etNopol2,etNopol3,etNopol4,etNopol5;

    private String getJnsGol1,getJnsGol2,getJnsGol3,getJnsGol4,getJnsGol5;
    private String getNopol1,getNopol2,getNopol3,getNopol4,getNopol5;

    String getNopolGroup, getGolKrGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_kecelakaan, container, false);

        AndroidNetworking.initialize(getContext());

        tvBulan     = view.findViewById(R.id.tv_bulan);
        tvTanggal   = view.findViewById(R.id.tv_tanggal);
        tvHari      = view.findViewById(R.id.tv_hari);
        tvLokasi    = view.findViewById(R.id.lokasi_id);
        tvSubmit    = view.findViewById(R.id.tv_submit);

        tvLatt = view.findViewById(R.id.tv_latt);
        tvLong = view.findViewById(R.id.tv_long);

//        PENGAMBILAN EDIT TEXT
        etJam           = view.findViewById(R.id.et_jam);
        etSTA        = view.findViewById(R.id.et_sta);
        etCuaca         = view.findViewById(R.id.et_cuaca);
        etNopol         = view.findViewById(R.id.et_nopol);
        etKronologi     = view.findViewById(R.id.et_kronologi);
        etPosisiakhir   = view.findViewById(R.id.et_posisi_akhir);
        etKorban        = view.findViewById(R.id.et_korban);
        etLukaBerat     = view.findViewById(R.id.et_luka_berat);
        etKorbanMD      = view.findViewById(R.id.et_korban_md);
        etKerugian      = view.findViewById(R.id.et_kerugian);
        etPatroli       = view.findViewById(R.id.et_patroli);
        etPolisi        = view.findViewById(R.id.et_kepolissian);

        progresInput    = view.findViewById(R.id.progres_insert);

        tvTanggal.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);

        mPlaceDetectionClient   = Places.getPlaceDetectionClient(getActivity());
        mGeoDataClient          = Places.getGeoDataClient(getActivity(), null);

        mFusedLocationProviderClient    = LocationServices.getFusedLocationProviderClient(getActivity());
        smf                             = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        smf.getMapAsync(this);

        getIdGambar = GenerateImageID.randomString(10);

        cal = Calendar.getInstance();
        mYear   = cal.get(Calendar.YEAR);
        mMonth  = cal.get(Calendar.MONTH);
        mDay    = cal.get(Calendar.DAY_OF_MONTH);

        currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdfth = new SimpleDateFormat("yyyy", Locale.getDefault());

        currentTahun = sdfth.format(new Date());

        linkr1  = view.findViewById(R.id.lin_kr1);
        linkr2  = view.findViewById(R.id.lin_kr2);
        linkr3  = view.findViewById(R.id.lin_kr3);
        linkr4  = view.findViewById(R.id.lin_kr4);
        linkr5  = view.findViewById(R.id.lin_kr5);

        spinJnKc1   = view.findViewById(R.id.spinner_jenis_kecgol1);
        spinJnKc2   = view.findViewById(R.id.spinner_jenis_kecgol2);
        spinJnKc3   = view.findViewById(R.id.spinner_jenis_kecgol3);
        spinJnKc4   = view.findViewById(R.id.spinner_jenis_kecgol4);
        spinJnKc5   = view.findViewById(R.id.spinner_jenis_kecgol5);

        etNopol1    = view.findViewById(R.id.et_nopol1);
        etNopol2    = view.findViewById(R.id.et_nopol2);
        etNopol3    = view.findViewById(R.id.et_nopol3);
        etNopol4    = view.findViewById(R.id.et_nopol4);
        etNopol5    = view.findViewById(R.id.et_nopol5);

        spinJalur = view.findViewById(R.id.spinner_jalur);
        ArrayAdapter<CharSequence> adapterJalur = ArrayAdapter.createFromResource(getActivity(), R.array.spin_jalur, R.layout.spinner_text);
        adapterJalur.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinJalur.setAdapter(adapterJalur);

        spinJenisKclk = view.findViewById(R.id.spinner_jenis_kecelakaan);
        ArrayAdapter<CharSequence> adapterJnsKclk = ArrayAdapter.createFromResource(getActivity(), R.array.spin_jenis_kecelakaan, R.layout.spinner_text);
        adapterJnsKclk.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinJenisKclk.setAdapter(adapterJnsKclk);

        spinJenisKclk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getKclk = spinJenisKclk.getSelectedItem().toString();
                if (getKclk.equals("Tunggal")){
                    linkr1.setVisibility(View.VISIBLE);
                    linkr2.setVisibility(View.GONE);
                    linkr3.setVisibility(View.GONE);
                    linkr4.setVisibility(View.GONE);
                    linkr5.setVisibility(View.GONE);
                }else if(getKclk.equals("Ganda")){
                    linkr1.setVisibility(View.VISIBLE);
                    linkr2.setVisibility(View.VISIBLE);
                    linkr3.setVisibility(View.GONE);
                    linkr4.setVisibility(View.GONE);
                    linkr5.setVisibility(View.GONE);
                }else if(getKclk.equals("Beruntun")){
                    linkr1.setVisibility(View.VISIBLE);
                    linkr2.setVisibility(View.VISIBLE);
                    linkr3.setVisibility(View.VISIBLE);
                    linkr4.setVisibility(View.VISIBLE);
                    linkr5.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapterGol1 = ArrayAdapter.createFromResource(getActivity(), R.array.spin_gol_kendaraan, R.layout.spinner_text);
        adapterGol1.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinJnKc1.setAdapter(adapterGol1);

        ArrayAdapter<CharSequence> adapterGol2 = ArrayAdapter.createFromResource(getActivity(), R.array.spin_gol_kendaraan, R.layout.spinner_text);
        adapterGol2.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinJnKc2.setAdapter(adapterGol2);

        ArrayAdapter<CharSequence> adapterGol3 = ArrayAdapter.createFromResource(getActivity(), R.array.spin_gol_kendaraan, R.layout.spinner_text);
        adapterGol3.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinJnKc3.setAdapter(adapterGol3);

        ArrayAdapter<CharSequence> adapterGol4 = ArrayAdapter.createFromResource(getActivity(), R.array.spin_gol_kendaraan, R.layout.spinner_text);
        adapterGol4.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinJnKc4.setAdapter(adapterGol4);

        ArrayAdapter<CharSequence> adapterGol5 = ArrayAdapter.createFromResource(getActivity(), R.array.spin_gol_kendaraan, R.layout.spinner_text);
        adapterGol5.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinJnKc5.setAdapter(adapterGol5);

        spinPenyebabKclk = view.findViewById(R.id.spinner_penyebab_kecelakaan);
        ArrayAdapter<CharSequence> adapterPnybKclk = ArrayAdapter.createFromResource(getActivity(), R.array.spin_penyebab_kecelakaan, R.layout.spinner_text);
        adapterPnybKclk.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinPenyebabKclk.setAdapter(adapterPnybKclk);

        spinJenisGol = view.findViewById(R.id.spinner_jenis_golkendaraan);
        ArrayAdapter<CharSequence> adapterGolKendaraan = ArrayAdapter.createFromResource(getActivity(), R.array.spin_gol_kendaraan, R.layout.spinner_text);
        adapterGolKendaraan.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinJenisGol.setAdapter(adapterGolKendaraan);

        spinDerek = view.findViewById(R.id.spinner_derek);
        ArrayAdapter<CharSequence> adapterDerek = ArrayAdapter.createFromResource(getActivity(), R.array.spin_derek, R.layout.spinner_text);
        adapterDerek.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinDerek.setAdapter(adapterDerek);

        return view;
    }

//    STATUS KORBAN
    private void getStatusKorbanLukaRingan() {
        AndroidNetworking.get(HttpGetStatusKorban+tvBulan.getText().toString()+"/"+1)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                getJumlahStatusKorban = jsonObject.getString("jumlah");

                                if (response.length()>0){
                                    int awal = Integer.parseInt(getJumlahStatusKorban);
                                    int jumlah = awal+1;
                                    AndroidNetworking.post(HttpEditPie)
                                            .addBodyParameter("bulan", tvBulan.getText().toString())
                                            .addBodyParameter("id_jenis", "1")
                                            .addBodyParameter("jumlah", String.valueOf(jumlah))
                                            .setPriority(Priority.MEDIUM)
                                            .build()
                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                @Override
                                                public void onResponse(JSONArray response) {

                                                }

                                                @Override
                                                public void onError(ANError anError) {

                                                }
                                            });
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AndroidNetworking.post(HttpPostPieAwal)
                                .addBodyParameter("bulan", tvBulan.getText().toString())
                                .addBodyParameter("tanggal", tvTanggal.getText().toString())
                                .addBodyParameter("jenis_korban", "Luka Ringan")
                                .addBodyParameter("jumlah", "1")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                });
    }

    private void getStatusKorbanLukaBerat() {
        AndroidNetworking.get(HttpGetStatusKorban+tvBulan.getText().toString()+"/"+2)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                getJumlahStatusKorban = jsonObject.getString("jumlah");

                                int awal = Integer.parseInt(getJumlahStatusKorban);
                                int jumlah = awal+1;

                                AndroidNetworking.post(HttpEditPie)
                                        .addBodyParameter("bulan", tvBulan.getText().toString())
                                        .addBodyParameter("id_jenis", "2")
                                        .addBodyParameter("jumlah", String.valueOf(jumlah))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AndroidNetworking.post(HttpPostPieAwal)
                                .addBodyParameter("bulan", tvBulan.getText().toString())
                                .addBodyParameter("tanggal", tvTanggal.getText().toString())
                                .addBodyParameter("jenis_korban", "Luka Berat")
                                .addBodyParameter("jumlah", "1")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                });
    }

    private void getStatusKorbanMD() {
        AndroidNetworking.get(HttpGetStatusKorban+tvBulan.getText().toString()+"/"+4)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                getJumlahStatusKorban = jsonObject.getString("jumlah");

                                int awal = Integer.parseInt(getJumlahStatusKorban);
                                int jumlah = awal+1;

                                AndroidNetworking.post(HttpEditPie)
                                        .addBodyParameter("bulan", tvBulan.getText().toString())
                                        .addBodyParameter("id_jenis", "4")
                                        .addBodyParameter("jumlah", String.valueOf(jumlah))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AndroidNetworking.post(HttpPostPieAwal)
                                .addBodyParameter("bulan", tvBulan.getText().toString())
                                .addBodyParameter("tanggal", tvTanggal.getText().toString())
                                .addBodyParameter("jenis_korban", "Meninggal")
                                .addBodyParameter("jumlah", "1")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                });
    }

    private void getStatusTidakAdaKorban() {
        AndroidNetworking.get(HttpGetStatusKorban+3)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                getJumlahStatusKorban = jsonObject.getString("jumlah");

                                int awal = Integer.parseInt(getJumlahStatusKorban);
                                int jumlah = awal+1;

                                AndroidNetworking.post(HttpEditPie)
                                        .addBodyParameter("bulan", tvBulan.getText().toString())
                                        .addBodyParameter("id_jenis", "3")
                                        .addBodyParameter("jumlah", String.valueOf(jumlah))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AndroidNetworking.post(HttpPostPieAwal)
                                .addBodyParameter("tanggal", tvTanggal.getText().toString())
                                .addBodyParameter("jenis_korban", "Tidak Ada Korban")
                                .addBodyParameter("jumlah", "1")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                });
    }
//    END OF STATUS KORBAN

//    JENIS
    private void getKclkTunggal() {
        AndroidNetworking.get(HttpGetJenisKclk+tvBulan.getText().toString()+"/"+currentTahun+"/2")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                getJumlahBerdasarJnsKclk = jsonObject.getString("jumlah");

                                int awal = Integer.parseInt(getJumlahBerdasarJnsKclk);
                                int jumlah = awal+1;

                                AndroidNetworking.post(HttpEditPieJnsKclk)
                                        .addBodyParameter("id_jenis", "2")
                                        .addBodyParameter("bulan", tvBulan.getText().toString())
                                        .addBodyParameter("tahun", currentTahun)
                                        .addBodyParameter("jumlah", String.valueOf(jumlah))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AndroidNetworking.post(HttpPostPieJenisKclkAwal)
                                .addBodyParameter("id_jenis", "2")
                                .addBodyParameter("bulan", tvBulan.getText().toString())
                                .addBodyParameter("tahun", currentTahun)
                                .addBodyParameter("tanggal", tvTanggal.getText().toString())
                                .addBodyParameter("jenis_korban", spinJenisKclk.getSelectedItem().toString())
                                .addBodyParameter("jumlah", "1")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                });
    }

    private void getKclkGanda() {
        AndroidNetworking.get(HttpGetJenisKclk+tvBulan.getText().toString()+"/"+currentTahun+"/3")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                getJumlahBerdasarJnsKclk = jsonObject.getString("jumlah");

                                int awal = Integer.parseInt(getJumlahBerdasarJnsKclk);
                                int jumlah = awal+1;

                                AndroidNetworking.post(HttpEditPieJnsKclk)
                                        .addBodyParameter("id_jenis", "3")
                                        .addBodyParameter("bulan", tvBulan.getText().toString())
                                        .addBodyParameter("tahun", currentTahun)
                                        .addBodyParameter("jumlah", String.valueOf(jumlah))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AndroidNetworking.post(HttpPostPieJenisKclkAwal)
                                .addBodyParameter("id_jenis", "3")
                                .addBodyParameter("bulan", tvBulan.getText().toString())
                                .addBodyParameter("tahun", currentTahun)
                                .addBodyParameter("tanggal", tvTanggal.getText().toString())
                                .addBodyParameter("jenis_korban", spinJenisKclk.getSelectedItem().toString())
                                .addBodyParameter("jumlah", "1")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                });
    }

    private void getKclkBeruntun() {
        AndroidNetworking.get(HttpGetJenisKclk+tvBulan.getText().toString()+"/"+currentTahun+"/1")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                getJumlahBerdasarJnsKclk = jsonObject.getString("jumlah");

                                int awal = Integer.parseInt(getJumlahBerdasarJnsKclk);
                                int jumlah = awal+1;

                                AndroidNetworking.post(HttpEditPieJnsKclk)
                                        .addBodyParameter("id_jenis", "1")
                                        .addBodyParameter("bulan", tvBulan.getText().toString())
                                        .addBodyParameter("tahun", currentTahun)
                                        .addBodyParameter("jumlah", String.valueOf(jumlah))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AndroidNetworking.post(HttpPostPieJenisKclkAwal)
                                .addBodyParameter("id_jenis", "1")
                                .addBodyParameter("bulan", tvBulan.getText().toString())
                                .addBodyParameter("tahun", currentTahun)
                                .addBodyParameter("tanggal", tvTanggal.getText().toString())
                                .addBodyParameter("jenis_korban", spinJenisKclk.getSelectedItem().toString())
                                .addBodyParameter("jumlah", "1")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                });
    }
//    END OF JENIS

// PENYEBAB
    private void getPieJumlahPenyebab(final String idPenyebab) {
        AndroidNetworking.get(HttpGetPenyebabKclk+tvBulan.getText().toString()+"/"+currentTahun+"/"+idPenyebab)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                getJumlahBerdasarJnsKclk = jsonObject.getString("jumlah");

                                int awal = Integer.parseInt(getJumlahBerdasarJnsKclk);
                                int jumlah = awal+1;

                                AndroidNetworking.post(HttpEditPiePenyebabKclk)
                                        .addBodyParameter("id_jenis", idPenyebab)
                                        .addBodyParameter("bulan", tvBulan.getText().toString())
                                        .addBodyParameter("tahun", currentTahun)
                                        .addBodyParameter("jumlah", String.valueOf(jumlah))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AndroidNetworking.post(HttpPostPiePenyebabKclkAwal)
                                .addBodyParameter("bulan", tvBulan.getText().toString())
                                .addBodyParameter("tahun", currentTahun)
                                .addBodyParameter("tanggal", tvTanggal.getText().toString())
                                .addBodyParameter("penyebab", spinPenyebabKclk.getSelectedItem().toString())
                                .addBodyParameter("id_penyebab", idPenyebab)
                                .addBodyParameter("jumlah", "1")
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                });
    }
// END OF PENYEBAB

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_tanggal:
                cal = Calendar.getInstance();
                mYear   = cal.get(Calendar.YEAR);
                mMonth  = cal.get(Calendar.MONTH);
                mDay    = cal.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                            Date date = new Date(year, month, dayOfMonth-1);
                            String dayOfWeek = simpledateformat.format(date);

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

                            tvTanggal.setText(getTanggal + " - " + namaMonth + " - " + year);
                            tvBulan.setText(namaMonth);
                            tvHari.setText(dayOfWeek);

                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
                break;
            case R.id.tv_submit:

                progresInput.setVisibility(View.VISIBLE);

                getBulan         = tvBulan.getText().toString();
                getTanggal       = tvTanggal.getText().toString();
                getHari          = tvHari.getText().toString();
                getJam           = etJam.getText().toString();
                getSTA           = etSTA.getText().toString();
                getJajur         = spinJalur.getSelectedItem().toString();
                getLatt          = tvLatt.getText().toString();
                getLong          = tvLong.getText().toString();
                getCuaca         = etCuaca.getText().toString();

                getJenisKendaraan= spinJenisGol.getSelectedItem().toString();
                getNopol         = etNopol.getText().toString();

                getJnsGol1       = spinJnKc1.getSelectedItem().toString();
                getJnsGol2       = spinJnKc2.getSelectedItem().toString();
                getJnsGol3       = spinJnKc3.getSelectedItem().toString();
                getJnsGol4       = spinJnKc4.getSelectedItem().toString();
                getJnsGol5       = spinJnKc5.getSelectedItem().toString();

                getNopol1        = etNopol1.getText().toString();
                getNopol2        = etNopol2.getText().toString();
                getNopol3        = etNopol3.getText().toString();
                getNopol4        = etNopol4.getText().toString();
                getNopol5        = etNopol5.getText().toString();

                getJnsKclk       = spinJenisKclk.getSelectedItem().toString();

                getPenyebab      = spinPenyebabKclk.getSelectedItem().toString();
                getKronologi     = etKronologi.getText().toString();
                getPosisiakhir   = etPosisiakhir.getText().toString();
                getKorban        = etKorban.getText().toString();
                getLukaBerat     = etLukaBerat.getText().toString();
                getKorbanMD      = etKorbanMD.getText().toString();
                getKerugian      = etKerugian.getText().toString();
                getPatroli       = etPatroli.getText().toString();
                getDerek         = spinDerek.getSelectedItem().toString();
                getPolisi        = etPolisi.getText().toString();

                if (getJnsKclk.equals("Tunggal")){
                    getNopolGroup = getNopol1;
                    getGolKrGroup = getJnsGol1;
                }

                if (getJnsKclk.equals("Ganda")){
                    getNopolGroup = getNopol1+", "+getNopol2;
                    getGolKrGroup = getJnsGol1+", "+getJnsGol2;
                }

                if (getJnsKclk.equals("Beruntun")){
                    getNopolGroup = getNopol1+", "+getNopol2+", "+getNopol3+", "+getNopol4+", "+getNopol5;
                    getGolKrGroup = getJnsGol1+", "+getJnsGol2+", "+getJnsGol3+", "+getJnsGol4+", "+getJnsGol5;
                }

//                UNTUK MEMBUAT PIE PENYEBAB KECELAKAAN

                if (getPenyebab.equals("Pecah Ban")){
                    idPenyebab = "1";
                    getPieJumlahPenyebab(idPenyebab);
                }
                if (getPenyebab.equals("Mengantuk")){
                    idPenyebab = "2";
                    getPieJumlahPenyebab(idPenyebab);
                }
                if (getPenyebab.equals("Kurang Antisipasi")){
                    idPenyebab = "3";
                    getPieJumlahPenyebab(idPenyebab);
                }
                if (getPenyebab.equals("Lain-lain")){
                    idPenyebab = "4";
                    getPieJumlahPenyebab(idPenyebab);
                }

//                END OF UNTUK MEMBUAT PIE PENYEBAB KECELAKAAN

//                UNTUK MEMBUAT PIE JENIS KECELAKAAN
                if (getJnsKclk.equals("Tunggal")){
                    getKclkTunggal();
                }
                if (getJnsKclk.equals("Ganda")){
                    getKclkGanda();
                }
                if (getJnsKclk.equals("Beruntun")){
                    getKclkBeruntun();
                }
//                END OF UNTUK MEMBUAT PIE JENIS KECELAKAAN


//      UNTUK MEMBUAT DIAGRAM PIE JENIS KORBAN
                if (!getKorban.equals("0")){
                    getStatusKorbanLukaRingan();
                }
                if (!getLukaBerat.equals("0")){
                    getStatusKorbanLukaBerat();
                }
                if (!getKorbanMD.equals("0")){
                    getStatusKorbanMD();
                }
                if (getKorban.equals("0") && getLukaBerat.equals("0") && getKorbanMD.equals("0")){
                    getStatusTidakAdaKorban();
                    Toast.makeText(getActivity(), "Berjalan", Toast.LENGTH_LONG).show();
                }
//      END OF UNTUK MEMBUAT DIAGRAM PIE JENIS KORBAN

//                UNTUK MEMBUAT PIE JUMLAH KORBAN
                if (!getKorban.equals("0")){
                    getStatusPieJumlahKorban(getKorban);
                }
                if (!getLukaBerat.equals("0")){
                    getStatusPieJumlahKorbanLukaBerat(getLukaBerat);
                }
                if (!getKorbanMD.equals("0")){
                    getStatusPieJumlahKorbanMD(getKorbanMD);
                }
//                END OF PIE JUMLAH KORBAN

                AndroidNetworking.post(HttpPostKecelakaan)
                        .addBodyParameter("tanggal", getTanggal)
                        .addBodyParameter("hari", getHari)
                        .addBodyParameter("bulan", getBulan)
                        .addBodyParameter("jam", getJam)
                        .addBodyParameter("sta", getSTA)
                        .addBodyParameter("jalur", getJajur)
                        .addBodyParameter("latt", getLatt)
                        .addBodyParameter("long", getLong)
                        .addBodyParameter("cuaca", getCuaca)
                        .addBodyParameter("jenis_kendaraan", getJenisKendaraan)
                        .addBodyParameter("nopol", getNopolGroup)
                        .addBodyParameter("jenis_kclk", getGolKrGroup)
                        .addBodyParameter("penyebab", getPenyebab)
                        .addBodyParameter("kronologi", getKronologi)
                        .addBodyParameter("posisi", getPosisiakhir)
                        .addBodyParameter("luka_ringan", getKorban)
                        .addBodyParameter("luka_berat", getLukaBerat)
                        .addBodyParameter("korban_md", getKorbanMD)
                        .addBodyParameter("kerugian", getKerugian)
                        .addBodyParameter("patroli", getPatroli)
                        .addBodyParameter("derek", getDerek)
                        .addBodyParameter("kepolisian", getPolisi)
                        .addBodyParameter("id_gambar", getIdGambar)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                progresInput.setVisibility(View.GONE);
                                Intent intent = new Intent(getContext(), TakePicture.class);
                                intent.putExtra("id_gambar", getIdGambar);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getContext(), "Please try again...", Toast.LENGTH_LONG).show();
                                progresInput.setVisibility(View.GONE);
                            }
                        });

                break;
            default:
                break;
        }
    }

    private void getStatusPieJumlahKorbanMD(final String getKorbanMD) {
        AndroidNetworking.get(HttpGetPiestatuskclk+"3")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String getPieJumlahJumlahKorban = jsonObject.getString("jumlah");

                                int korbanbaru = Integer.parseInt(getKorbanMD);
                                int awal = Integer.parseInt(getPieJumlahJumlahKorban);
                                int jumlah = awal+korbanbaru;
                                AndroidNetworking.post(HttpEditPiestatuskclk)
                                        .addBodyParameter("id_jenis", "1")
                                        .addBodyParameter("jumlah", String.valueOf(jumlah))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AndroidNetworking.post(HttpPostPiestatuskclk)
                                .addBodyParameter("jenis_korban", "Korban Meninggal Dunia")
                                .addBodyParameter("id_jenis", "1")
                                .addBodyParameter("jumlah", getKorbanMD)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                });
    }

    private void getStatusPieJumlahKorbanLukaBerat(final String getLukaBerat) {
        AndroidNetworking.get(HttpGetPiestatuskclk+"2")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String getPieJumlahJumlahKorban = jsonObject.getString("jumlah");

                                int korbanbaru = Integer.parseInt(getLukaBerat);
                                int awal = Integer.parseInt(getPieJumlahJumlahKorban);
                                int jumlah = awal+korbanbaru;
                                AndroidNetworking.post(HttpEditPiestatuskclk)
                                        .addBodyParameter("id_jenis", "1")
                                        .addBodyParameter("jumlah", String.valueOf(jumlah))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AndroidNetworking.post(HttpPostPiestatuskclk)
                                .addBodyParameter("jenis_korban", "Korban Luka Berat")
                                .addBodyParameter("id_jenis", "1")
                                .addBodyParameter("jumlah", getLukaBerat)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                });
    }

    private void getStatusPieJumlahKorban(final String getKorban) {
        AndroidNetworking.get(HttpGetPiestatuskclk+"1")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String getPieJumlahJumlahKorban = jsonObject.getString("jumlah");

                                int korbanbaru = Integer.parseInt(getKorban);
                                int awal = Integer.parseInt(getPieJumlahJumlahKorban);
                                int jumlah = awal+korbanbaru;
                                AndroidNetworking.post(HttpEditPiestatuskclk)
                                        .addBodyParameter("id_jenis", "1")
                                        .addBodyParameter("jumlah", String.valueOf(jumlah))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONArray(new JSONArrayRequestListener() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AndroidNetworking.post(HttpPostPiestatuskclk)
                                .addBodyParameter("jenis_korban", "Korban Luka Ringan")
                                .addBodyParameter("id_jenis", "1")
                                .addBodyParameter("jumlah", getKorban)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        getDeviceLocation();
    }

    private void getDeviceLocation() {
        try {
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Geocoder geocoder;
                    List<Address> addresses = null;
                    geocoder = new Geocoder(getActivity(), Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(),
                                    location.getLongitude()), 13));

                    double la = location.getLatitude();
                    double lo = location.getLongitude();


                    gMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
                    gMap.setMyLocationEnabled(true);

                    tvLokasi.setText(addresses.get(0).getAddressLine(0));
                    tvLatt.setText(String.valueOf(la));
                    tvLong.setText(String.valueOf(lo));
                }
            });
        }catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

}
