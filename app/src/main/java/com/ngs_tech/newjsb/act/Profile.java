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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ngs_tech.newjsb.Main;
import com.ngs_tech.newjsb.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Profile extends AppCompatActivity {

    String HttpGetUserData = "http://114.7.131.86/toll_api/index.php/api/User/";
    String HttpEditUserData = "http://114.7.131.86/toll_api/index.php/api/Edituserapp/";
    private String idUser, uuid, namaUser, imgUser, noHp, statusUser;

    private Uri mainImageURI = null;
    private boolean isChanged = false;

    ImageView back;
    TextView save, chagePassword, change, textChange;
    TextInputLayout textNewPassword;
    CircleImageView setup_image;
    EditText nama,hp;
    ProgressBar setup_progress;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String user_id=null;
    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        AndroidNetworking.initialize(getApplicationContext());

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        save= findViewById(R.id.save_setup_profile);
        nama= findViewById(R.id.setup_name);
        hp= findViewById(R.id.setup_hp);
        setup_progress = findViewById(R.id.setup_progress);
        chagePassword = findViewById(R.id.btn_change_password);
        change = findViewById(R.id.btn_change);
        textChange = findViewById(R.id.new_password);

        textNewPassword = findViewById(R.id.text_input_new_pass);

        firebaseAuth= FirebaseAuth.getInstance();
        user_id= firebaseAuth.getCurrentUser().getUid();

        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        setup_image=findViewById(R.id.setup_image);

        firebaseFirestore.collection("User").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        String namaku=task.getResult().getString("name");
                        String imageku=task.getResult().getString("image");

                        nama.setText(namaku);
                        mainImageURI=Uri.parse(imageku);

                        RequestOptions placeHolder=new RequestOptions();
                        placeHolder.placeholder(R.drawable.profile);

                        Glide.with(Profile.this)
                                .setDefaultRequestOptions(placeHolder)
                                .load(imageku)
                                .into(setup_image);
                    }else {
                        Toast.makeText(getApplicationContext(),"Lengkapi Profil Anda", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),"FireStore Retrieve Eror "+error,Toast.LENGTH_SHORT).show();
                }
            }
        });

        setup_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String randomName = UUID.randomUUID().toString();
                final String namauser=nama.getText().toString();
                final String no_hp=hp.getText().toString();
                final String uuid = user_id;

                if (isValidPhone(no_hp)){
                    Toast.makeText(Profile.this, "Pake kode negara untuk no hp", Toast.LENGTH_LONG).show();
                }
                if(isChanged) {
                    if (!TextUtils.isEmpty(namauser) && !TextUtils.isEmpty(no_hp)) {
                        setup_progress.setVisibility(View.VISIBLE);
                        File newImageFile=new File(mainImageURI.getPath());
                        try {
                            compressedImageFile=new Compressor(Profile.this)
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
                                        String downloadLink = uri.toString();
                                        Log.e("download url : ", downloadLink);
                                        storeFireStore(downloadLink,namauser, no_hp, uuid);
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

        chagePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textNewPassword.setVisibility(View.VISIBLE);
                chagePassword.setVisibility(View.GONE);
                change.setVisibility(View.VISIBLE);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup_progress.setVisibility(View.VISIBLE);
                String newPass = textChange.getText().toString();
                user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Profile.this, "Password is update ..!", Toast.LENGTH_SHORT);
                            sighOut();
                            setup_progress.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(Profile.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                            setup_progress.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });



    }

    private void sighOut() {
        firebaseAuth.signOut();
        Intent intent = new Intent(Profile.this, Main.class);
        startActivity(intent);
        finishAffinity();
    }

    private void storeFireStore(final String downloadLink, final String namauser, final String no_hp, final String uuid) {
        Map<String,String> userMap=new HashMap<>();
        userMap.put("name",namauser);
        userMap.put("image",downloadLink);


        firebaseFirestore.collection("User").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    AndroidNetworking.post(HttpEditUserData)
                            .addBodyParameter("img", downloadLink)
                            .addBodyParameter("nama_user", namauser)
                            .addBodyParameter("no_hp", no_hp)
                            .addBodyParameter("uuid", uuid)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(getApplicationContext(),"Save Berhasil ",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(getApplicationContext(),"Save Gagal ",Toast.LENGTH_SHORT).show();
                                }
                            });

                    finish();
                    Toast.makeText(getApplicationContext(),"Save Profile ",Toast.LENGTH_SHORT).show();
                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),"FireStore Eror "+error,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(Profile.this);

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
                        {
                            try {
                                for (int i=0; i<response.length(); i++){
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    idUser      = jsonObject.getString("id_user");
                                    uuid        = jsonObject.getString("uuid");
                                    namaUser    = jsonObject.getString("nama_user");
                                    imgUser     = jsonObject.getString("img");
                                    noHp        = jsonObject.getString("no_hp");

                                    hp.setText(noHp);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

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

    private boolean isValidPhone(String phone)
    {
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

}
