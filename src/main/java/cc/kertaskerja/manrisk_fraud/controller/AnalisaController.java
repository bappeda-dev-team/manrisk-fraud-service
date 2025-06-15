package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaDTO;
import cc.kertaskerja.manrisk_fraud.service.analisa.AnalisaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analisa")
@RequiredArgsConstructor
@Tag(name = "Analisa", description = "API Analisa untuk Manajemen Risiko Fraud")
public class AnalisaController {

    private final AnalisaService analisaService;

    @GetMapping("/getAllData/{nip}/{tahun}")
    @Operation(summary = "Ambil semua data analisa")
    public ResponseEntity<ApiResponse<List<AnalisaDTO>>> getAllData(@PathVariable String nip,
                                                                    @PathVariable String tahun) {
        List<AnalisaDTO> analisaList = analisaService.findAllAnalisa(nip, tahun);
        ApiResponse<List<AnalisaDTO>> response = ApiResponse.success(analisaList,
                "Retrieved " + analisaList.size() + " data successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getOne/{idManrisk}")
    @Operation(summary = "Ambil satu data analisa berdasarkan ID Manrisk")
    public ResponseEntity<ApiResponse<AnalisaDTO>> getByIdManrisk(@PathVariable String idManrisk) {
        AnalisaDTO dto = analisaService.findOneAnalisa(idManrisk);
        ApiResponse<AnalisaDTO> response = ApiResponse.success(dto, "Retrieved 1 data successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Simpan data analisa baru")
    public ResponseEntity<ApiResponse<AnalisaDTO>> saveAnalisa(@RequestBody AnalisaDTO dto) {
        AnalisaDTO saved = analisaService.saveAnalisa(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved, "Berhasil menyimpan data analisa"));
    }
}
