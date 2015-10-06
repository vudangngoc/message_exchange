package com.creative.crypto;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.io.IOException;
/**
 * http://stackoverflow.com/questions/5126616/xor-operation-with-two-strings-in-java/7983531#7983531
 *
 */
public class StringXORer implements EncryptString {

    /* (non-Javadoc)
	 * @see com.creative.crypto.EncryptString#encode(java.lang.String, java.lang.String)
	 */
    @Override
	public String encode(String s, String key) {
        return base64Encode(xorWithKey(s.getBytes(), key.getBytes()));
    }

    /* (non-Javadoc)
	 * @see com.creative.crypto.EncryptString#decode(java.lang.String, java.lang.String)
	 */
    @Override
	public String decode(String s, String key) {
        return new String(xorWithKey(base64Decode(s), key.getBytes()));
    }

    private byte[] xorWithKey(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i%key.length]);
        }
        return out;
    }

    private byte[] base64Decode(String s) {
        try {
            BASE64Decoder d = new BASE64Decoder();
            return d.decodeBuffer(s);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private String base64Encode(byte[] bytes) {
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encode(bytes).replaceAll("\\s", "");

    }
}