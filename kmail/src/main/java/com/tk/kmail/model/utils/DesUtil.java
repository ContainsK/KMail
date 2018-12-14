package com.tk.kmail.model.utils;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class DesUtil {

    public static class MD5 {
        private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
                'E', 'F'};

        public static String toHexString(byte[] b) { // String to byte
            StringBuilder sb = new StringBuilder(b.length * 2);
            for (int i = 0; i < b.length; i++) {
                sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
                sb.append(HEX_DIGITS[b[i] & 0x0f]);
            }
            return sb.toString();
        }

        public static String toMd5(String s) {
            try {
                // Create MD5 Hash
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(s.getBytes());
                byte messageDigest[] = digest.digest();
                return toHexString(messageDigest);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    private static final String DES = "DES";
    private static final String KEY = "CCR!@#$%";

    public static String encrypt(String data) {
        try {
            byte[] bt = encrypt(data.getBytes(), KEY.getBytes());
            byte[] strs = Base64.encode(bt, Base64.DEFAULT);
            return new String(strs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String decrypt(String data) {
        if (data == null) {
            return null;
        }
        byte[] buf = Base64.decode(data.getBytes(), Base64.DEFAULT);
        try {
            byte[] bt = decrypt(buf, KEY.getBytes());
            return new String(bt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();

        DESKeySpec dks = new DESKeySpec(key);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(1, securekey, sr);
        return cipher.doFinal(data);
    }

    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();

        DESKeySpec dks = new DESKeySpec(key);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(2, securekey, sr);
        return cipher.doFinal(data);
    }
}
