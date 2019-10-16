package com.khoslalabs.trialApp;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

import javax.crypto.Cipher;


public class DecryptionUtil {

    private static final String ASYMMETRIC_ALGO = "RSA/ECB/PKCS1Padding";

    public static byte[] decryptUsingPrivateKey(PrivateKey privateKey, byte[] data) throws IOException, GeneralSecurityException {
        Cipher pkCipher = Cipher.getInstance(ASYMMETRIC_ALGO);
        pkCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return pkCipher.doFinal(data);
    }

    public static byte[] decryptUsingSymmetricKey(byte[] symmetricKey, byte[] data) throws InvalidCipherTextException {
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new AESEngine(), new PKCS7Padding());

        cipher.init(false, new KeyParameter(symmetricKey));

        int outputSize = cipher.getOutputSize(data.length);

        byte[] tempOP = new byte[outputSize];
        int processLen = cipher.processBytes(data, 0, data.length, tempOP, 0);
        int outputLen = cipher.doFinal(tempOP, processLen);

        byte[] result = new byte[processLen + outputLen];
        System.arraycopy(tempOP, 0, result, 0, result.length);
        return result;

    }
}
