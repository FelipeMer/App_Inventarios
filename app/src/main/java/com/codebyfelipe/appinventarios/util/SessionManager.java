package com.codebyfelipe.appinventarios.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class SessionManager { //función que guarda y lee el JWT de fomr acifrada

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            prefs = EncryptedSharedPreferences.create(
                    context,
                    Constants.PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error al crear SharedPreferences cifradas", e);
        }
    }

    public void saveToken(String token) {
        prefs.edit().putString(Constants.KEY_JWT_TOKEN, token).apply();
    }

    public String getToken() {
        return prefs.getString(Constants.KEY_JWT_TOKEN, null);
    }

    public void saveUser(String userId, String userName) {
        prefs.edit()
                .putString(Constants.KEY_USER_ID, userId)
                .putString(Constants.KEY_USER_NAME, userName)
                .apply();
    }

    public String getUserId() {
        return prefs.getString(Constants.KEY_USER_ID, null);
    }

    public String getUserName() {
        return prefs.getString(Constants.KEY_USER_NAME, null);
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}