package cc.kertaskerja.manrisk_fraud.service.global;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RencanaKinerjaService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AccessTokenService accessToken;

    @Value("${external.rekin.base-url}")
    private String rekinBaseUrl;

    public Map<String, Object> getRencanaKinerja(String nip, String tahun) {
        String token = accessToken.getAccessToken();
        String url = String.format("%s/get_rencana_kinerja/pegawai/%s?tahun=%s", rekinBaseUrl, nip, tahun);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        Map<String, String> uriVariables = Map.of(
                "nip", nip,
                "tahun", tahun
        );

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class,
                uriVariables
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch rencana kinerja: " + response.getStatusCode());
        }
    }

    public Map<String, Object> getDetailRencanaKinerja(String idManrisk) {
        String token = accessToken.getAccessToken();
        String url = String.format("%s/detail-rencana_kinerja/%s", rekinBaseUrl, idManrisk);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        Map<String, String> uriVariables = Map.of(
                "idManrisk", idManrisk
        );

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class,
                uriVariables
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch detail rencana kinerja: " + response.getStatusCode());
        }
    }
}