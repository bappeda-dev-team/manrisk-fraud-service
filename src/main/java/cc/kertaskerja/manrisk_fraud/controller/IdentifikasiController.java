package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiDTO;
import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiReqDTO;
import cc.kertaskerja.manrisk_fraud.service.identifikasi.IdentifikasiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/identifikasi")
@RequiredArgsConstructor
@Tag(name = "Identifikasi", description = "API Identifikasi untuk Manajemen Risiko Fraud")
public class IdentifikasiController {

    private final IdentifikasiService identifikasiService;

    @GetMapping("/getAllData/{nip}/{tahun}")
    @Operation(summary = "Ambil semua data identifikasi")
    public ResponseEntity<ApiResponse<List<IdentifikasiDTO>>> getAllData(@PathVariable String nip,
                                                                         @PathVariable String tahun) {
        List<IdentifikasiDTO> result = identifikasiService.findAllIdentifikasi(nip, tahun);
        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data successfully"));
    }

    @GetMapping("/getOne/{idManrisk}")
    @Operation(summary = "Ambil satu data identifikasi berdasarkan ID Manrisk")
    public ResponseEntity<ApiResponse<IdentifikasiDTO>> getByIdManrisk(@PathVariable String idManrisk) {
        IdentifikasiDTO dto = identifikasiService.findOneIdentifikasi(idManrisk);
        ApiResponse<IdentifikasiDTO> response = ApiResponse.success(dto, "Retrieved 1 data successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Simpan data identifikasi baru")
    public ResponseEntity<ApiResponse<IdentifikasiDTO>> saveIdentifikasi(@Valid @RequestBody IdentifikasiDTO req) {
        IdentifikasiDTO result = identifikasiService.saveIdentifikasi(req);
        ApiResponse<IdentifikasiDTO> response = ApiResponse.success(result, "Data berhasil disimpan");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
