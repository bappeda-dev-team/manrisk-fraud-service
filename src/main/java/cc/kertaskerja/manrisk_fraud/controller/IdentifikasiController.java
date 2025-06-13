package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiDTO;
import cc.kertaskerja.manrisk_fraud.service.identifikasi.IdentifikasiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        List<IdentifikasiDTO> identifikasiList = identifikasiService.findAllIdentifikasi(nip, tahun);
        ApiResponse<List<IdentifikasiDTO>> response = ApiResponse.success(identifikasiList,
                "Retrieved " + identifikasiList.size() + " data successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getOne/{idManrisk}")
    @Operation(summary = "Ambil satu data identifikasi berdasarkan ID Manrisk")
    public ResponseEntity<ApiResponse<IdentifikasiDTO>> getByIdManrisk(@PathVariable String idManrisk) {
        IdentifikasiDTO dto = identifikasiService.findOneIdentifikasi(idManrisk);
        ApiResponse<IdentifikasiDTO> response = ApiResponse.success(dto, "Retrieved 1 data successfully");

        return ResponseEntity.ok(response);
    }


    @PostMapping
    @Operation(summary = "Tambah data identifikasi")
    public ResponseEntity<ApiResponse<IdentifikasiDTO>> create(@RequestBody IdentifikasiDTO dto) {
        try {
            IdentifikasiDTO created = identifikasiService.saveDataIdentifikasi(dto);
            return new ResponseEntity<>(ApiResponse.created(created), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    ApiResponse.error(400, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
