package com.ngs_tech.newjsb.act;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ngs_tech.newjsb.Main;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.sub_act.LaluLintas;
import com.ngs_tech.newjsb.sub_act.Pemeliharaan;
import com.ngs_tech.newjsb.sub_act.PengToll;
import com.ngs_tech.newjsb.sub_act.RKAP;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements View.OnClickListener{

//    String HttpGetUserData = "http://114.7.131.86/toll_api/index.php/api/User/";
    private LinearLayout linPengumpulTol, linLalulintas, linMonitoringRKP, linPemeliharaanSPM;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id=null;

    private String idUser, uuid, namaUser, imgUser, noHp, statusUser;
    private TextView tvNamaUser;
    private CircleImageView cirImgUser, cirImgLogout;
    private ImageView imgEditProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AndroidNetworking.initialize(getApplicationContext());

        firebaseFirestore   =FirebaseFirestore.getInstance();
        firebaseAuth        =FirebaseAuth.getInstance();
        user_id             =firebaseAuth.getCurrentUser().getUid();

        imgEditProfil       = findViewById(R.id.img_edit_profil);
        tvNamaUser          = findViewById(R.id.nama_user);
        cirImgUser          = findViewById(R.id.cir_img_user);
        linPengumpulTol     = findViewById(R.id.lin_pengumpulan_tol);
        linLalulintas       = findViewById(R.id.lin_lalulintas);
        linPemeliharaanSPM  = findViewById(R.id.lin_pemeliharaan_spm);
        linMonitoringRKP    = findViewById(R.id.lin_monitoring_rkp);
        cirImgLogout        = findViewById(R.id.cir_img_logout);

        linPengumpulTol.setOnClickListener(this);
        linMonitoringRKP.setOnClickListener(this);
        linLalulintas.setOnClickListener(this);
        linPemeliharaanSPM.setOnClickListener(this);
        cirImgLogout.setOnClickListener(this);
        imgEditProfil.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.lin_pengumpulan_tol:
                intent = new Intent(Home.this, PengToll.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.lin_lalulintas:
                intent = new Intent(Home.this, LaluLintas.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.lin_pemeliharaan_spm:
                intent = new Intent(Home.this, Pemeliharaan.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.lin_monitoring_rkp:
                intent = new Intent(Home.this, RKAP.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.cir_img_logout:
                firebaseAuth.signOut();
                intent = new Intent(Home.this, Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
                break;
            case R.id.img_edit_profil:
                intent = new Intent(Home.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//        AndroidNetworking.get(HttpGetUserData+user_id)
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONArray(new JSONArrayRequestListener() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        {
//                            try {
//                                for (int i=0; i<response.length(); i++){
//                                    JSONObject jsonObject = response.getJSONObject(i);
//                                    idUser      = jsonObject.getString("id_user");
//                                    uuid        = jsonObject.getString("uuid");
//                                    namaUser    = jsonObject.getString("nama_user");
//                                    imgUser     = jsonObject.getString("img");
//
//                                    final RequestOptions placeHolder = new RequestOptions();
//                                    placeHolder.placeholder(R.drawable.profile);
//                                    Glide.with(getApplicationContext())
//                                            .setDefaultRequestOptions(placeHolder)
//                                            .load(imgUser)
//                                            .into(cirImgUser);
//
//                                    tvNamaUser.setText(namaUser);
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//
//                    }
//                });


    }
}
