package com.minosai.skindoc.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.minosai.skindoc.R;
import com.minosai.skindoc.api.ApiClient;
import com.minosai.skindoc.api.ApiInterface;
import com.minosai.skindoc.auth.AuthActivity;
import com.minosai.skindoc.auth.data.AuthResponse;
import com.minosai.skindoc.auth.data.TokenString;
import com.minosai.skindoc.camera.CameraActivity;
import com.minosai.skindoc.chat.ChatActivity;
import com.minosai.skindoc.user.data.User;
import com.minosai.skindoc.user.fragment.MainActivityFragment;
import com.minosai.skindoc.user.utils.JWTUtils;
import com.minosai.skindoc.user.utils.UserDataStore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private User user;

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode("55733305212-ak8qek9fvkuaopqvhcf3qe9utme29np6.apps.googleusercontent.com")
                .requestIdToken("55733305212-ak8qek9fvkuaopqvhcf3qe9utme29np6.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);

//        if(account != null){
//            Log.i("Google user details", account.getId());
//            Log.i("Google user det-token", account.getIdToken());
//        } else {
//            startActivity(new Intent(MainActivity.this, AuthActivity.class));
//        }

        String token = UserDataStore.getInstance().getToken(this);

        if(token == null){
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
        } else {
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

            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_user, new MainActivityFragment());
            ft.commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                startActivity(new Intent(MainActivity.this, CameraActivity.class));
//                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
//                intent.putExtra(ChatActivity.USER_NODE, "minosaipatient-minosaidoctor");
//                startActivity(intent);
                buildDialog();
            }
        });
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Information");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("A short information");
        builder.setView(input);

        builder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newAppt(input.getText().toString());
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void newAppt(String s) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AuthResponse> call = apiInterface.logoutUser(new TokenString(UserDataStore.getInstance().getToken(getApplicationContext())));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful()){
                    AuthResponse authResponse = response.body();
                    Toast.makeText(getApplicationContext(), authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    UserDataStore.getInstance().clearData(getApplicationContext());

                    if (account != null) {
                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(MainActivity.this, AuthActivity.class));
                                    }
                                });
                    }

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

//        if (account != null) {
//            mGoogleSignInClient.signOut()
//                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            startActivity(new Intent(MainActivity.this, AuthActivity.class));
//                        }
//                    });
//        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
