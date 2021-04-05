package com.ngs_tech.newjsb.utils;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ngs_tech.newjsb.Main;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.act.EditProfil;
import com.ngs_tech.newjsb.act.Home;
import com.ngs_tech.newjsb.act.HomeDashboard;
import com.ngs_tech.newjsb.act.HomeVendor;
import com.ngs_tech.newjsb.act.ResponSPM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Checkin extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id=null;
    private String statusUser;

    String HttpGetUserData = "http://114.7.131.86/toll_api/index.php/api/UserCek/";
    String HttpGetUserDataCek = "http://114.7.131.86/toll_api/index.php/api/User/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        AndroidNetworking.initialize(getApplicationContext());

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        user_id=firebaseAuth.getCurrentUser().getUid();
//

        AndroidNetworking.get(HttpGetUserData+user_id)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, HttpGetUserDataCek + user_id, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        statusUser = jsonObject.getString("status_user");
                                        if (statusUser.equals("1")) {
                                            Intent intent = new Intent(Checkin.this, Home.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            finish();
                                        } else if (statusUser.equals("2")) {
                                            Intent intent = new Intent(Checkin.this, HomeDashboard.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            finish();
                                        } else if (statusUser.equals("3")) {
                                            Intent intent = new Intent(Checkin.this, HomeVendor.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            finish();
                                        } else {
                                            Toast.makeText(Checkin.this, "Status User tidak di ketahui", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        RequestQueue requestQueue = Volley.newRequestQueue(Checkin.this);
                        requestQueue.add(stringRequest);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Intent edit=new Intent(getApplicationContext(), EditProfil.class);
                        startActivity(edit);
                    }
                });

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            firebaseFirestore.collection("User").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if (task.getResult().exists()){

                        }else {
                            Intent edit=new Intent(getApplicationContext(), EditProfil.class);
                            startActivity(edit);
                        }
                    }else {
                        String error=task.getException().getMessage();
                        Toast.makeText(getApplicationContext(),"FireStore Retrieve Eror "+error,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Intent login= new Intent(getApplicationContext(), Main.class);
            startActivity(login);
            Toast.makeText(getApplicationContext(),"Anda Belum Login",Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    @Override
    protected void onStart() {
        super.onStart();

    }


}
