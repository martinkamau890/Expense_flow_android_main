package com.example.expenseflow;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenManager {

    private final String documentName = "Api Token";
    private final String tokenValue = "JWT_Token";

    private SharedPreferences sharedPreferences;



    public TokenManager(Context context){
        try {
            initialize(context);
        } catch (GeneralSecurityException | IOException e) {
            context.deleteSharedPreferences(documentName);
            try {
                initialize(context);
            } catch (GeneralSecurityException | IOException ex) {
                throw new RuntimeException("Unable to initialize encrypted preferences", ex);
            }
        }


    }

    public void initialize (Context context)
            throws GeneralSecurityException, IOException{

        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        sharedPreferences = EncryptedSharedPreferences.create(
                context,
                documentName,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
    }

    public void saveToken(String token){
        sharedPreferences.edit().putString(tokenValue, token).apply();
    }

    public String getToken(){
        return sharedPreferences.getString(tokenValue, null);
    }

    public void removeToken(){
        sharedPreferences.edit().remove(tokenValue).apply();
    }
}
