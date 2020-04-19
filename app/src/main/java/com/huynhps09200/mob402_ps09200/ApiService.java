package com.huynhps09200.mob402_ps09200;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

interface ApiService {
    @Multipart
    @POST("/uploadd")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("uploadd") RequestBody name);
}
