package com.louzx.swipe.core.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class EncryptionUtils {

    public static String md5 (String plaintext) {
        return DigestUtils.md5DigestAsHex(plaintext.getBytes(StandardCharsets.UTF_8));
    }

}
