package com.honeywell.sndemo.encrypt;

import android.util.Log;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PinEncryption {
    static final String TAG = "PinEncryption";

    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String FILE_PATH = "/license/encryption_key";
    private static final String SECRET_KEY = "fiKxTbc65YZM6mQ1gBOTTg==";

    public static void encryPin(String pin) {
        try {
            Key secretKey = generateSecretKey();
            byte[] encryptedPin = encryptPIN(pin, secretKey);
            writeToFile(encryptedPin);
            Log.d(TAG, "Encrypted PIN has been written to the file: encryption_key");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String decryPin() {
        if(new File(FILE_PATH).exists()) {
            try {
                Key secretKey = generateSecretKey();
                String encrypinstr = readFromFile(FILE_PATH);
                byte[] encryptedBytes = Base64.getDecoder().decode(encrypinstr);
                String decryptString = decryptPIN(encryptedBytes, secretKey);
                Log.d(TAG, "PIN: " + decryptString);

                return decryptString;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static Key generateSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(decodedKey, ENCRYPTION_ALGORITHM);
    }

    private static byte[] encryptPIN(String pin, Key secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(pin.getBytes());
    }

    private static String decryptPIN(byte[] encryptedPIN, Key secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedPIN);
        return new String(decryptedBytes);
    }

    private static void writeToFile(byte[] encryptedPin) throws IOException {
        FileOutputStream fos = new FileOutputStream(FILE_PATH);
        fos.write(encryptedPin);
        fos.close();
    }

    private static String readFromFile(String path) throws IOException {
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStream.read(bytes);
        fileInputStream.close();

        return Base64.getEncoder().encodeToString(bytes);
    }
}

