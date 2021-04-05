package com.ngs_tech.newjsb.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ngs_tech.newjsb.Main;
import com.ngs_tech.newjsb.R;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private Button register;
    private ProgressDialog progressDialog;
    private EditText emailtxt,password,repeatpassword;

    ImageView circleLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        circleLogo = findViewById(R.id.circle_logo);
        circleLogo.bringToFront();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circleLogo.setElevation(50);
        }

        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        register=findViewById(R.id.regist_button);
        emailtxt=findViewById(R.id.regist_email);
        password=findViewById(R.id.regist_password);
        repeatpassword=findViewById(R.id.regist_repeat_password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString=emailtxt.getText().toString();
                String passwordString=password.getText().toString();
                String passwordRepeatString=repeatpassword.getText().toString();
                if (passwordString.equals(passwordRepeatString)) {
                    progressDialog.setTitle("Mendaftar, Mohon Tunggu...");
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(emailString,passwordRepeatString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
//                                Log.d(TAG, "onComplete: Pendaftaran Berhasil");
                                current_user=mAuth.getCurrentUser();
                                if(!current_user.isEmailVerified()){
                                    current_user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
//                                                Log.d(TAG, "onComplete: Pendaftaran Berhasil");
                                                Toast.makeText(getApplicationContext(),"Email Verifikasi telah dikirim",Toast.LENGTH_SHORT).show();

                                                sendToLogin();
                                                finish();

                                            }else {
                                                Toast.makeText(getApplicationContext(),"Email Verifikasi gagal dikirim",Toast.LENGTH_SHORT).show();
//                                                Log.e(TAG, "onComplete: Pendaftaran Gagal", task.getException());
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"Password Tidak Sama",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendToLogin() {
        Intent in=new Intent(Register.this, Main.class);
        startActivity(in);
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressDialog.dismiss();
        progressDialog=null;
        finish();
    }
}
