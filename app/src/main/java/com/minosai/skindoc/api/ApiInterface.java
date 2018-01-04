package com.minosai.skindoc.api;

import com.minosai.skindoc.auth.data.AuthResponse;
import com.minosai.skindoc.auth.data.LoginCredentials;
import com.minosai.skindoc.auth.data.SignupCredentials;
import com.minosai.skindoc.auth.data.TokenString;
import com.minosai.skindoc.user.data.api.AppointBody;
import com.minosai.skindoc.user.data.api.ResolveBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by minos.ai on 29/12/17.
 */

public interface ApiInterface {

    @POST("login")
    Call<AuthResponse> loginUser(@Body LoginCredentials loginCredentials);

    @POST("signup")
    Call<AuthResponse> signupUser(@Body SignupCredentials signupCredentials);

    @POST("logout")
    Call<AuthResponse> logoutUser(@Body TokenString tokenString);

    @POST("oauth")
    Call<AuthResponse> oauthTest(@Body TokenString tokenString);

    @POST("appoint")
    Call<AuthResponse> newAppointment(@Body AppointBody appointBody);

    @POST("new")
    Call<AuthResponse> newToken(@Body TokenString tokenString);

    @POST("resolve")
    Call<AuthResponse> resolveAppointment(@Body ResolveBody resolveBody);

    //TODO: define mlpredict
}
