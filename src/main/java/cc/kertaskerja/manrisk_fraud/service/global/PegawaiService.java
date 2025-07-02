package cc.kertaskerja.manrisk_fraud.service.global;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PegawaiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AccessTokenService accessToken;

    @Value("${external.rekin.base-url}")
    private String baseUrl;
}
