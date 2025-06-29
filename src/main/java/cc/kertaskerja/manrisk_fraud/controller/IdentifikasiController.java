package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiResDTO;
import cc.kertaskerja.manrisk_fraud.service.identifikasi.IdentifikasiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/identifikasi")
@RequiredArgsConstructor
@Tag(name = "Identifikasi", description = "API Identifikasi untuk Manajemen Risiko Fraud")
public class IdentifikasiController {

    private final IdentifikasiService identifikasiService;

    @GetMapping("/get-all-data/{nip}/{tahun}")
    @Operation(summary = "Ambil semua data identifikasi berdasarkan NIP dan Tahun")
    public ResponseEntity<ApiResponse<List<IdentifikasiResDTO>>> getAllData(@PathVariable String nip,
                                                                            @PathVariable String tahun) {
        List<IdentifikasiResDTO> result = identifikasiService.findAllIdentifikasi(nip, tahun);

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data successfully"));
    }

    @GetMapping("/get-detail/{idRekin}")
    @Operation(summary = "Ambil satu data identifikasi berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<IdentifikasiResDTO>> getByIdRekin(@PathVariable String idRekin) {
        IdentifikasiResDTO dto = identifikasiService.findOneIdentifikasi(idRekin);
        ApiResponse<IdentifikasiResDTO> response = ApiResponse.success(dto, "Retrieved 1 data successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Simpan data identifikasi baru")
    public ResponseEntity<ApiResponse<?>> saveIdentifikasi(@Valid @RequestBody IdentifikasiReqDTO reqDTO,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();

            ApiResponse<List<String>> errorResponse = ApiResponse.<List<String>>builder()
                    .success(false)
                    .statusCode(400)
                    .message("Validation failed")
                    .errors(errorMessages)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        }

        IdentifikasiResDTO result = identifikasiService.saveIdentifikasi(reqDTO);
        return ResponseEntity.ok(ApiResponse.success(result, "Saved successfully"));
    }

    @PutMapping("/{idRekin}")
    @Operation(summary = "Update data identifikasi berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<?>> updateIdentifikasi(@PathVariable String idRekin,
                                                             @Valid @RequestBody IdentifikasiReqDTO reqDTO,
                                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();

            ApiResponse<List<String>> errorResponse = ApiResponse.<List<String>>builder()
                    .success(false)
                    .statusCode(400)
                    .message("Validation failed")
                    .errors(errorMessages)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        }

        IdentifikasiResDTO result = identifikasiService.updateIdentifikasi(idRekin, reqDTO);

        return ResponseEntity.ok(ApiResponse.success(result, "Updated successfully"));
    }

    @PatchMapping("/{idRekin}")
    @Operation(summary = "Verifikasi Identifikasi berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<?>> verifyIdentifikasi(@PathVariable String idRekin,
                                                             @Valid @RequestBody IdentifikasiReqDTO.UpdateStatusDTO reqDTO,
                                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();

            ApiResponse<List<String>> errorResponse = ApiResponse.<List<String>>builder()
                    .success(false)
                    .statusCode(400)
                    .message("Validation failed")
                    .errors(errorMessages)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        }

        IdentifikasiResDTO updated = identifikasiService.verifyIdentifikasi(idRekin, reqDTO);

        return ResponseEntity.ok(ApiResponse.updated(updated));
    }

    @DeleteMapping("/{idRekin}")
    @Operation(summary = "Hapus data identifikasi berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<String>> deleteIdentifikasi(@PathVariable String idRekin) {
        identifikasiService.deleteIdentifikasi(idRekin);

        return ResponseEntity.ok(ApiResponse.success(idRekin, "Deleted successfully"));
    }
}
