package cc.kertaskerja.manrisk_fraud.helper;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Crypto {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "mySuperSecretKey"; // Must be 16 characters for AES-128

    public static String encrypt(String plainText) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted); // 🔁 use URL-safe encoding
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting: " + e.getMessage());
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] original = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedText)); // 🔁 match with UrlDecoder
            return new String(original);
        } catch (Exception e) {
            return encryptedText;
        }
    }

    /**
     * Check whether the input is a valid AES-encrypted string.
     * If decryption works and the result looks like expected format (e.g., NIP),
     * then assume it's encrypted.
     */
    public static boolean isEncrypted(String input) {
        try {
            String decrypted = decrypt(input);

            // ✅ Customize this condition to match your expected plaintext pattern (e.g., 18-digit NIP)
            if (decrypted.matches("\\d{18}")) {
                return true;
            }

            // Otherwise it's likely not encrypted (e.g., random decrypted junk)
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
