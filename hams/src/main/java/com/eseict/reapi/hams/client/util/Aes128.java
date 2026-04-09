package com.eseict.reapi.hams.client.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

/**
 * AES/ECB 암호화 유틸.
 * 레거시 Aes128.kt 이식 (Java 21).
 */
@Slf4j
public class Aes128 {

    private final Key keySpec;

    public Aes128(String key) {
        try {
            byte[] keyBytes = new byte[16];
            byte[] b = key.getBytes("UTF-8");
            System.arraycopy(b, 0, keyBytes, 0, Math.min(b.length, keyBytes.length));
            this.keySpec = new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new IllegalArgumentException("AES key init failed", e);
        }
    }

    public String encrypt(String str) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("AES encrypt failed: {}", e.getMessage());
            return null;
        }
    }

    public String decrypt(String str) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] byteStr = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
            return new String(cipher.doFinal(byteStr), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AES decrypt failed: {}", e.getMessage());
            return null;
        }
    }
}
