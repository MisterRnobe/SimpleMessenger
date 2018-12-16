package server.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
@NoArgsConstructor
public class HashUtils {
    public String hash256(String data) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes());
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
