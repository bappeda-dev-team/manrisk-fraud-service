package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.service.external.OPDService;
import cc.kertaskerja.manrisk_fraud.service.external.RencanaKinerjaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/external")
public class ExternalAPIController {

    private final OPDService opdService;
    private final RencanaKinerjaService rekinList;

    @GetMapping("/opdlist")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getOpdList() {
        List<Map<String, Object>> result = opdService.findAll();

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data Perangkat Daerah successfully"));
    }

    @GetMapping("/rencana-kinerja/{nip}/{tahun}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRekinList(@PathVariable String nip,
                                                                               @PathVariable String tahun) {
        List<Map<String, Object>> result = rekinList.findAll(nip, tahun);

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data Rencana Kinerja successfully"));
    }
}
