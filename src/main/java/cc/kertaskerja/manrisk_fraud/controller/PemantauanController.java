package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.pemantauan.PemantauanReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.pemantauan.PemantauanResDTO;
import cc.kertaskerja.manrisk_fraud.service.pemantauan.PemantauanService;
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
@RequestMapping("/pemantauan")
@RequiredArgsConstructor
@Tag(name = "Pemantauan", description = "API Pemantauan untuk Manajemen Risiko Fraud")
public class PemantauanController {

    private final PemantauanService pemantauanService;

    @GetMapping("/get-all-data/{nip}/{tahun}")
    @Operation(summary = "Ambil semua data Pemantauan RTP berdasarkan NIP dan Tahun")
    public ResponseEntity<ApiResponse<List<PemantauanResDTO>>> getAllData(@PathVariable String nip,
                                                                          @PathVariable String tahun) {
        List<PemantauanResDTO> result = pemantauanService.findAllPemantauan(nip, tahun);

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data successfully"));
    }

    @GetMapping("/get-detail/{idRekin}")
    @Operation(summary = "Ambil satu data Pemantauan RTP berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<PemantauanResDTO>> getByIdRekin(@PathVariable String idRekin) {
        PemantauanResDTO dto = pemantauanService.findOnePemantauan(idRekin);
        ApiResponse<PemantauanResDTO> response = ApiResponse.success(dto, "Retrieved 1 data successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Simpan data Pemantauan RTP baru")
    public ResponseEntity<ApiResponse<?>> savePemantauan(@Valid @RequestBody PemantauanReqDTO dto,
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

        PemantauanResDTO result = pemantauanService.savePemantauan(dto);

        return ResponseEntity.ok(ApiResponse.success(result, "Saved successfully"));
    }

    @PutMapping("/{idRekin}")
    @Operation(summary = "Update data Pemantauan RTP berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<?>> updatePemantauan(@PathVariable String idRekin,
                                                           @Valid @RequestBody PemantauanReqDTO dto,
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

        PemantauanResDTO result = pemantauanService.updatePemantauan(idRekin, dto);

        return ResponseEntity.ok(ApiResponse.success(result, "Updated successfully"));
    }

    @PatchMapping("/{idRekin}")
    @Operation(summary = "Update status Pemantauan RTP berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<?>> updateStatusPemantauan(@PathVariable String idRekin,
                                                                 @Valid @RequestBody PemantauanReqDTO.UpdateStatusDTO dto,
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

        PemantauanResDTO updated = pemantauanService.verifyPemantauan(idRekin, dto);

        return ResponseEntity.ok(ApiResponse.updated(updated));
    }

    @DeleteMapping("/{idRekin}")
    @Operation(summary = "Hapus data Pemantauan RTP berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<String>> deletePemantauan(@PathVariable String idRekin) {
        pemantauanService.deletePemantauan(idRekin);

        return ResponseEntity.ok(ApiResponse.success(idRekin, "Deleted successfully"));
    }
}
