package com.ngs_tech.newjsb.act;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.adapters.AdapterHasilSpm;
import com.ngs_tech.newjsb.adapters.AdapterSpmCoba;
import com.ngs_tech.newjsb.fragspm.FrSpmSatu;
import com.ngs_tech.newjsb.models.HasilSpmModel;
import com.ngs_tech.newjsb.models.SpmSatuModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HasilSpm extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<HasilSpmModel> hasilSpmModelist;
    private RecyclerView.Adapter adapter;

    private String url = "http://114.7.131.86/toll_api/index.php/api/GetHasilSpm/";
    private FirebaseAuth firebaseAuth;
    private String user_id=null;


    private ImageView imgTtd1, imgTtd2, imgTtd3, imgTtd4;
    private ImageView mClear, mGetSign;
    private ImageView mClear2, mGetSign2;
    private ImageView mClear3, mGetSign3;
    private ImageView mClear4, mGetSign4;

    File file;
    File file2;
    File file3;
    File file4;

    Dialog dialog;
    Dialog dialog2;
    Dialog dialog3;
    Dialog dialog4;

    LinearLayout mContent;
    LinearLayout mContent2;
    LinearLayout mContent3;
    LinearLayout mContent4;

    View view;
    View view2;
    View view3;
    View view4;

    signature mSignature;
    signature2 mSignature2;
    signature3 mSignature3;
    signature4 mSignature4;

    Bitmap bitmap;
    Bitmap bitmap2;
    Bitmap bitmap3;
    Bitmap bitmap4;

    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";
    String DIRECTORY2 = Environment.getExternalStorageDirectory().getPath() + "/DigitSign2/";
    String DIRECTORY3 = Environment.getExternalStorageDirectory().getPath() + "/DigitSign3/";
    String DIRECTORY4 = Environment.getExternalStorageDirectory().getPath() + "/DigitSign4/";

    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath   = DIRECTORY + pic_name + ".png";
    String StoredPath2  = DIRECTORY2 + pic_name + ".png";
    String StoredPath3  = DIRECTORY3 + pic_name + ".png";
    String StoredPath4  = DIRECTORY4 + pic_name + ".png";

    EditText etContohPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_spm);

        AndroidNetworking.initialize(getApplicationContext());
        firebaseAuth        =FirebaseAuth.getInstance();
        user_id             =firebaseAuth.getCurrentUser().getUid();

        etContohPath = findViewById(R.id.et_contoh_path);

        mList                   = findViewById(R.id.recycle_alat_tol);
        hasilSpmModelist        = new ArrayList<>();
        adapter                 = new AdapterHasilSpm(getApplicationContext(), hasilSpmModelist);
        linearLayoutManager     = new LinearLayoutManager(HasilSpm.this);
        dividerItemDecoration   = new DividerItemDecoration(mList.getContext(),linearLayoutManager.getOrientation());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        getData(user_id);

        file    = new File(DIRECTORY);
        if (!file.exists()){
            file.mkdir();
        }
        file2   = new File(DIRECTORY2);
        if (!file2.exists()){
            file2.mkdir();
        }
        file3   = new File(DIRECTORY3);
        if (!file3.exists()){
            file3.mkdir();
        }
        file4   = new File(DIRECTORY4);
        if (!file4.exists()){
            file4.mkdir();
        }

        dialog = new Dialog(HasilSpm.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sign);
        dialog.setCancelable(false);

        dialog2 = new Dialog(HasilSpm.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.dialog_sign);
        dialog2.setCancelable(false);

        dialog3 = new Dialog(HasilSpm.this);
        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog3.setContentView(R.layout.dialog_sign);
        dialog3.setCancelable(false);

        dialog4 = new Dialog(HasilSpm.this);
        dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog4.setContentView(R.layout.dialog_sign);
        dialog4.setCancelable(false);

        imgTtd1     = findViewById(R.id.img_ttd_1);
        imgTtd2     = findViewById(R.id.img_ttd_2);
        imgTtd3     = findViewById(R.id.img_ttd_3);
        imgTtd4     = findViewById(R.id.img_ttd_4);

        imgTtd1.setOnClickListener(this);
        imgTtd2.setOnClickListener(this);
        imgTtd3.setOnClickListener(this);
        imgTtd4.setOnClickListener(this);
    }

    private void getData(String user_id) {
        final ProgressDialog progressDialog = new ProgressDialog(HasilSpm.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        AndroidNetworking.get(url+user_id)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0; i<response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                HasilSpmModel hasilSpmModel = new HasilSpmModel();
                                hasilSpmModel.setUuid(jsonObject.getString("uuid"));
                                hasilSpmModel.setId_gerbang(jsonObject.getString("id_gerbang"));
                                hasilSpmModel.setId_list(jsonObject.getString("id_list"));
                                hasilSpmModel.setSta(jsonObject.getString("sta"));
                                hasilSpmModel.setLajur(jsonObject.getString("lajur"));
                                hasilSpmModel.setJalur(jsonObject.getString("jalur"));
                                hasilSpmModel.setKon_saat_ini(jsonObject.getString("kon_saat_ini"));
                                hasilSpmModel.setTolak_ukur(jsonObject.getString("tolak_ukur"));
                                hasilSpmModel.setKriteria(jsonObject.getString("kriteria"));
                                hasilSpmModel.setStatus(jsonObject.getString("status"));
                                hasilSpmModel.setId_gambar(jsonObject.getString("id_gambar"));
                                hasilSpmModel.setCreate_at(jsonObject.getString("create_at"));
                                hasilSpmModel.setIndikator(jsonObject.getString("indikator"));
                                hasilSpmModel.setSub_indikator(jsonObject.getString("sub_indikator"));
                                hasilSpmModel.setNama_kat(jsonObject.getString("nama_kat"));
                                hasilSpmModelist.add(hasilSpmModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(HasilSpm.this, "Data Belum Ada...", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_ttd_1:
                dialogTtdAction1();
                break;
            case R.id.img_ttd_2:
                dialogTtdAction2();
                break;
            case R.id.img_ttd_3:
                dialogTtdAction3();
                break;
            case R.id.img_ttd_4:
                dialogTtdAction4();
                break;
            default:
                break;
        }
    }

    private void dialogTtdAction4() {
        mContent4        = dialog4.findViewById(R.id.linearLayout);
        mSignature4      = new signature4(getApplicationContext(), null);
        mSignature4.setBackgroundColor(Color.WHITE);
        mContent4.addView(mSignature4, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear4          = dialog4.findViewById(R.id.clear);
        mGetSign4        = dialog4.findViewById(R.id.getsign);
        mGetSign4.setEnabled(false);
        view4            = mContent4;

        mClear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignature4.clear();
                mGetSign4.setEnabled(false);
            }
        });

        mGetSign4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view4.setDrawingCacheEnabled(true);
                mSignature4.save(view4, StoredPath4);
                imgTtd4.setImageURI(Uri.parse(StoredPath4));
                imgTtd4.setEnabled(false);
                dialog4.dismiss();
            }
        });
        dialog4.show();
    }

    private void dialogTtdAction3() {
        mContent3        = dialog3.findViewById(R.id.linearLayout);
        mSignature3      = new signature3(getApplicationContext(), null);
        mSignature3.setBackgroundColor(Color.WHITE);
        mContent3.addView(mSignature3, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear3          = dialog3.findViewById(R.id.clear);
        mGetSign3        = dialog3.findViewById(R.id.getsign);
        mGetSign3.setEnabled(false);
        view3            = mContent3;

        mClear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignature3.clear();
                mGetSign3.setEnabled(false);
            }
        });

        mGetSign3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view3.setDrawingCacheEnabled(true);
                mSignature3.save(view3, StoredPath3);
                imgTtd3.setImageURI(Uri.parse(StoredPath3));
                imgTtd3.setEnabled(false);
                dialog3.dismiss();
            }
        });
        dialog3.show();
    }

    private void dialogTtdAction2() {
        mContent2        = dialog2.findViewById(R.id.linearLayout);
        mSignature2      = new signature2(getApplicationContext(), null);
        mSignature2.setBackgroundColor(Color.WHITE);
        mContent2.addView(mSignature2, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear2          = dialog2.findViewById(R.id.clear);
        mGetSign2        = dialog2.findViewById(R.id.getsign);
        mGetSign2.setEnabled(false);
        view2            = mContent2;

        mClear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignature2.clear();
                mGetSign2.setEnabled(false);
            }
        });

        mGetSign2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view2.setDrawingCacheEnabled(true);
                mSignature2.save(view2, StoredPath2);
                imgTtd2.setImageURI(Uri.parse(StoredPath2));
                imgTtd2.setEnabled(false);
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    private void dialogTtdAction1() {
        mContent        = dialog.findViewById(R.id.linearLayout);
        mSignature      = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear          = dialog.findViewById(R.id.clear);
        mGetSign        = dialog.findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        view            = mContent;

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignature.clear();
                mGetSign.setEnabled(false);
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setDrawingCacheEnabled(true);
                mSignature.save(view, StoredPath);
                etContohPath.setText(StoredPath);
                uploadImageTTD(StoredPath);
                imgTtd1.setImageURI(Uri.parse(StoredPath));
                imgTtd1.setEnabled(false);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void uploadImageTTD(String storedPath) {
        Uri file_uri = Uri.parse(storedPath);
        File file = new File(String.valueOf(file_uri));
        AndroidNetworking.upload("http://114.7.131.86/Uploadttd.php")
                .addMultipartFile("image", file)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Berhasuil", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private class signature4 extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature4(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        @SuppressLint("WrongThread")
        public void save(View v, String StoredPath4){
            if (bitmap4 == null){
                bitmap4  = Bitmap.createBitmap(mContent4.getWidth(), mContent4.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap4);
            try {
                FileOutputStream mFileOutStream     = new FileOutputStream(StoredPath4);
                v.draw(canvas);
                bitmap4.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();
            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign4.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }


        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }


    }

    private class signature3 extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature3(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }


        @SuppressLint("WrongThread")
        public void save(View v, String StoredPath3){
            if (bitmap3 == null){
                bitmap3  = Bitmap.createBitmap(mContent3.getWidth(), mContent3.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap3);
            try {
                FileOutputStream mFileOutStream     = new FileOutputStream(StoredPath3);
                v.draw(canvas);
                bitmap3.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();
            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign3.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }


        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }

    }


    private class signature2 extends View {
        private static final float STROKE_WIDTH         = 5f;
        private static final float HALF_STROKE_WIDTH    = STROKE_WIDTH / 2;
        private Paint paint     = new Paint();
        private Path path       = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect   = new RectF();

        public signature2(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        @SuppressLint("WrongThread")
        public void save(View v, String StoredPath2){
            if (bitmap2 == null){
                bitmap2  = Bitmap.createBitmap(mContent2.getWidth(), mContent2.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap2);
            try {
                FileOutputStream mFileOutStream     = new FileOutputStream(StoredPath2);
                v.draw(canvas);
                bitmap2.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();
            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign2.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;

        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }



    }


    private class signature extends View {
        private static final float STROKE_WIDTH         = 5f;
        private static final float HALF_STROKE_WIDTH    = STROKE_WIDTH / 2;
        private Paint paint     = new Paint();
        private Path path       = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect   = new RectF();

        public signature(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        @SuppressLint("WrongThread")
        public void save(View v, String StoredPath){
            if (bitmap == null){
                bitmap  = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                FileOutputStream mFileOutStream     = new FileOutputStream(StoredPath);
                v.draw(canvas);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();
            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;

        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }

    }
}
