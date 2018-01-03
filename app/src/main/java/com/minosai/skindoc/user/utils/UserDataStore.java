package com.minosai.skindoc.user.utils;

import java.lang.reflect.Type;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minosai.skindoc.auth.data.SignupCredentials;
import com.minosai.skindoc.user.data.User;

/**
 * Created by minos.ai on 30/12/17.
 */

public class UserDataStore {
    private static UserDataStore ourInstance = null;

    public static final String PREF_USER = "com.minosai.skindoc.user_preferences";
    public static final String PREF_USER_TOKEN = "com.minosai.skindoc.token-pref";
    public static final String SECRET = "b'k5AfGCquSoeAyGA6+jKWBISJ5Gu7T0TomabJADn+EcU='";
    public static final String PREF_USER_DETAILS = "com.minosai.skindoc.user-details";
    public static final String PREF_GOOGLE_TOKEN = "com.minosai.skindoc.google-token";

    private User user = null;
    private String token;
    private String googleToken;

    public SignupCredentials signupCredentials = new SignupCredentials();

    public String getGoogleToken(Context context) {
        preferences = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
        googleToken = preferences.getString(PREF_GOOGLE_TOKEN, null);
        return googleToken;
    }

    public void setGoogleToken(Context context, String googleToken) {
        this.googleToken = googleToken;
        preferences = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(PREF_GOOGLE_TOKEN, googleToken);
        editor.commit();
    }

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    Gson gson = new Gson();
    Type type = new TypeToken<User>() {}.getType();

    public static UserDataStore getInstance() {
        if(ourInstance == null){
            ourInstance = new UserDataStore();
        }
        return ourInstance;
    }

    private UserDataStore() {
    }

    public void saveToken(Context context, String token) {
        preferences = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(UserDataStore.PREF_USER_TOKEN, token);
        editor.commit();
        String decodedJson = null;
        try {
            decodedJson = JWTUtils.decoded(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setUser(context, decodedJson);
        this.token = token;
    }

    public String getToken(Context context) {
        if(token == null) {
            preferences = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
            token = preferences.getString(UserDataStore.PREF_USER_TOKEN, null);
        }
        return token;
    }

    public void saveUser(Context context) {
        String json = gson.toJson(user, type);
        preferences = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(PREF_USER_DETAILS, json);
        editor.commit();
    }

    public User getUser(Context context) {
        if(user == null) {
            try {
                preferences = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
                String json = preferences.getString(PREF_USER_DETAILS, null);
                user = gson.fromJson(json, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public void setUser(Context context, String decodedJson) {
        user = gson.fromJson(decodedJson, type);
        saveUser(context);
    }

    public void clearData(Context context) {
        preferences = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
