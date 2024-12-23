package com.gcs.game.utils;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Date;

public class TOTPUtil {

    private static final long STEP = 30000;
    private static final int CODE_DIGITS = 6;
    private static final long INITIAL_TIME = 0;
    private static final long FLEXIBILITY_TIME = 30000;
    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    public static String generateMyTOTP(String apiKey) {
        if (apiKey.isEmpty()) {
            return "";
        }
        long now = new Date().getTime();
        String time = Long.toHexString(timeFactor(now)).toUpperCase();
        return generateTOTP(apiKey, time);
    }

    public static boolean verifyTOTPRigidity(String apiKey, String secret, String totp) {
        if (generateMyTOTP(apiKey + secret).isEmpty()) {
            return false;
        } else {
            return generateMyTOTP(apiKey + secret).equals(totp);
        }
    }

    public static boolean verifyTOTPFlexibility(String apiKey, String secret, String totp) {
        long now = new Date().getTime();
        String time = Long.toHexString(timeFactor(now)).toUpperCase();
        String tempTotp = generateTOTP(secret, time);
        if (tempTotp.equals(totp)) {
            return true;
        }
        String time2 = Long.toHexString(timeFactor(now - FLEXIBILITY_TIME)).toUpperCase();
        String tempTotp2 = generateTOTP(secret, time2);
        return tempTotp2.equals(totp);
    }

    private static long timeFactor(long targetTime) {
        return (targetTime - INITIAL_TIME) / STEP;
    }

    private static byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "AES");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    private static byte[] base32Str2Bytes(String base32str) {
        Base32 base32 = new Base32();
        return base32.decode(base32str);
    }

    private static byte[] hexStr2Bytes(String hex) {
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
        byte[] ret = new byte[bArray.length - 1];
        System.arraycopy(bArray, 1, ret, 0, ret.length);
        return ret;
    }

    private static String generateTOTP(String key, String time) {
        return generateTOTP(key, time, "HmacSHA1");
    }


    private static String generateTOTP256(String key, String time) {
        return generateTOTP(key, time, "HmacSHA256");
    }

    private static String generateTOTP512(String key, String time) {
        return generateTOTP(key, time, "HmacSHA512");
    }

    private static String generateTOTP(String key, String time, String crypto) {
        StringBuilder timeBuilder = new StringBuilder(time);
        while (timeBuilder.length() < 16) timeBuilder.insert(0, "0");
        time = timeBuilder.toString();

        byte[] msg = hexStr2Bytes(time);
        byte[] k = base32Str2Bytes(key);
        byte[] hash = hmac_sha(crypto, k, msg);
        return truncate(hash);
    }

    private static String truncate(byte[] target) {
        StringBuilder result;
        int offset = target[target.length - 1] & 0xf;
        int binary = ((target[offset] & 0x7f) << 24)
                | ((target[offset + 1] & 0xff) << 16)
                | ((target[offset + 2] & 0xff) << 8) | (target[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[CODE_DIGITS];
        result = new StringBuilder(Integer.toString(otp));
        while (result.length() < CODE_DIGITS) {
            result.insert(0, "0");
        }
        return result.toString();
    }

}
