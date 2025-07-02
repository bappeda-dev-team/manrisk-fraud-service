package cc.kertaskerja.manrisk_fraud.service.external;

import cc.kertaskerja.manrisk_fraud.service.global.AccessTokenService;
import cc.kertaskerja.manrisk_fraud.utils.HttpClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RencanaKinerjaServiceImpl implements RencanaKinerjaService {

    private final AccessTokenService accessTokenService;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${external.rekin.base-url}")
    private String rekinBaseUrl;


    @Override
    public List<Map<String, Object>> findAll(String nip, String tahun) {
        String token = accessTokenService.getAccessToken();
        String url = String.format("%s/get_rencana_kinerja/pegawai/%s?tahun=%s", rekinBaseUrl, nip, tahun);

        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        Map<String, Object> response = httpClient.get(url, headers, Map.class);

        try {
            Object dataRaw = response.get("rencana_kinerja");
            String jsonData = objectMapper.writeValueAsString(dataRaw);

            return objectMapper.readValue(jsonData, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }
}
