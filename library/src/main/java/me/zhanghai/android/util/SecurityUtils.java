/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.util;

import android.content.Context;
import android.provider.Settings;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtils {

    // NOTE:
    // See https://android.googlesource.com/platform/libcore/+/master/support/src/test/java/libcore/java/security/StandardNames.java
    // for supported algorithms of Android.

    private static final String ALGORITHM_NAME_PBKDF2_WITH_HMAC_SHA1 = "PBKDF2WithHmacSHA1";
    private static final String ALGORITHM_NAME_HMAC_SHA1 = "HmacSHA1";

    private static final String CHARSET_NAME = "UTF-8";

    private static final String OBFUSCATION_ALGORITHM = "PBEWithMD5AndDES";
    private static final String OBFUSCATION_PASSWORD = "this_is_insecure_enough";
    private static final int OBFUSCATION_ITERATION_COUNT = 32;


    public static byte[] random(int numberOfBytes) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[numberOfBytes];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    // Implement PBKDF2.
    // NOTE:
    // There is a bug in Android about this, see:
    // https://code.google.com/p/android/issues/detail?id=40578
    // One can choose to use SpongyCastle, see:
    // https://github.com/rtyley/spongycastle
    // But in this case we simply need to use the bug-free Ascii version.
    public static byte[] pbkdf2WithHmacSha1(String password, byte[] salt, int iterationCount,
                                            int keyLength) {

        SecretKeyFactory secretKeyFactory;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM_NAME_PBKDF2_WITH_HMAC_SHA1);
        } catch (NoSuchAlgorithmException e) {
            LogUtils.wtf("No such algorithm: " + ALGORITHM_NAME_PBKDF2_WITH_HMAC_SHA1);
            throw new RuntimeException(e);
        }
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
        SecretKey secretKey;
        try {
            secretKey = secretKeyFactory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            LogUtils.wtf("Invalid key specification: " + keySpec);
            throw new RuntimeException(e);
        }

        return secretKey.getEncoded();
    }

    public static byte[] pbkdf2WithHmacSha1(String password, String salt, int iterationCount,
                                            int keyLength) {
        try {
            return pbkdf2WithHmacSha1(password, salt.getBytes(CHARSET_NAME), iterationCount,
                    keyLength);
        } catch (UnsupportedEncodingException e) {
            LogUtils.wtf("Unsupported encoding: " + CHARSET_NAME);
            throw new RuntimeException(e);
        }
    }

    public static byte[] hmacSha1(byte[] key, byte[] data) {
        Mac mac;
        try {
            mac = Mac.getInstance(ALGORITHM_NAME_HMAC_SHA1);
        } catch (NoSuchAlgorithmException e) {
            LogUtils.wtf("No such algorithm: " + ALGORITHM_NAME_HMAC_SHA1);
            throw new RuntimeException(e);
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM_NAME_HMAC_SHA1);
        try {
            mac.init(secretKeySpec);
        } catch (InvalidKeyException e) {
            LogUtils.wtf("Invalid key: " + secretKeySpec);
            throw new RuntimeException(e);
        }
        return mac.doFinal(data);
    }

    public static byte[] hmacSha1(String key, String data) {
        try {
            return hmacSha1(key.getBytes(CHARSET_NAME), data.getBytes(CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            LogUtils.wtf("Unsupported encoding: " + CHARSET_NAME);
            throw new RuntimeException(e);
        }
    }

    // NOTICE: This function is null-intolerant.
    public static String obfuscate(String value, Context context) {
        try {
            return doObfuscation(value, Cipher.ENCRYPT_MODE, context);
        } catch (GeneralSecurityException e) {
            // Should never happen.
            throw new RuntimeException(e);
        }
    }

    public static String obfuscateIfNotNull(String value, Context context) {
        return value == null ? null : obfuscate(value, context);
    }

    // NOTICE: This function is null-intolerant.
    public static String deobfuscate(String value, Context context)
            throws GeneralSecurityException {
        return doObfuscation(value, Cipher.DECRYPT_MODE, context);
    }

    private static String doObfuscation(String value, int cipherMode, Context context)
            throws GeneralSecurityException {

        try {

            byte[] bytes;
            switch (cipherMode) {
                case Cipher.ENCRYPT_MODE:
                    bytes = value.getBytes(CHARSET_NAME);
                    break;
                case Cipher.DECRYPT_MODE:
                    bytes = IoUtils.base64ToByteArray(value);
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected cipherMode: " + cipherMode);
            }

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(OBFUSCATION_ALGORITHM);
            SecretKey key = keyFactory.generateSecret(
                    new PBEKeySpec(OBFUSCATION_PASSWORD.toCharArray()));

            Cipher cipher = Cipher.getInstance(OBFUSCATION_ALGORITHM);
            byte[] salt = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID).getBytes(CHARSET_NAME);
            cipher.init(cipherMode, key,
                    new PBEParameterSpec(salt, OBFUSCATION_ITERATION_COUNT));

            byte[] finalBytes = cipher.doFinal(bytes);
            switch (cipherMode) {
                case Cipher.ENCRYPT_MODE:
                    return IoUtils.byteArrayToBase64(finalBytes);
                case Cipher.DECRYPT_MODE:
                    return new String(finalBytes, CHARSET_NAME);
                default:
                    throw new IllegalArgumentException("Unexpected cipherMode: " + cipherMode);
            }

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | InvalidAlgorithmParameterException | InvalidKeyException
                | UnsupportedEncodingException e) {

            // Should never happen.
            throw new RuntimeException(e);
        }
    }
}
