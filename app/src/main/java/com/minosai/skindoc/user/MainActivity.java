package com.minosai.skindoc.user;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.minosai.skindoc.user.data.User;
import com.minosai.skindoc.user.data.api.AppointBody;
import com.minosai.skindoc.user.data.api.PredictBody;
import com.minosai.skindoc.user.data.api.PredictResponse;
import com.minosai.skindoc.user.fragment.MainActivityFragment;
import com.minosai.skindoc.user.utils.JWTUtils;
import com.minosai.skindoc.user.utils.UserDataStore;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_ID = 234;
    public static final int RC_SOTRAGE_PERMISSION = 123;

    private User user;

    View mView;
    private ProgressDialog progressDialog;

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mView = findViewById(android.R.id.content);
        progressDialog = new ProgressDialog(this);

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

            Log.i("MAIN-ONSTART-TOKEN", token);

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

            getNewToken();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView = view;
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

    private void replaceFragment() {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_user, new MainActivityFragment());
        ft.commit();
    }

    private void buildDialog() {

        new MaterialDialog.Builder(this)
                .title("Appointment")
                .content("Give a quick information")
                .inputType(
                        InputType.TYPE_CLASS_TEXT)
                .positiveText("CONTINUE")
                .input(
                        "Information",
                        "",
                        false,
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                newAppt(input.toString());
                            }
                        })
                .show();

    }

    private void newAppt(final String description) {
        progressDialog.setMessage("Creating enw appointment...");
        progressDialog.show();

        String token = UserDataStore.getInstance().getToken(this);
        Log.i("API-NEWAPPT-TOKEN", token);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        AppointBody appointBody = new AppointBody(token, description);
        Call<AuthResponse> call = apiInterface.newAppointment(appointBody);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.i("API=NEWAPPT-RESPONSE", response.toString());
                if(response.isSuccessful()) {
                    getNewToken();
                } else {
                    Snackbar.make(mView, "Unable to create !", Snackbar.LENGTH_LONG)
                            .setAction("retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    newAppt(description);
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Snackbar.make(mView, "Unable to create", Snackbar.LENGTH_LONG)
                        .setAction("retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                newAppt(description);
                            }
                        }).show();
            }
        });
        progressDialog.dismiss();
    }

    public void getNewToken() {
        progressDialog.setMessage("Loading content...");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AuthResponse> call = apiInterface.newToken(new TokenString(UserDataStore.getInstance().getToken(getApplicationContext())));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.i("API-NEWTOKEN-RESPONSE",  response.toString());
                if(response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    UserDataStore.getInstance().saveToken(getApplicationContext(), authResponse.getToken());
                    replaceFragment();
                } else {
                    Snackbar.make(mView, "An error occurred !!", Snackbar.LENGTH_LONG)
                            .setAction("retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getNewToken();
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Snackbar.make(mView, "An error occurred", Snackbar.LENGTH_LONG)
                        .setAction("retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getNewToken();
                            }
                        }).show();
            }
        });
        progressDialog.dismiss();
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
                 break;
            case R.id.action_camera:
//                startActivity(new Intent(this, CameraActivity.class));
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PICK_IMAGE_ID);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                if(resultCode == RESULT_OK) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    checkSavePic();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void checkSavePic() {
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_SOTRAGE_PERMISSION);
            }
        } else {
            savePic();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_SOTRAGE_PERMISSION :
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    savePic();
                break;
        }
    }

    private void savePic() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/SkinDoc");
        myDir.mkdirs();
        String fname = "Image-"+UserDataStore.getInstance().getUser(this).getUser()+".jpg";
        File file = new File(myDir, fname);
        Log.i("IMAGE-SAVE", "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File imgFile = new File(root+"/SkinDoc/"+fname);
        predictImage(imgFile);
    }

    private void predictImage(final File file) {
        progressDialog.setMessage("Analysing image...");
        progressDialog.show();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("filefieldname", file.getName(), requestFile);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<PredictResponse> call = apiInterface.predictImage(body);
        call.enqueue(new Callback<PredictResponse>() {
            @Override
            public void onResponse(@NonNull Call<PredictResponse> call, @NonNull Response<PredictResponse> response) {
                Log.i("API-MLPREDICT-RESPONSE", response.toString());
                if(response.isSuccessful()) {
                    progressDialog.dismiss();
                    final PredictResponse predictResponse = response.body();
                    String predection = null;
                    if(predictResponse.getMessage().equals("positive")) {
                        predection = "We predicted you might have Melanoma with "+predictResponse.getConfidence()+" confidence";
                    } else {
                        predection = "We were not able to detect any cancer in your skin. Confidence: " + predictResponse.getConfidence();
                    }
                    final AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Results")
                            .setMessage(predection)
                            .setPositiveButton("make appointment", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String newAptStr = "Predicted to have melanoma with "+predictResponse.getConfidence()+" confidence";
                                    newAppt(newAptStr);
                                }
                            })
                            .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else {
                    progressDialog.dismiss();
                    Snackbar.make(mView, "Unable to predict", Snackbar.LENGTH_LONG)
                            .setAction("retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    predictImage(file);
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<PredictResponse> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(mView, "An error occurred", Snackbar.LENGTH_LONG)
                        .setAction("retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                predictImage(file);
                            }
                        }).show();
            }
        });
    }

    private void logoutUser() {
        if (account != null) {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            UserDataStore.getInstance().clearData(getApplicationContext());
                            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                        }
                    });
        } else {
            UserDataStore.getInstance().clearData(getApplicationContext());
            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
