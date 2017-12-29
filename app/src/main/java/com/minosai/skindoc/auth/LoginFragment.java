package com.minosai.skindoc.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.minosai.skindoc.R;
import com.minosai.skindoc.user.MainActivity;

/**
 * Created by minos.ai on 29/12/17.
 */

public class LoginFragment extends Fragment {

    private FragmentActivity listener;

    private TextView textUserName, textPassword;
    private FloatingActionButton fab;
    private Button btnSignup;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
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

        fab = (FloatingActionButton) view.findViewById(R.id.fab_login);

        btnSignup = (Button) view.findViewById(R.id.btn_login_signup);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = textUserName.getText().toString();
                String password = textPassword.getText().toString();
                login(userName, password);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft =  getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_auth, new SignupFragment());
                ft.commit();
            }
        });
    }

    private void login(String userName, String password) {
        //TODO: login
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
}
