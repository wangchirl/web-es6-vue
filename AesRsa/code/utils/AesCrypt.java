package utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.util.encoders.Hex;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2018/10/29.
 */
public class AesCrypt {
    private static final String KEY = "weitu_";
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
    public static String base64Encode(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }
    public static byte[] base64Decode(String base64Code) throws Exception{
        return new BASE64Decoder().decodeBuffer(base64Code);
    }
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));//初始化向量参数AES为16bytes，DES为8bytes

        return cipher.doFinal(content.getBytes("utf-8"));
    }
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes);
    }
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }


    public static String makeRandomPassword(int len){
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*.?".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();

        for (int x = 0; x < len; ++x) {
            sb.append(charr[r.nextInt(charr.length)]);
        }
        return sb.toString();
    }





    /**
     * 测试
     *
     */
    public static void main(String[] args) throws Exception {

        String content = "这是这个啊";  //0gqIDaFNAAmwvv3tKsFOFf9P9m/6MWlmtB8SspgxqpWKYnELb/lXkyXm7P4sMf3e
        System.out.println("加密前：" + content);

        String key = "wwqqeerrttyyuuii";
        System.out.println("加密密钥和解密密钥：" + key);

        String encrypt = aesEncrypt(content, key);
        System.out.println(encrypt.length()+":加密后：" + encrypt);

        String decrypt = aesDecrypt(encrypt, key);
        System.out.println("解密后：" + decrypt);
    }
}
