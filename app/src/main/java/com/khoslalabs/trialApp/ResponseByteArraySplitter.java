package com.khoslalabs.trialApp;

/**
 * @author Deepanshu
 */
class ResponseByteArraySplitter {

    private static final int SYMMETRIC_KEY_SIZE = 256;
    private final byte[] encryptedSymmetricKey;
    private final byte[] encryptedData;

    ResponseByteArraySplitter(byte[] data) throws Exception {

        int offset 				= 0;
        encryptedSymmetricKey 	= new byte[SYMMETRIC_KEY_SIZE];

        copyByteArray(
                data,
                offset,
                encryptedSymmetricKey.length,
                encryptedSymmetricKey
        );

        offset 			= offset + SYMMETRIC_KEY_SIZE;
        encryptedData 	= new byte[data.length - offset];
        copyByteArray(data, offset, encryptedData.length, encryptedData);
    }

    byte[] getEncryptedSymmetricKey() {
        return encryptedSymmetricKey;
    }

    byte[] getEncryptedData() {
        return encryptedData;
    }

    private void copyByteArray(byte[] src,
                               int offset,
                               int length,
                               byte[] dest) throws Exception {
        try {
            System.arraycopy(src, offset, dest, 0, length);
        } catch (Exception e) {
            throw new Exception("Decryption failed, Corrupted packet!", e);
        }
    }
}
