package com.ngs_tech.newjsb.act;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ngs_tech.newjsb.R;

import java.util.ArrayList;

public class PictureLalulintas extends AppCompatActivity {

    private static final String TAG = PictureSPM.class.getSimpleName();

    private ListView listView;
    private ProgressBar mProgressBar;
    private ImageView btnChoose, btnUpload;

    private String getIDGambar  = "";
    private String getIDAlat    = "";
    private String getIDGerbang = "";
    private String getUUID      = "";


    private ArrayList<Uri> arrayList;

    private final int REQUEST_CODE_PERMISSIONS  = 1;
    private final int REQUEST_CODE_READ_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_lalulintas);
    }
}
