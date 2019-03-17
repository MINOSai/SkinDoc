package com.minosai.skindoc.api;

import com.minosai.skindoc.auth.data.AuthResponse;
import com.minosai.skindoc.auth.data.LoginCredentials;
import com.minosai.skindoc.auth.data.SignupCredentials;
import com.minosai.skindoc.auth.data.TokenString;
import com.minosai.skindoc.user.data.api.AppointBody;
import com.minosai.skindoc.user.data.api.PredictBody;
import com.minosai.skindoc.user.data.api.PredictResponse;
import com.minosai.skindoc.user.data.api.ResolveBody;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by minos.ai on 29/12/17.
 */

public interface ApiInterface {

    @POST("login")
    Call<AuthResponse> loginUser(@Body LoginCredentials loginCredentials);

    @POST("signup")
    Call<AuthResponse> signupUser(@Body SignupCredentials signupCredentials);

    @POST("appoint")
    Call<AuthResponse> newAppointment(@Body AppointBody appointBody);

    @Multipart
    @POST("mlpredict")
    Call<PredictResponse> predictImage(@Part MultipartBody.Part file);

    @POST("logout")
    Call<AuthResponse> logoutUser(@Body TokenString tokenString);

    @POST("oauth")
    Call<AuthResponse> oauthTest(@Body TokenString tokenString);

    @POST("new")
    Call<AuthResponse> newToken(@Body TokenString tokenString);

    @POST("resolve")
    Call<AuthResponse> resolveAppointment(@Body ResolveBody resolveBody);
}
