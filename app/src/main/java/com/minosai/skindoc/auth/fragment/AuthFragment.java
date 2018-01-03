package com.minosai.skindoc.auth.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.minosai.skindoc.R;
import com.minosai.skindoc.api.ApiClient;
import com.minosai.skindoc.api.ApiInterface;
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

public class AuthFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity listener;

    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    Button btnGoogle, btnLogin, btnSignup;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode("55733305212-ak8qek9fvkuaopqvhcf3qe9utme29np6.apps.googleusercontent.com")
                .requestIdToken("55733305212-ak8qek9fvkuaopqvhcf3qe9utme29np6.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        btnGoogle = (Button) view.findViewById(R.id.btn_auth_google);
        btnLogin = (Button) view.findViewById(R.id.btn_auth_login);
        btnSignup = (Button) view.findViewById(R.id.btn_auth_signup);

        btnGoogle.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_auth_google :
                googleLogin();
                break;
            case R.id.btn_auth_login :
                FragmentTransaction loginft =  getFragmentManager().beginTransaction();
                loginft.replace(R.id.frame_auth, new LoginFragment());
                loginft.commit();
                break;
            case R.id.btn_auth_signup :
                FragmentTransaction signupft =  getFragmentManager().beginTransaction();
                signupft.replace(R.id.frame_auth, new SignupFragment());
                signupft.commit();
                break;
        }
    }

    private void googleLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_GOOGLE_SIGN_IN && resultCode == Activity.RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                UserDataStore.getInstance().setGoogleToken(getContext(), account.getIdToken());
                Log.i("GOOGLE_TOKEN", account.getIdToken());
                sendToken(account.getIdToken());
            } catch (ApiException e) {
                Log.i("Google user signup", "FAILED");
                e.printStackTrace();
            }
        }
    }

    private void sendToken(final String idToken) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AuthResponse> call = apiInterface.oauthTest(new TokenString(idToken));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful()){
                    AuthResponse authResponse = response.body();
                    if(authResponse.getMessage().equals("Signup required")) {
                        FragmentTransaction signupdetailsft = getFragmentManager().beginTransaction();
                        signupdetailsft.replace(R.id.frame_auth, new AuthDetailFragment());
                        signupdetailsft.commit();
                    } else {
                        UserDataStore.getInstance().saveToken(getContext(), authResponse.getToken());
                        Toast.makeText(getContext(), authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                } else {
                    Snackbar.make(getView(), "An error occurred", Snackbar.LENGTH_LONG)
                            .setAction("retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    sendToken(idToken);
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Snackbar.make(getView(), "An error occurred", Snackbar.LENGTH_LONG)
                        .setAction("retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sendToken(idToken);
                            }
                        }).show();
            }
        });
    }
}
