package com.ngs_tech.newjsb.act;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ngs_tech.newjsb.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class EditProfil extends AppCompatActivity {

    private String HttpUrl = "http://114.7.131.86/toll_api/index.php/api/User/";
    String HttpGetUserData = "http://114.7.131.86/toll_api/index.php/api/User";

    private Uri mainImageURI = null;
    private boolean isChanged = false;
    String uuidd;

    TextView save;
    CircleImageView setup_image;
    EditText nama,alamat,hp,gerbang;
    ProgressBar setup_progress;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String user_id=null;
    private Bitmap compressedImageFile;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                setup_image.setImageURI(mainImageURI);
                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        AndroidNetworking.initialize(getApplicationContext()); //inisialisasi library FAN

        save    = findViewById(R.id.save_setup_profile);
        nama    = findViewById(R.id.setup_name);
        hp      = findViewById(R.id.setup_hp);
        gerbang = findViewById(R.id.gerbang);

        firebaseAuth        =FirebaseAuth.getInstance();
        user_id             =firebaseAuth.getCurrentUser().getUid();
        setup_progress      =findViewById(R.id.progress_setup);
        storageReference    =FirebaseStorage.getInstance().getReference();
        firebaseFirestore   =FirebaseFirestore.getInstance();
        setup_image         =findViewById(R.id.setup_image);

        hp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (hp.getText().toString().trim().length()<12){
                        hp.setError("Nomor kurang");
                    }else {
                        hp.setError(null);
                    }
                }
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, HttpGetUserData + user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String idUser = jsonObject.getString("no_hp");
                        uuidd = jsonObject.getString("uuid");
                        String namaUser = jsonObject.getString("nama_user");
                        String imgUser = jsonObject.getString("img");

                        final RequestOptions placeHoldder = new RequestOptions();
                        placeHoldder.placeholder(R.drawable.profile);
                        Glide.with(EditProfil.this)
                                .setDefaultRequestOptions(placeHoldder)
                                .load(imgUser)
                                .into(setup_image);

                        nama.setText(namaUser);
                        hp.setText(idUser);
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
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfil.this);
        requestQueue.add(stringRequest);

        setup_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String namauser   = nama.getText().toString();
                final String getGerbang = gerbang.getText().toString();
                final String no_hp      = hp.getText().toString();

                if (isValidPhone(no_hp)){
                    Toast.makeText(EditProfil.this, "Pake kode negara untuk no hp", Toast.LENGTH_LONG).show();
                }

                if (isChanged){
                    if (!TextUtils.isEmpty(namauser) && !TextUtils.isEmpty(no_hp) && !TextUtils.isEmpty(getGerbang) && mainImageURI != null) {
                        setup_progress.setVisibility(View.VISIBLE);
                        final File newImageFile=new File(mainImageURI.getPath());

                        try {
                            compressedImageFile = new Compressor(EditProfil.this)
                                    .setMaxHeight(200)
                                    .setMaxWidth(200)
                                    .setQuality(10)
                                    .compressToBitmap(newImageFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos=new ByteArrayOutputStream();
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG,100,baos);
                        byte [] data=baos.toByteArray();

                        UploadTask uploadTask=storageReference.child("profile_images").child(user_id+".jpg").putBytes(data);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final String downloadLink = uri.toString();

                                        AndroidNetworking.post(HttpUrl)
                                                .addBodyParameter("img", downloadLink)
                                                .addBodyParameter("nama_user", namauser)
                                                .addBodyParameter("no_hp", no_hp)
                                                .addBodyParameter("uuid", user_id)
                                                .addBodyParameter("status_gerbang", getGerbang)
                                                .setPriority(Priority.MEDIUM)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.d("TAG", "onResponse: " + response); //untuk log pada onresponse
                                                        Toast.makeText(getApplicationContext(),"Data berhasil ditambahkan" , Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onError(ANError anError) {
                                                        Toast.makeText(getApplicationContext(),"Data gagal ditambahkan", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        storeFireStore(downloadLink,namauser,getGerbang);

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
            }
        });

    }

    private void storeFireStore(String downloadLink, String namauser, String getGerbang) {
        Map<String,String> userMap=new HashMap<>();
        userMap.put("name",namauser);
        userMap.put("image",downloadLink);
        userMap.put("uuid", user_id);
        userMap.put("id_gerbang", getGerbang);

        firebaseFirestore.collection("User").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(getApplicationContext(),"Save Profile ",Toast.LENGTH_SHORT).show();
                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),"FireStore Eror "+error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidPhone(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() < 6 || phone.length() > 13)
            {
                check = false;

            }
            else
            {
                check = true;

            }
        }
        else
        {
            check=false;
        }
        return check;
    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(EditProfil.this);
    }
}
