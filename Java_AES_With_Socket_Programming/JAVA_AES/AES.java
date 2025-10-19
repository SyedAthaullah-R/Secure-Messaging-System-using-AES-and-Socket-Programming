package JAVA_AES;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    Cipher cipher;
    public String keyToString(SecretKey secKey){
        return Base64.getEncoder().encodeToString(secKey.getEncoded());
    }
    public SecretKey keyStringToKey(String keyString){
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
    public SecretKey generate_Secret_Key() throws Exception{
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secKey = keyGen.generateKey();
        return secKey;
    }
    public String encode(String plaintext, SecretKey secKey) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] encryptedByte = cipher.doFinal(plaintext.getBytes());
        String encryptedString = Base64.getEncoder().encodeToString(encryptedByte);
        return encryptedString;
    }
    public String decrypt(String encyptedString, SecretKey secKey) throws Exception{
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] decryptedByte = cipher.doFinal(Base64.getDecoder().decode(encyptedString));
        String decyptedString = new String(decryptedByte);
        return decyptedString;
    }
}
