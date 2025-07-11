package cc.kertaskerja.manrisk_fraud.service.external;

import cc.kertaskerja.manrisk_fraud.helper.Crypto;
import org.springframework.stereotype.Service;

@Service
public class EncryptServiceImpl implements EncryptService {
    @Override
    public String encrypt(String plainText) {
        return Crypto.encrypt(plainText);
    }

    @Override
    public String decrypt(String encryptedText) {
        return Crypto.decrypt(encryptedText);
    }
}
