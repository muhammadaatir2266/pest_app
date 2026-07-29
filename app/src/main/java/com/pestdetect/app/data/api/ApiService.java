package com.pestdetect.app.data.api;

import com.pestdetect.app.data.models.ApiResponse;
import com.pestdetect.app.data.models.ScanResponse;
import com.pestdetect.app.data.models.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/signup")
    Call<ApiResponse<Map<String, Object>>> signup(@Body Map<String, String> body);

    @POST("auth/login")
    Call<ApiResponse<Map<String, Object>>> login(@Body Map<String, String> body);

    @POST("auth/guest-login")
    Call<ApiResponse<Map<String, Object>>> guestLogin();

    @Multipart
    @POST("scans")
    Call<ApiResponse<ScanResponse>> uploadScanImage(
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part image
    );

    @GET("scans")
    Call<ApiResponse<Map<String, Object>>> getScanHistory(
            @Header("Authorization") String authHeader
    );

    @GET("users/me")
    Call<ApiResponse<User>> getProfile(
            @Header("Authorization") String authHeader
    );
}
