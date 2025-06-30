package cc.kertaskerja.manrisk_fraud.service.global;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PegawaiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AccessTokenService accessToken;
    private final ObjectMapper objectMapper;

    @Value("${external.rekin.base-url}")
    private String baseUrl;

    public Map<String, Object> getPegawaiDetail(String nip) {
        String token = accessToken.getAccessToken();
        String url = String.format("%s/user/findpegawai/%s", baseUrl, nip);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch pegawai: " + response.getStatusCode());
        }
    }

    public Map<String, Object> getMappedPembuat(String nip) {
        Map<String, Object> pegawai = getPegawaiDetail(nip);
        JsonNode rkNode = objectMapper.convertValue(pegawai, JsonNode.class);
        JsonNode data = rkNode.get("data");

        Map<String, Object> result = new HashMap<>();
        result.put("nip", data.get("nip").asText());
        result.put("nama", data.get("nama_pegawai").asText());

        return result;
    }
}
