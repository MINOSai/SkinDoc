package com.minosai.skindoc.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minosai.skindoc.R;
import com.minosai.skindoc.api.ApiClient;
import com.minosai.skindoc.api.ApiInterface;
import com.minosai.skindoc.auth.data.AuthResponse;
import com.minosai.skindoc.auth.data.SignupCredentials;
import com.minosai.skindoc.user.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by minos.ai on 29/12/17.
 */

public class SignupFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity listener;

    private TextView textUserName, textEmail, textPassword, textCity;
    private TextView textQualify, textDescription;
    Button btnPatient, btnDoctor, btnLogin;
    FloatingActionButton fab;
    LinearLayout doctorLayout;

    SignupCredentials signupCredentials;

    private ProgressDialog signupProgress;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
            signupProgress = new ProgressDialog(context);
            signupProgress.setMessage("Signing you up...");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
        implementMethods();
    }

    private void implementMethods() {
        btnDoctor.setOnClickListener(this);
        btnPatient.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    private void initViews(View view) {
        textUserName = (TextView) view.findViewById(R.id.txt_signup_username);
        textEmail = (TextView) view.findViewById(R.id.txt_signup_email);
        textPassword = (TextView) view.findViewById(R.id.txt_signup_password);
        textQualify = (TextView) view.findViewById(R.id.txt_signup_qualify);
        textDescription = (TextView) view.findViewById(R.id.txt_signup_description);

        btnDoctor = (Button) view.findViewById(R.id.btn_signup_doctor);
        btnPatient = (Button) view.findViewById(R.id.btn_signup_patient);
        btnLogin = (Button) view.findViewById(R.id.btn_signup_login);

        fab = (FloatingActionButton) view.findViewById(R.id.fab_signup);
        doctorLayout = (LinearLayout) view.findViewById(R.id.layout_signup_doctor_details);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_signup_doctor :
                userDoctor();
                break;
            case R.id.btn_signup_patient :
                userPatient();
                break;
            case R.id.btn_signup_login :
                FragmentTransaction ft =  getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_auth, new LoginFragment());
                ft.commit();
                break;
            case R.id.fab_signup :
                signup();
                break;
        }

    }

    private void userPatient() {
        btnDoctor.setTextColor(getResources().getColor(R.color.mBlack));
        btnPatient.setTextColor(getResources().getColor(R.color.colorAccent));
        doctorLayout.setVisibility(View.GONE);
    }

    private void signup() {
        signupProgress.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AuthResponse> call = apiInterface.signupUser(signupCredentials);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful()){
                    AuthResponse authResponse = response.body();
                    Toast.makeText(getContext(), authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    saveToken(authResponse.getToken());
                    signupProgress.dismiss();
                } else {
                    Snackbar.make(getView(), "An error occurred", Snackbar.LENGTH_LONG)
                            .setAction("retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    signup();
                                }
                            }).show();
                    signupProgress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Snackbar.make(getView(), "An error occurred", Snackbar.LENGTH_LONG)
                        .setAction("retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                signup();
                            }
                        }).show();
                signupProgress.dismiss();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences preferences = getContext().getSharedPreferences(MainActivity.TOKEN_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MainActivity.TOKEN_PREF, token);
        editor.commit();
    }

    private void userDoctor() {
        btnDoctor.setTextColor(getResources().getColor(R.color.colorAccent));
        btnPatient.setTextColor(getResources().getColor(R.color.mBlack));
        doctorLayout.setVisibility(View.VISIBLE);
    }
}
