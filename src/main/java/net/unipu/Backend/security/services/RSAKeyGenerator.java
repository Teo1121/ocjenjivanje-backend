package net.unipu.Backend.security.services;

import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Service
public class RSAKeyGenerator {

    private final PrivateKey privateKey;

    private final PublicKey publicKey;

    private String FormattedPublicKey;

    public RSAKeyGenerator() {
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();

        byte[] publicKeyBytes = publicKey.getEncoded();
        String publicKeyContent = Base64.getEncoder().encodeToString(publicKeyBytes);
        FormattedPublicKey = "-----BEGIN PUBLIC KEY-----" + System.lineSeparator();
        FormattedPublicKey += String.join(System.lineSeparator(),publicKeyContent.split("(?<=\\G.{64})"));
        FormattedPublicKey += System.lineSeparator()+"-----END PUBLIC KEY-----";
    }

    public String decode(String message) {
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] encryptedMessageBytes = Base64.getDecoder().decode(message);
            byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
            return new String(decryptedMessageBytes, StandardCharsets.UTF_8);
        }
        catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
               InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public String encode(String message) {
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] secretMessageBytes = message.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
            return Base64.getEncoder().encodeToString(encryptedMessageBytes);
        }
        catch (NoSuchPaddingException | InvalidKeyException | BadPaddingException | NoSuchAlgorithmException |
               IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }
    public String getFormattedPublicKey() {
        return FormattedPublicKey;
    }
}
