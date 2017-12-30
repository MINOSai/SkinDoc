package com.minosai.skindoc.auth.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.minosai.skindoc.R;
import com.minosai.skindoc.api.ApiClient;
import com.minosai.skindoc.api.ApiInterface;
import com.minosai.skindoc.auth.data.AuthResponse;
import com.minosai.skindoc.auth.data.LoginCredentials;
import com.minosai.skindoc.user.MainActivity;
import com.minosai.skindoc.user.UserDataStore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by minos.ai on 29/12/17.
 */

public class LoginFragment extends Fragment {

    private FragmentActivity listener;

    private TextView textUserName, textPassword, textBtnSignup;
    private FloatingActionButton fab;
    private Button btnSignup, btnLogin;

    private LoginCredentials loginCredentials = new LoginCredentials();

    private ProgressDialog loginProgress;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
            loginProgress = new ProgressDialog(context);
            loginProgress.setMessage("Logging you in...");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        textUserName = (TextView) view.findViewById(R.id.txt_login_username);
        textPassword = (TextView) view.findViewById(R.id.txt_login_password);
        textBtnSignup = (TextView) view.findViewById(R.id.txt_login_signup);

        fab = (FloatingActionButton) view.findViewById(R.id.fab_login);

        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnSignup = (Button) view.findViewById(R.id.btn_login_signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginCredentials.setUser(textUserName.getText().toString());
                loginCredentials.setPass(textPassword.getText().toString());
                login();
            }
        });

        textBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft =  getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_auth, new SignupFragment());
                ft.commit();
            }
        });
    }

    private void login() {
        loginProgress.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AuthResponse> call = apiInterface.loginUser(loginCredentials);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful()){
                    AuthResponse authResponse = response.body();
                    Toast.makeText(getContext(), authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    UserDataStore.getInstance().saveToken(getContext(), authResponse.getToken());
                    loginProgress.dismiss();
                    startActivity(new Intent(getContext(), MainActivity.class));
                } else {
                    Snackbar.make(getView(), "An error occurred", Snackbar.LENGTH_LONG)
                            .setAction("retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    login();
                                }
                            }).show();
                    loginProgress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Snackbar.make(getView(), "An error occurred", Snackbar.LENGTH_LONG)
                        .setAction("retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                login();
                            }
                        }).show();
                loginProgress.dismiss();
            }
        });
        //        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
}
