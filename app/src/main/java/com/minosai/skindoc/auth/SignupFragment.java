package com.minosai.skindoc.auth;

import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minosai.skindoc.R;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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

        btnDoctor.setOnClickListener(this);
        btnPatient.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //TODO: methods for onClick
            case R.id.btn_signup_doctor : {
                btnDoctor.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
            }
            case R.id.btn_signup_login : {
                FragmentTransaction ft =  getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_auth, new LoginFragment());
                ft.commit();
                break;
            }
        }

    }
}
