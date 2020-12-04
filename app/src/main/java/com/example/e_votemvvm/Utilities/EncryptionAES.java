package com.example.e_votemvvm.Utilities;

import android.util.Log;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionAES {

    private String TAG = "ENCRYPT-DECRYPT";
    private static final String characterEncoding       = "UTF-8";
    private static final String cipherTransformation    = "AES/CBC/PKCS5PADDING";
    private static final String aesEncryptionAlgorithm = "AES";



    public String encryptAes(String plainText,String encryptionKey) {

        String encryptedText = "";

        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            byte[] key      = encryptionKey.getBytes(characterEncoding);

            SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithm);

            IvParameterSpec ivparameterspec = new IvParameterSpec(key);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF8"));
            Base64.Encoder encoder = Base64.getEncoder();
            encryptedText = encoder.encodeToString(cipherText);

        } catch (Exception E) {
            Log.d( TAG,"Encrypt Exception : "+ E.getMessage());
        }
        return encryptedText;
    }


    public String decryptAes(String encryptedText, String encryptionKey) {
        String decryptedText = "";
        try {

            Cipher cipher = Cipher.getInstance(cipherTransformation);
            byte[] key = encryptionKey.getBytes(characterEncoding);
            SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithm);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] cipherText = decoder.decode(encryptedText.getBytes("UTF8"));
            decryptedText = new String(cipher.doFinal(cipherText), "UTF-8");

        } catch (Exception E) {
            Log.d(TAG,"decrypt Exception : "+E.getMessage());
        }

        return decryptedText;
    }


}
