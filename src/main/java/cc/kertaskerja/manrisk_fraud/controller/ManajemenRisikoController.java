package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.ManajemenRisikoDTO;
import cc.kertaskerja.manrisk_fraud.service.manajemenRisiko.ManajemenRisikoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manajemen-risiko")
@RequiredArgsConstructor
@Tag(name = "Manajemen Risiko")
public class ManajemenRisikoController {

    private final ManajemenRisikoService manajemenRisikoService;

    @GetMapping("/{nip}/{tahun}")
    @Operation(summary = "Tampilkan semua data Manajemen Risiko")
    public ResponseEntity<ApiResponse<List<ManajemenRisikoDTO>>> getAllData(@PathVariable String nip,
                                                                            @PathVariable String tahun) {
        List<ManajemenRisikoDTO> result = manajemenRisikoService.showAllData(nip, tahun);

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data successfully"));
    }
}
