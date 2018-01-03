package com.minosai.skindoc.auth;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.minosai.skindoc.R;
import com.minosai.skindoc.auth.fragment.AuthFragment;
import com.minosai.skindoc.auth.fragment.LoginFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        getSupportActionBar().hide();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_auth, new AuthFragment(), "AuthFragment");
        ft.addToBackStack("AuthFragmentTag");
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
