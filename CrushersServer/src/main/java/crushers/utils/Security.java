package crushers.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {

    public Security(){
    }

    //https://www.javatpoint.com/how-to-encrypt-password-in-java <<------ Inspiration from this
    public String passwordEncryption(String password, String hashType) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(hashType);
        messageDigest.update(password.getBytes());
        byte[] bytes = messageDigest.digest();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < bytes.length; i++){
            //not correct hashing since it requires: (bytes[i] & 0xff) + 0x10, 16).substring(1) -> correct hashing from hexadecimal
            builder.append(Integer.toString(bytes[i]));
        }
        return builder.toString();
    }


}
