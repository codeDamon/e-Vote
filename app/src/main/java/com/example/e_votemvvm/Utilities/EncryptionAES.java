package com.example.e_votemvvm.Utilities;

import android.util.Log;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class EncryptionAES {

    String TAG = "ENCRYPT";

    public String encrypt(String plainText, String key){
        try {
            String cypherText = AESCrypt.encrypt(key, plainText);
            return cypherText;

        }catch (GeneralSecurityException e){
            //handle error
            Log.d(TAG,"Error:"+e);
            return null;
        }
    }

    public String decrypt(String cypherText, String key){

        try {
            String plainText = AESCrypt.decrypt(cypherText, key);
            return plainText;
        }catch (GeneralSecurityException e){
            //handle error - could be due to incorrect password or tampered encryptedMsg
            Log.d(TAG,"Error:"+e);
            return "error";
        }
    }


}
