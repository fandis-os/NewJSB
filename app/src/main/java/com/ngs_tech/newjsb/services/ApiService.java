package com.ngs_tech.newjsb.services;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    String BASE_URL = "http://114.7.131.86/";

    @Multipart
    @POST("upload2.php")
    Call<ResponseBody> uploadMultiple(
            @Part("description") RequestBody description,
            @Part("size") RequestBody size,
            @Part List<MultipartBody.Part> files);
}
