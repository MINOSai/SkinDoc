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

    private TextView textUserName, textEmail, textPassword, txtBtnLogin;
    private Button btnContinue;

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
        initViews(view);
        implementMethods();
    }

    private void implementMethods() {
        txtBtnLogin.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    private void initViews(View view) {
        textUserName = (TextView) view.findViewById(R.id.txt_signup_username);
        textEmail = (TextView) view.findViewById(R.id.txt_signup_email);
        textPassword = (TextView) view.findViewById(R.id.txt_signup_password);
        txtBtnLogin = (TextView) view.findViewById(R.id.txt_signup_login);

        btnContinue = (Button) view.findViewById(R.id.btn_signup_continue);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txt_signup_login :
                FragmentTransaction loginft =  getFragmentManager().beginTransaction();
                loginft.replace(R.id.frame_auth, new LoginFragment());
                loginft.commit();
                break;
            case R.id.btn_signup_continue :
                UserDataStore.getInstance().signupCredentials.setName(textUserName.getText().toString());
                UserDataStore.getInstance().signupCredentials.setEmail(textEmail.getText().toString());
                UserDataStore.getInstance().signupCredentials.setPass(textPassword.getText().toString());

                FragmentTransaction continueft = getFragmentManager().beginTransaction();
                continueft.replace(R.id.frame_auth, new AuthDetailFragment());
                continueft.commit();
                break;
        }

    }

}
