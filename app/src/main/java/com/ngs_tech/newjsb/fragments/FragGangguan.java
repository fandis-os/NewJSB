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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.act.TakePicture;
import com.ngs_tech.newjsb.utils.GenerateImageID;

import org.json.JSONArray;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragGangguan extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private int mYear, mMonth, mDay;

    private Spinner spinerJalur, spinerJnisGangguan, spinJnsGolKendaraan, spinDerek;
    private TextView tvTanggal, tvHari, tvLokasi, tvSubmit;
    private EditText etPukul, etSta;

    private String getIDGambar, getIdKategoriGangguan;
    private Double getLng, getLat;

    private Calendar cal;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap gMap;
    private SupportMapFragment smf;

    private static final String HttpPostGangguan = "http://114.7.131.86/toll_api/index.php/api/PostGangguan";
    private ProgressBar progresInput;

    private FirebaseAuth firebaseAuth;
    private String user_id=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_gangguan, container, false);

        AndroidNetworking.initialize(getContext());
        firebaseAuth        =FirebaseAuth.getInstance();
        user_id             =firebaseAuth.getCurrentUser().getUid();

        tvTanggal       = view.findViewById(R.id.tv_tanggal);
        tvHari          = view.findViewById(R.id.tv_hari);
        tvLokasi        = view.findViewById(R.id.lokasi_id);
        tvSubmit        = view.findViewById(R.id.tv_submit);

        etPukul             = view.findViewById(R.id.et_jam);
        etSta               = view.findViewById(R.id.et_sta);

        progresInput    = view.findViewById(R.id.progres_insert);

        mFusedLocationProviderClient    = LocationServices.getFusedLocationProviderClient(getActivity());
        smf                             = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        smf.getMapAsync(this);

        getIDGambar         = GenerateImageID.randomString(10);

        tvTanggal.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);

        spinerJalur = view.findViewById(R.id.spinner_jalur);
        ArrayAdapter<CharSequence> adapterJalur = ArrayAdapter.createFromResource(getActivity(), R.array.spin_jalur, R.layout.spinner_text);
        adapterJalur.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinerJalur.setAdapter(adapterJalur);

        spinerJnisGangguan = view.findViewById(R.id.spinner_jenis_gangguan);
        ArrayAdapter<CharSequence> adapterJnsGangguan = ArrayAdapter.createFromResource(getActivity(), R.array.spin_jenis_gangguan, R.layout.spinner_text);
        adapterJnsGangguan.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinerJnisGangguan.setAdapter(adapterJnsGangguan);

        spinJnsGolKendaraan = view.findViewById(R.id.spinner_jenis_golkendaraan);
        ArrayAdapter<CharSequence> adapterGolKendaraan = ArrayAdapter.createFromResource(getActivity(), R.array.spin_gol_kendaraan, R.layout.spinner_text);
        adapterGolKendaraan.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinJnsGolKendaraan.setAdapter(adapterGolKendaraan);

        spinDerek = view.findViewById(R.id.spinner_derek);
        ArrayAdapter<CharSequence> adapterDerek = ArrayAdapter.createFromResource(getActivity(), R.array.spin_derek, R.layout.spinner_text);
        adapterDerek.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinDerek.setAdapter(adapterDerek);

        return view;
    }

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
                            tvHari.setText(dayOfWeek);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
                break;
            case R.id.tv_submit:
                progresInput.setVisibility(View.VISIBLE);

                if (spinerJnisGangguan.getSelectedItem().toString().equals("Pecah Ban")){
                    getIdKategoriGangguan = "1";
                }
                if (spinerJnisGangguan.getSelectedItem().toString().equals("Mesin")){
                    getIdKategoriGangguan = "2";
                }
                if (spinerJnisGangguan.getSelectedItem().toString().equals("Diluar Mesin")){
                    getIdKategoriGangguan = "3";
                }

                AndroidNetworking.post(HttpPostGangguan)
                        .addBodyParameter("uuid", user_id)
                        .addBodyParameter("tanggal", tvTanggal.getText().toString())
                        .addBodyParameter("hari", tvHari.getText().toString())
                        .addBodyParameter("jam", etPukul.getText().toString())
                        .addBodyParameter("sta", etSta.getText().toString())
                        .addBodyParameter("jalur", spinerJalur.getSelectedItem().toString())
                        .addBodyParameter("jenis_gangguan", spinerJnisGangguan.getSelectedItem().toString())
                        .addBodyParameter("id_kat", getIdKategoriGangguan)
                        .addBodyParameter("jenis_kendaraan", spinJnsGolKendaraan.getSelectedItem().toString())
                        .addBodyParameter("derek", spinDerek.getSelectedItem().toString())
                        .addBodyParameter("lat", String.valueOf(getLat))
                        .addBodyParameter("lng", String.valueOf(getLng))
                        .addBodyParameter("id_gambar", getIDGambar)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                progresInput.setVisibility(View.GONE);
                                Intent intent = new Intent(getContext(), TakePicture.class);
                                intent.putExtra("id_gambar", getIDGambar);
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

                    gMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
                    gMap.setMyLocationEnabled(true);

                    tvLokasi.setText(addresses.get(0).getAddressLine(0));
                    getLat = location.getLatitude();
                    getLng = location.getAltitude();
                }
            });
        }catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
