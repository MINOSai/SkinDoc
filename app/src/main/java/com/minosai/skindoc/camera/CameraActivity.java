package com.minosai.skindoc.camera;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.minosai.skindoc.R;
import com.minosai.skindoc.auth.fragment.LoginFragment;
import com.minosai.skindoc.camera.fragment.CameraFragment;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getSupportActionBar().hide();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_camera, new CameraFragment());
        ft.commit();
    }
}
