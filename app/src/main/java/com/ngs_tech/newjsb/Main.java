package com.ngs_tech.newjsb;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ngs_tech.newjsb.act.Home;
import com.ngs_tech.newjsb.act.Register;
import com.ngs_tech.newjsb.utils.Checkin;
import com.ngs_tech.newjsb.utils.Permissions;

public class Main extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private static final int VERIFY_PERMISSIONS_REQUEST = 1;
    RelativeLayout linearLayout;
    Button button;
    TextView register,forget;
    Animation uptodown, downtoup;
    EditText username,password;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    ImageView circleLogo;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        register=findViewById(R.id.txt_register);
        register.setVisibility(View.VISIBLE);

        button = findViewById(R.id.button);
        linearLayout = findViewById(R.id.linear);

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        linearLayout.setAnimation(uptodown);

        username=findViewById(R.id.login_username);
        password=findViewById(R.id.login_password);
        circleLogo=findViewById(R.id.circle_logo);
        circleLogo.bringToFront();
        circleLogo.setElevation(50);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Main.this, Register.class);
                startActivity(in);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginusername=username.getText().toString();
                String loginpassword=password.getText().toString();

                if (!TextUtils.isEmpty(loginpassword)&&!TextUtils.isEmpty(loginusername)){
                    progressDialog.setTitle("Masuk, Mohon Tunggu...");
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(loginusername, loginpassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
//                                        Log.d(TAG, "signInWithEmail:success");
                                        user=mAuth.getCurrentUser();
                                        if(user.isEmailVerified()){

                                            sendToHome();
                                            Toast.makeText(getApplicationContext(), "Login Sukses",
                                                    Toast.LENGTH_SHORT).show();
                                        }else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Email Belum diVerifikasi",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        progressDialog.dismiss();
                                        // If sign in fails, display a message to the user.
//                                        Log.e(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), "Login Gagal",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });

    }

    private void sendToHome() {
//        Intent intent = new Intent(Main.this, Checkin.class);
        Intent intent = new Intent(Main.this, Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(checkPermissionsArray(Permissions.PERMISSIONS)){
            return;

        }else{
            verifyPermissions(Permissions.PERMISSIONS);
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null&&currentUser.isEmailVerified()){
            sendToHome();
        }
        else{
//            Toast.makeText(Main.this, "Preferences gagal", Toast.LENGTH_LONG).show();
        }
    }

    private void verifyPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(
                Main.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    private boolean checkPermissionsArray(String[] permissions) {
        for(int i = 0; i< permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    private boolean checkPermissions(String permission) {
        int permissionRequest = ActivityCompat.checkSelfPermission(Main.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
//            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        }
        else{
//            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }
}
