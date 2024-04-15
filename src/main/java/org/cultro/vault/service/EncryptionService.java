package org.cultro.vault.service;

import org.cultro.vault.util.AESUtil;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class EncryptionService {

    public SecretKey generateKey() throws Exception {
        return AESUtil.generateKey();
    }

    public byte[] generateIV() {
        return AESUtil.generateIV();
    }

    public String encrypt(String plaintext, SecretKey key, byte[] iv) throws Exception {
        return AESUtil.encrypt(plaintext, key, iv);
    }

    public String decrypt(String ciphertext, SecretKey key, byte[] iv) throws Exception {
        return AESUtil.decrypt(ciphertext, key, iv);
    }

    public String encodeKey(SecretKey key) {
        return AESUtil.encodeKey(key);
    }

    public SecretKey decodeKey(String encodedKey) {
        return AESUtil.decodeKey(encodedKey);
    }
}
