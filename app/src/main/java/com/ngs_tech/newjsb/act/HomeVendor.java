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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeVendor extends AppCompatActivity implements View.OnClickListener{

    String HttpGetUserData = "http://114.7.131.86/toll_api/index.php/api/User/";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id=null;

    private String namaUser, imgUser;
    private TextView tvNamaUser;
    private CircleImageView cirImgUser, cirImgLogout;
    private ImageView imgEditProfil;
    private LinearLayout linPemeliharaanSPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_vendor);

        AndroidNetworking.initialize(getApplicationContext());

        firebaseFirestore   =FirebaseFirestore.getInstance();
        firebaseAuth        =FirebaseAuth.getInstance();
        user_id             =firebaseAuth.getCurrentUser().getUid();

        imgEditProfil       = findViewById(R.id.img_edit_profil);
        tvNamaUser          = findViewById(R.id.nama_user);
        cirImgUser          = findViewById(R.id.cir_img_user);
        cirImgLogout        = findViewById(R.id.cir_img_logout);
        linPemeliharaanSPM  = findViewById(R.id.lin_pemeliharaan_spm);

        imgEditProfil.setOnClickListener(this);
        cirImgLogout.setOnClickListener(this);
        linPemeliharaanSPM.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        AndroidNetworking.get(HttpGetUserData+user_id)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                namaUser    = jsonObject.getString("nama_user");
                                imgUser     = jsonObject.getString("img");

                                final RequestOptions placeHolder = new RequestOptions();
                                placeHolder.placeholder(R.drawable.profile);
                                Glide.with(getApplicationContext())
                                        .setDefaultRequestOptions(placeHolder)
                                        .load(imgUser)
                                        .into(cirImgUser);

                                tvNamaUser.setText(namaUser);
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

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.img_edit_profil:
                intent = new Intent(HomeVendor.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.cir_img_logout:
                firebaseAuth.signOut();
                intent = new Intent(HomeVendor.this, Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
                break;
            case R.id.lin_pemeliharaan_spm:
                intent = new Intent(HomeVendor.this, ResponSPM.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            default:
                break;
        }
    }
}
