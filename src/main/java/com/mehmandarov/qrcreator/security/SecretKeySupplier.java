package com.mehmandarov.qrcreator.security;

import org.apache.commons.codec.binary.Hex;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@ApplicationScoped
public class SecretKeySupplier {

    @Inject
    @ConfigProperty(name = "hash_salt.value", defaultValue = "heiverden1!")
    String salt;

    public String generateVerificationKey(String str) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 10000;
        int keyLength = 512;

        char[] strChars = str.toCharArray();
        byte[] saltBytes = salt.getBytes();

        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec spec = new PBEKeySpec(strChars, saltBytes, iterations, keyLength);
        SecretKey key = skf.generateSecret( spec );
        byte[] hashedBytes = key.getEncoded( );

        return Hex.encodeHexString(hashedBytes);
    }
}
