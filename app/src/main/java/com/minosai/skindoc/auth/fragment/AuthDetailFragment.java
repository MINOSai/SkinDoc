package com.minosai.skindoc.auth.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.minosai.skindoc.R;
import com.minosai.skindoc.api.ApiClient;
import com.minosai.skindoc.api.ApiInterface;
import com.minosai.skindoc.auth.AuthActivity;
import com.minosai.skindoc.auth.data.AuthResponse;
import com.minosai.skindoc.auth.data.TokenString;
import com.minosai.skindoc.user.MainActivity;
import com.minosai.skindoc.user.utils.UserDataStore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by minos.ai on 03/01/18.
 */

//TODO: use checkbox correctly

public class AuthDetailFragment extends Fragment{

    private FragmentActivity listener;

    TextInputLayout txtUsername, txtDescription, txtQualification;
    FloatingActionButton fab;
    CheckBox doctorCheckBox;
    LinearLayout doctorLayout;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authdetail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode("55733305212-ak8qek9fvkuaopqvhcf3qe9utme29np6.apps.googleusercontent.com")
                .requestIdToken("55733305212-ak8qek9fvkuaopqvhcf3qe9utme29np6.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if(account != null){
            UserDataStore.getInstance().signupCredentials.setEmail(account.getEmail());
            UserDataStore.getInstance().signupCredentials.setGoogle("True");
            UserDataStore.getInstance().signupCredentials.setPass(null);
        } else {
            UserDataStore.getInstance().signupCredentials.setGoogle(null);
        }

        initViews(view);

        UserDataStore.getInstance().signupCredentials.setPortal("1");
        doctorCheckBox.setChecked(false);
        doctorLayout.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDetails();
            }
        });

        doctorCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(doctorCheckBox.isChecked()) {
                    doctorCheckBox.setChecked(true);
                    doctorLayout.setVisibility(View.VISIBLE);
                    UserDataStore.getInstance().signupCredentials.setPortal("0");
                } else {
                    doctorCheckBox.setChecked(false);
                    doctorLayout.setVisibility(View.GONE);
                    UserDataStore.getInstance().signupCredentials.setPortal("1");
                }
            }
        });
    }

    private void initViews(View view) {
        txtUsername = (TextInputLayout) view.findViewById(R.id.txt_authdetail_username);
        txtDescription = (TextInputLayout) view.findViewById(R.id.txt_aithdetail_description);
        txtQualification = (TextInputLayout) view.findViewById(R.id.txt_authdetail_qualification);
        fab = (FloatingActionButton) view.findViewById(R.id.fab_authdetail_done);
        doctorCheckBox = (CheckBox) view.findViewById(R.id.checkBox_isdoctor);
        doctorLayout = (LinearLayout) view.findViewById(R.id.layout_authdetail_doctor);
    }

    private void getDetails() {
        UserDataStore.getInstance().signupCredentials.setUser(txtUsername.getEditText().getText().toString());
        if (doctorCheckBox.isChecked()) {
            UserDataStore.getInstance().signupCredentials.setQualification(txtQualification.getEditText().getText().toString());
            UserDataStore.getInstance().signupCredentials.setDescription(txtDescription.getEditText().getText().toString());
            UserDataStore.getInstance().signupCredentials.setPortal("0");
        } else {
            UserDataStore.getInstance().signupCredentials.setPortal("1");
        }
        signupUser();
    }

    //TODO: show progressbars
    private void signupUser() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Log.i("signupdetail", UserDataStore.getInstance().signupCredentials.getEmail());
        Call<AuthResponse> call = apiInterface.signupUser(UserDataStore.getInstance().signupCredentials);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful()){
                    AuthResponse authResponse = response.body();
                    UserDataStore.getInstance().saveToken(getContext(), authResponse.getToken());
                    startActivity(new Intent(getContext(), MainActivity.class));
                } else {
                    Snackbar.make(getView(), "An error occurred", Snackbar.LENGTH_LONG)
                            .setAction("retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    signupUser();
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Snackbar.make(getView(), "Error occurred", Snackbar.LENGTH_LONG)
                        .setAction("retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                signupUser();
                            }
                        }).show();
            }
        });
    }
}
