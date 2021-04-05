package com.ngs_tech.newjsb.sub_act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.act.HasilSpm;
import com.ngs_tech.newjsb.fragspm.FrSpmCoba;
import com.ngs_tech.newjsb.fragspm.FrSpmDelapan;
import com.ngs_tech.newjsb.fragspm.FrSpmDua;
import com.ngs_tech.newjsb.fragspm.FrSpmEmpat;
import com.ngs_tech.newjsb.fragspm.FrSpmEnam;
import com.ngs_tech.newjsb.fragspm.FrSpmLima;
import com.ngs_tech.newjsb.fragspm.FrSpmSatu;
import com.ngs_tech.newjsb.fragspm.FrSpmTiga;
import com.ngs_tech.newjsb.fragspm.FrSpmTujuh;

public class Pemeliharaan extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout lin1, lin2, lin3, lin4, lin5, lin6, lin7, lin8;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private TextView tvSpm1, tvSpm2, tvSpm3, tvSpm4, tvSpm5, tvSpm6, tvSpm7, tvSpm8;

    private ImageView imgReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemeliharaan);

        fragmentManager     = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, new FrSpmCoba()).commit();

        tvSpm1  = findViewById(R.id.tv_spm_1); lin1     = findViewById(R.id.lin_spm1);
        tvSpm2  = findViewById(R.id.tv_spm_2); lin2     = findViewById(R.id.lin_spm2);
        tvSpm3  = findViewById(R.id.tv_spm_3); lin3     = findViewById(R.id.lin_spm3);
        tvSpm4  = findViewById(R.id.tv_spm_4); lin4     = findViewById(R.id.lin_spm4);
        tvSpm5  = findViewById(R.id.tv_spm_5); lin5     = findViewById(R.id.lin_spm5);
        tvSpm6  = findViewById(R.id.tv_spm_6); lin6     = findViewById(R.id.lin_spm6);
        tvSpm7  = findViewById(R.id.tv_spm_7); lin7     = findViewById(R.id.lin_spm7);
        tvSpm8  = findViewById(R.id.tv_spm_8); lin8     = findViewById(R.id.lin_spm8);
        imgReport = findViewById(R.id.img_report);

        lin1.setOnClickListener(this);
        lin2.setOnClickListener(this);
        lin3.setOnClickListener(this);
        lin4.setOnClickListener(this);
        lin5.setOnClickListener(this);
        lin6.setOnClickListener(this);
        lin7.setOnClickListener(this);
        lin8.setOnClickListener(this);
        imgReport.setOnClickListener(this);

        tvSpm1.setTextColor(Color.BLACK);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_spm1:
                fragmentManager     = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new FrSpmCoba()).commit();
                tvSpm1.setTextColor(Color.BLACK);
                tvSpm2.setTextColor(Color.WHITE);
                tvSpm3.setTextColor(Color.WHITE);
                tvSpm4.setTextColor(Color.WHITE);
                tvSpm5.setTextColor(Color.WHITE);
                tvSpm6.setTextColor(Color.WHITE);
                tvSpm7.setTextColor(Color.WHITE);
                tvSpm8.setTextColor(Color.WHITE);
                break;
            case R.id.lin_spm2:
                fragmentManager     = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new FrSpmDua()).commit();
                tvSpm1.setTextColor(Color.WHITE);
                tvSpm2.setTextColor(Color.BLACK);
                tvSpm3.setTextColor(Color.WHITE);
                tvSpm4.setTextColor(Color.WHITE);
                tvSpm5.setTextColor(Color.WHITE);
                tvSpm6.setTextColor(Color.WHITE);
                tvSpm7.setTextColor(Color.WHITE);
                tvSpm8.setTextColor(Color.WHITE);
                break;
            case R.id.lin_spm3:
                fragmentManager     = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new FrSpmTiga()).commit();
                tvSpm1.setTextColor(Color.WHITE);
                tvSpm2.setTextColor(Color.WHITE);
                tvSpm3.setTextColor(Color.BLACK);
                tvSpm4.setTextColor(Color.WHITE);
                tvSpm5.setTextColor(Color.WHITE);
                tvSpm6.setTextColor(Color.WHITE);
                tvSpm7.setTextColor(Color.WHITE);
                tvSpm8.setTextColor(Color.WHITE);
                break;
            case R.id.lin_spm4:
                fragmentManager     = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new FrSpmEmpat()).commit();
                tvSpm1.setTextColor(Color.WHITE);
                tvSpm2.setTextColor(Color.WHITE);
                tvSpm3.setTextColor(Color.WHITE);
                tvSpm4.setTextColor(Color.BLACK);
                tvSpm5.setTextColor(Color.WHITE);
                tvSpm6.setTextColor(Color.WHITE);
                tvSpm7.setTextColor(Color.WHITE);
                tvSpm8.setTextColor(Color.WHITE);
                break;
            case R.id.lin_spm5:
                fragmentManager     = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new FrSpmLima()).commit();
                tvSpm1.setTextColor(Color.WHITE);
                tvSpm2.setTextColor(Color.WHITE);
                tvSpm3.setTextColor(Color.WHITE);
                tvSpm4.setTextColor(Color.WHITE);
                tvSpm5.setTextColor(Color.BLACK);
                tvSpm6.setTextColor(Color.WHITE);
                tvSpm7.setTextColor(Color.WHITE);
                tvSpm8.setTextColor(Color.WHITE);
                break;
            case R.id.lin_spm6:
                fragmentManager     = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new FrSpmEnam()).commit();
                tvSpm1.setTextColor(Color.WHITE);
                tvSpm2.setTextColor(Color.WHITE);
                tvSpm3.setTextColor(Color.WHITE);
                tvSpm4.setTextColor(Color.WHITE);
                tvSpm5.setTextColor(Color.WHITE);
                tvSpm6.setTextColor(Color.BLACK);
                tvSpm7.setTextColor(Color.WHITE);
                tvSpm8.setTextColor(Color.WHITE);
                break;
            case R.id.lin_spm7:
                fragmentManager     = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new FrSpmTujuh()).commit();
                tvSpm1.setTextColor(Color.WHITE);
                tvSpm2.setTextColor(Color.WHITE);
                tvSpm3.setTextColor(Color.WHITE);
                tvSpm4.setTextColor(Color.WHITE);
                tvSpm5.setTextColor(Color.WHITE);
                tvSpm6.setTextColor(Color.WHITE);
                tvSpm7.setTextColor(Color.BLACK);
                tvSpm8.setTextColor(Color.WHITE);
                break;
            case R.id.lin_spm8:
                fragmentManager     = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new FrSpmDelapan()).commit();
                tvSpm1.setTextColor(Color.WHITE);
                tvSpm2.setTextColor(Color.WHITE);
                tvSpm3.setTextColor(Color.WHITE);
                tvSpm4.setTextColor(Color.WHITE);
                tvSpm5.setTextColor(Color.WHITE);
                tvSpm6.setTextColor(Color.WHITE);
                tvSpm7.setTextColor(Color.WHITE);
                tvSpm8.setTextColor(Color.BLACK);
                break;
            case R.id.img_report:
                Intent intent = new Intent(Pemeliharaan.this, HasilSpm.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
