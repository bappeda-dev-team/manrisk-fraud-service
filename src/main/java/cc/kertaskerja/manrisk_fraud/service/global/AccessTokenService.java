package cc.kertaskerja.manrisk_fraud.service.global;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccessTokenService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final RedisTokenService redisTokenService;

    public String getAccessToken() {
        String cachedToken = redisTokenService.getAccessToken();
        if (cachedToken != null && !cachedToken.isEmpty()) {
            return cachedToken;
        }

        String tokenUrl = "https://api-ekak.zeabur.app/user/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of(
                "username", "akun_test_level_3",
                "password", "KabKabMadiun2024"
        );

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> res = response.getBody();
            Map<String, Object> data = (Map<String, Object>) res.get("data");

            if (data != null && data.get("token") != null) {
                String accessToken = (String) data.get("token");
                redisTokenService.saveAccessToken(accessToken);
                return accessToken;
            } else {
                throw new RuntimeException("Token not found in response body");
            }
        } else {
            throw new RuntimeException("Failed to login: " + response.getStatusCode());
        }
    }
}
