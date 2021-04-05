package com.ngs_tech.newjsb.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.ngs_tech.newjsb.R;
import com.ngs_tech.newjsb.adapters.PrltTolAdapter;
import com.ngs_tech.newjsb.models.PeralatanTolModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class FragPeralatanToll extends Fragment implements View.OnClickListener{

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<PeralatanTolModel> alatTolModelList;
    private RecyclerView.Adapter adapter;

    private String url  = "http://114.7.131.86/toll_api/index.php/api/SpmList/1";

    private ImageView imgTtd1, imgTtd2, imgTtd3, imgTtd4;
    //    DIALOG TTD
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

    // Creating Separate Directory for saving Generated Images
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";
    String DIRECTORY2 = Environment.getExternalStorageDirectory().getPath() + "/DigitSign2/";
    String DIRECTORY3 = Environment.getExternalStorageDirectory().getPath() + "/DigitSign3/";
    String DIRECTORY4 = Environment.getExternalStorageDirectory().getPath() + "/DigitSign4/";

    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath   = DIRECTORY + pic_name + ".png";
    String StoredPath2  = DIRECTORY2 + pic_name + ".png";
    String StoredPath3  = DIRECTORY3 + pic_name + ".png";
    String StoredPath4  = DIRECTORY4 + pic_name + ".png";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_peralatan_toll, container, false);

        mList                   = view.findViewById(R.id.recycle_alat_tol);
        alatTolModelList        = new ArrayList<>();
        adapter                 = new PrltTolAdapter(getContext(), alatTolModelList);
        linearLayoutManager     = new LinearLayoutManager(getActivity());
        dividerItemDecoration   = new DividerItemDecoration(mList.getContext(),linearLayoutManager.getOrientation());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        getData();

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

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sign);
        dialog.setCancelable(false);

        imgTtd1     = view.findViewById(R.id.img_ttd_1);

        imgTtd1.setOnClickListener(this);

        return view;
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        PeralatanTolModel peralatanTolModel = new PeralatanTolModel();
                        peralatanTolModel.setId_alat(jsonObject.getString("id_alat"));
                        peralatanTolModel.setNama_alat(jsonObject.getString("nama_alat"));

                        alatTolModelList.add(peralatanTolModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    private class signature extends View{

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

    private class signature2 {
    }

    private class signature3 {
    }

    private class signature4 {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_ttd_1:
                dialogTtdAction1();
                break;
            case R.id.img_ttd_2:
                break;
            case R.id.img_ttd_3:
                break;
            case R.id.img_ttd_4:
                break;
            default:
                break;
        }
    }

    private void dialogTtdAction1() {
        mContent        = dialog.findViewById(R.id.linearLayout);
        mSignature      = new signature(getActivity(), null);
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
                imgTtd1.setImageURI(Uri.parse(StoredPath));
                imgTtd1.setEnabled(false);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (resultCode == RESULT_OK ){
                // Tampung Data tempat ke variable
                Place placeData = PlaceAutocomplete.getPlace(getContext(), data);

//                if (requestCode==REQUEST_CODE){
//                    // Dapatkan Detail
//                    String placeAddress = placeData.getAddress().toString();
//                    LatLng placeLatLng = placeData.getLatLng();
//                    String placeName = placeData.getName().toString();
//
//                    // Cek user milih titik jemput atau titik tujuan
////                    lokasi.setText(placeAddress);
//                }

            }

        }catch (Exception e) {
            Log.d(TAG, "onActivityResult: "+e.toString());
            Toast.makeText(getContext(), "Silahkan coba lagi", Toast.LENGTH_LONG).show();
        }
    }
}
