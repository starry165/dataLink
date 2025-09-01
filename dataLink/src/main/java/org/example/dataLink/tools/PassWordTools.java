package org.example.dataLink.tools;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class PassWordTools {
    private static final String KEY="your-secret-key-here";
    private static final String SALT="aabbccddeeff00112233445566778899";
    private static final TextEncryptor encryptor= Encryptors.delux(KEY,SALT);

    public static String encrypt(String test){
        return encryptor.encrypt(test);
    }

    public static String decrypt(String test){
        return encryptor.decrypt(test);
    }
}
