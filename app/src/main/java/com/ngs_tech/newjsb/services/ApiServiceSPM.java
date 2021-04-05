package com.ngs_tech.newjsb.services;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiServiceSPM {
    String BASE_URL = "http://mudikqu.com/";

    @Multipart
    @POST("uploadspm.php")
    Call<ResponseBody> uploadMultiple(
            @Part("id_alat") RequestBody id_alat,
            @Part("size") RequestBody size,
            @Part("id_gerbang") RequestBody id_gerbang,
            @Part("uuid") RequestBody uuid,
            @Part("id_gambar") RequestBody id_gambar,
            @Part List<MultipartBody.Part> files);
}
