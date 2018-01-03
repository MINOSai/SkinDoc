package com.minosai.skindoc.auth.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.minosai.skindoc.R;
import com.minosai.skindoc.api.ApiClient;
import com.minosai.skindoc.api.ApiInterface;
import com.minosai.skindoc.auth.data.AuthResponse;
import com.minosai.skindoc.auth.data.SignupCredentials;
import com.minosai.skindoc.user.MainActivity;
import com.minosai.skindoc.user.utils.UserDataStore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by minos.ai on 29/12/17.
 */

public class SignupFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity listener;

    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    private TextView textUserName, textEmail, textPassword, textCity;
    private TextView textQualify, textDescription;
    private Button btnPatient, btnDoctor, btnLogin, btnGoogleSignin;
    private FloatingActionButton fab;
    private LinearLayout doctorLayout;

    private SignupCredentials signupCredentials = new SignupCredentials();

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode("55733305212-ak8qek9fvkuaopqvhcf3qe9utme29np6.apps.googleusercontent.com")
                .requestIdToken("55733305212-ak8qek9fvkuaopqvhcf3qe9utme29np6.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        initViews(view);
        implementMethods();
    }

    private void implementMethods() {
        btnDoctor.setOnClickListener(this);
        btnPatient.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        fab.setOnClickListener(this);
        btnGoogleSignin.setOnClickListener(this);
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
        btnGoogleSignin = (Button) view.findViewById(R.id.btn_signup_google);

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
                signupCredentials.setFname(textUserName.getText().toString().split(" ")[0]);
                try {
                    signupCredentials.setLname(textUserName.getText().toString().split(" ")[1]);
                } catch (Exception e){
                    e.printStackTrace();
                }
                signupCredentials.setEmail(textEmail.getText().toString());
                signupCredentials.setPass(textPassword.getText().toString());
                if(signupCredentials.getPortal() == null){
                    Toast.makeText(listener, "Please choose a role.", Toast.LENGTH_SHORT).show();
                    break;
                } else if(signupCredentials.getPortal().matches("0")){
                    signupCredentials.setDescription(textDescription.getText().toString());
                    signupCredentials.setQualification(textQualify.getText().toString());
                }
                signup();
                break;
            case R.id.btn_signup_google:
                googleAuth();
                break;
        }

    }

    private void googleAuth() {
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
                Log.i("Google user signup ", account.getId());
                Log.i("Google user signup", account.getIdToken());
                startActivity(new Intent(getContext(), MainActivity.class));
            } catch (ApiException e) {
                Log.i("Google user signup", "FAILED");
                e.printStackTrace();
            }
        }
    }

    private void userPatient() {
        btnDoctor.setTextColor(getResources().getColor(R.color.mBlack));
        btnPatient.setTextColor(getResources().getColor(R.color.colorAccent));
        doctorLayout.setVisibility(View.GONE);

        signupCredentials.setPortal("1");
    }


    private void userDoctor() {
        btnDoctor.setTextColor(getResources().getColor(R.color.colorAccent));
        btnPatient.setTextColor(getResources().getColor(R.color.mBlack));
        doctorLayout.setVisibility(View.VISIBLE);

        signupCredentials.setPortal("0");
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
                    UserDataStore.getInstance().saveToken(getContext(), authResponse.getToken());
                    signupProgress.dismiss();
                    startActivity(new Intent(getContext(), MainActivity.class));
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
}
