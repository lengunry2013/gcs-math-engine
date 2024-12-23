package com.gcs.game.utils;


import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
@Slf4j
public class Security {

    public static final String MESSAGE_ENCODE_KEY_FOR_DB = "27AL0NY5";

    /**
     * @param src
     * @param key
     * @return
     * @throws Exception
     * @author DongYu 2011-6-1
     */
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        IvParameterSpec iv = new IvParameterSpec(key);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
        return cipher.doFinal(src);
    }

    /**
     * @param data
     * @param desKey
     * @return
     * @author DongYu 2011-6-1
     */
    public final static String decrypt(String data, String desKey) {
        try {
            byte[] buffer = new byte[data.length() / 2];
            for (int i = 0; i < (data.length() / 2); i++) {
                int num2 = Integer.parseInt(data.substring(i * 2, i * 2 + 2),
                        0x10);
                buffer[i] = (byte) num2;
            }
            return new String(decrypt(buffer, desKey.getBytes()));

        } catch (Exception e) {
            log.error("decrypt error", e);
        }
        return null;
    }

    /**
     * @param src
     * @param key
     * @return
     * @throws Exception
     * @author DongYu 2011-6-1
     */
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        IvParameterSpec iv = new IvParameterSpec(key);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
        return cipher.doFinal(src);
    }

    /**
     * @param str
     * @param key
     * @return
     * @author DongYu 2011-6-1
     */
    public final static String encrypt(String str, String key) {
        try {
            return byte2hex(encrypt(str.getBytes(), key.getBytes()));
        } catch (Exception e) {
            log.error("encrypt error", e);
        }
        return null;
    }

    /**
     * @param b
     * @return
     * @author DongYu 2011-6-1
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("Incorrect Length");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * @param b
     * @return
     * @author DongYu 2011-6-1
     */
    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer("");
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

}
