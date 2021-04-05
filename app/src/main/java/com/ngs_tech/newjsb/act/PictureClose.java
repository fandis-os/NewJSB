package com.ngs_tech.newjsb.act;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.services.ApiService;
import com.ngs_tech.newjsb.services.ApiServiceClose;
import com.ngs_tech.newjsb.utils.FileUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PictureClose extends AppCompatActivity {



    private static final String TAG = TakePicture.class.getSimpleName();

    private ListView listView;
    private ProgressBar mProgressBar;
    private ImageView btnChoose, btnUpload, btnCam;

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
        setContentView(R.layout.activity_picture_close);

        Intent intent = getIntent();
        getIDGambar     = intent.getStringExtra("gambar");
        getIDAlat       = intent.getStringExtra("id_alat");
        getIDGerbang    = intent.getStringExtra("id_gerbang");
        getUUID         = intent.getStringExtra("uuid");

//        Toast.makeText(this, getUUID+"---"+getIDGambar, Toast.LENGTH_LONG).show();

        listView = findViewById(R.id.listView);
        mProgressBar = findViewById(R.id.progressBar);

        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        btnCam    = findViewById(R.id.btnCam);

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCam();
            }
        });

        arrayList = new ArrayList<>();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                uploadImagesToServer();
            }
        });

    }

    private void showCam() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(3,4)
                .start(PictureClose.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == RESULT_OK) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(resultData);
                if (resultCode == RESULT_OK) {
                    if (resultData != null) {
                        Uri imageUri = result.getUri();
                        try {
                            arrayList.add(imageUri);
                            MyAdapter mAdapter = new MyAdapter(PictureClose.this, arrayList);
                            listView.setAdapter(mAdapter);
                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Uri> arrayList;

        public MyAdapter(Context context, ArrayList<Uri> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            if (mInflater!=null){
                convertView = mInflater.inflate(R.layout.item_image, parent, false);
            }
            ImageView imageView = convertView.findViewById(R.id.imageView);
            TextView imagePath = convertView.findViewById(R.id.imagePath);

            imagePath.setText(FileUtils.getPath(context, arrayList.get(position)));

            Glide.with(context)
                    .load(arrayList.get(position))
                    .into(imageView);

            return convertView;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadImagesToServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiServiceClose.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        showProgress();

        // create list of file parts (photo, video, ...)
        List<MultipartBody.Part> parts = new ArrayList<>();
        // create upload service client
        ApiServiceClose service = retrofit.create(ApiServiceClose.class);
        if (arrayList != null) {
            // create part for file (photo, video, ...)
            for (int i = 0; i < arrayList.size(); i++) {
                parts.add(prepareFilePart("image"+i, arrayList.get(i)));
            }
        }

        // create a map of data to pass along
        RequestBody description = createPartFromString(getIDGambar);
        RequestBody id_gerbang = createPartFromString(getIDGerbang);
        RequestBody uuid = createPartFromString(getUUID);
        RequestBody id_alat = createPartFromString(getIDAlat);
        RequestBody size = createPartFromString(""+parts.size());

        // finally, execute the request
        Call<ResponseBody> call = service.uploadMultiple(description, id_gerbang, uuid, id_alat, size, parts);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideProgress();
                if(response.isSuccessful()) {
//                    Toast.makeText(PictureClose.this, "Images successfully uploaded!", Toast.LENGTH_SHORT).show();
                    AndroidNetworking.post("http://114.7.131.86/toll_api/index.php/api/Editproses/")
//                        .addBodyParameter("id", hasilSpmModel.getId())
                        .addBodyParameter("uuid",getUUID)
                        .addBodyParameter("gambar",getIDGambar)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Toast.makeText(getApplicationContext(), "Oke edit berhasil", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(), "Oke edit gagal", Toast.LENGTH_LONG).show();
                            }
                        });
                }else {
                    Snackbar.make(findViewById(android.R.id.content),
                            "Something went wrong", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgress();
                Log.e(TAG, "Image upload failed!", t);
                Snackbar.make(findViewById(android.R.id.content),
                        "Image upload failed!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = FileUtils.getFile(this, fileUri);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create (MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse("text/*"), descriptionString);
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        btnChoose.setEnabled(false);
        btnUpload.setVisibility(View.GONE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        btnChoose.setEnabled(true);
        btnUpload.setVisibility(View.VISIBLE);
    }
}
