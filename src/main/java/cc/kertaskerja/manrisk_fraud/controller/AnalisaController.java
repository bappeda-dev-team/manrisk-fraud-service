package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaDTO;
import cc.kertaskerja.manrisk_fraud.service.analisa.AnalisaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analisa")
@RequiredArgsConstructor
@Tag(name = "Analisa", description = "API Analisa untuk Manajemen Risiko Fraud")
public class AnalisaController {

    private final AnalisaService analisaService;

    @GetMapping
    @Operation(summary = "Ambil semua data analisa")
    public ResponseEntity<ApiResponse<List<AnalisaDTO>>> getAllData() {
        List<AnalisaDTO> analisaList = analisaService.findAllAnalisa();
        ApiResponse<List<AnalisaDTO>> response = ApiResponse.success(analisaList,
                "Retrieved " + analisaList.size() + " data successfully");

        return ResponseEntity.ok(response);
    }
}
