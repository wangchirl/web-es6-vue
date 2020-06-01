package utils;

import org.bouncycastle.util.encoders.Hex;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by WANGCHIRL on 2018/10/29.
 * AES+RSA加密解决前端数据明文问题
 */
public class AES_RSA {

    public static final String AES_KEY = "wwqqeerrttyyuuii";

    //公钥私钥固定
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjIp6TuqWY+xQXN9q/RCr81Eld" +
            "cvNkCB8z9e9kATao2/AP6iP13z3divfdRilzv600xYXSRIubdlYzIVQyUVRTMS/N" +
            "3ByAg18csPndLEslvjnHlhtSEo8hFdTcD8pA9HA+ZP3GoHKEPcqRdCedq/WlFL8o" +
            "PBgLIr5EgPHdiL34gwIDAQAB";

    public static final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKMinpO6pZj7FBc3" +
            "2r9EKvzUSV1y82QIHzP172QBNqjb8A/qI/XfPd2K991GKXO/rTTFhdJEi5t2VjMh" +
            "VDJRVFMxL83cHICDXxyw+d0sSyW+OceWG1ISjyEV1NwPykD0cD5k/cagcoQ9ypF0" +
            "J52r9aUUvyg8GAsivkSA8d2IvfiDAgMBAAECgYBh7tOB3gexmfEKqzY/oj2oQAy2" +
            "894XTCTnPZnz0CKvJjGsbCQgY18xU07PYGJcIVJqnrVicFHbByRiV3zDJD3ZKfXv" +
            "PPoLaKTBvs1GcZkUMrxZ7fKJbQ0tiNNYCu0ub6XWeyytCl+3DfI7fjXwh61IVr1J" +
            "QwD+6QDknYBUc16fGQJBANIQ/iCJnyeKchZEje5polkvtvy7xUa2FzYKku77Zok8" +
            "90FU6GJbAYvzoZTHkLJWRIgLEFL0TuMHIJQeo9jo7i8CQQDGzojrQRlxiV3H0FMR" +
            "jyaK84ihJwW3Wo3aZZK2cEynkb5uBOm6E0+Q3GMMQb5L4+T6N3XwjkXh3+A37y+J" +
            "vzntAkEAo1LG4nFdppaDgUCnwTesG/93HCS5ivLENSYfe2NHI2idLNRhISd0mmU/" +
            "ubc3jxB3k/ZWKMj+FJFUCRWRenIPzQJBAL4Ti775f90Yi+xV1oYsSpqBWzlQuNtk" +
            "HcoYYRxC5Fc2Nk60Lc81VVZYD+8REGM4oZ1F12JGdLQPuy1fZyEZDsUCQQCOae2A" +
            "KM6tYG0rs9R5fF0gx3PWbHhWqWVrpiu8llxOiD64zzzXU13OCkW+ebhugRITL+S7" +
            "frm1YoJfvRprl5Za";




    /**
     * 生成密钥对
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    //获取公钥--Base64编码
    public static String getPublicKey(KeyPair keyPair){
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return byte2Base64(bytes);
    }

    //获取私钥 --Base64编码
    public static String getPrivateKey(KeyPair keyPair){
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return byte2Base64(bytes);
    }

    /**
     * 将Base64编码的公钥转换成PublicKey对象
     * @param pubStr
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey string2PublicKey(String pubStr) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        byte[] keyBytes = base642Byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }


    /**
     * 将Base64编码的私钥转换为PrivateKey对象
     * @param priStr
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey string2PrivateKey(String priStr) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        byte[] keyBytes = base642Byte(priStr);
        PKCS8EncodedKeySpec keySpec = new  PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 公钥加密
     * @param content
     * @param publicKey
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] publicEncrypt(byte[] content,PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    /**
     * 私钥解密
     * @param content
     * @param privateKey
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] privateEncrypt(byte[] content,PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    /**
     * 字节数组转Base64编码
     * @param bytes
     * @return
     */
    public static String byte2Base64(byte[] bytes){
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    /**
     * Base64编码转字节数组
     * @param base64Key
     * @return
     * @throws IOException
     */
    public static byte[] base642Byte(String base64Key) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Key);
    }



    //**           AES    工具类                               ** //

    /**
     * 生成AES密钥，然后Base64编码
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String genKeyAES() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();
        String base64Str = byte2Base64(key.getEncoded());
        return base64Str;
    }

    /**
     * 将Base64编码后的AES密钥转换为SecretKey对象
     * @param base64Key
     * @return
     * @throws IOException
     */
    public static SecretKey loadKeyAES(String base64Key) throws IOException {
        byte[] bytes = base642Byte(base64Key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(bytes,"AES");
        return secretKeySpec;
    }

    /**
     * AES加密
     * @param source
     * @param secretKey
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] encryptAES(byte[] source,SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        return cipher.doFinal(source);
    }

    /**
     * 解密js加密后的值
     */
    public static String decodeJsValue(String jsValue) throws Exception {
        byte[] input = Hex.decode(jsValue);
        byte[] raw = decrypt(input,AES_KEY);

        // 标志位为0之后的是输入的有效字节
        int i = raw.length - 1;
        while (i > 0 && raw[i] != 0) {
            i--;
        }
        i++;
        byte[] data = new byte[raw.length - i];
        for (int j = i; j < raw.length; j++) {
            data[j - i] = raw[j];
        }

        return new String(data, "utf-8");
    }


    /**
     * AES解密
     * @param source
     * @param secretKey
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] decryptAES(byte[] source,SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,secretKey);
        return cipher.doFinal(source);
    }


    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {



    }


}
