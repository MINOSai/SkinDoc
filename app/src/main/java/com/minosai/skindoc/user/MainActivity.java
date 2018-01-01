package com.minosai.skindoc.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.minosai.skindoc.R;
import com.minosai.skindoc.api.ApiClient;
import com.minosai.skindoc.api.ApiInterface;
import com.minosai.skindoc.auth.AuthActivity;
import com.minosai.skindoc.auth.data.AuthResponse;
import com.minosai.skindoc.auth.data.LogoutCredentials;
import com.minosai.skindoc.camera.CameraActivity;
import com.minosai.skindoc.chat.ChatActivity;
import com.minosai.skindoc.user.data.User;
import com.minosai.skindoc.user.utils.JWTUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String token = UserDataStore.getInstance().getToken(this);

        if(token == null){
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
        } else {
            TextView textView = (TextView) findViewById(R.id.txt_test);
            String decodedJson = null;
            try {
                decodedJson = JWTUtils.decoded(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            user = UserDataStore.getInstance().getUser(this);
            if(user == null && decodedJson!= null) {
                UserDataStore.getInstance().setUser(this, decodedJson);
                user = UserDataStore.getInstance().getUser(this);
            }
            textView.setText(user.getFname());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }

        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_logout:
                logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AuthResponse> call = apiInterface.logoutUser(new LogoutCredentials(UserDataStore.getInstance().getToken(getApplicationContext())));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful()){
                    AuthResponse authResponse = response.body();
                    Toast.makeText(getApplicationContext(), authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    UserDataStore.getInstance().clearData(getApplicationContext());
                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
