package com.louzx.swipe.core.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionUtils {

    public static String md5 (String plaintext) {
        return DigestUtils.md5DigestAsHex(plaintext.getBytes(StandardCharsets.UTF_8));
    }

    public static String base64 (String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String unBase64 (String text) {
        return new String(Base64.getDecoder().decode(text.getBytes(StandardCharsets.UTF_8)));
    }
}
