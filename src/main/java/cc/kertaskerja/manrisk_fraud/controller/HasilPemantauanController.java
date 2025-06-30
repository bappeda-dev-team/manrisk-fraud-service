package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.hasilPemantauan.HasilPemantauanReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.hasilPemantauan.HasilPemantauanResDTO;
import cc.kertaskerja.manrisk_fraud.helper.Authorization;
import cc.kertaskerja.manrisk_fraud.service.hasilPemantauan.HasilPemantauanService;
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
@RequestMapping("/hasil-pemantauan")
@RequiredArgsConstructor
@Tag(name = "Hasil Pemantauan", description = "API Hasil Pemantauan untuk Manajemen Risiko Fraud")
public class HasilPemantauanController {

    private final Authorization authorization;
    private final HasilPemantauanService hasilPemantauanService;

    @GetMapping("/get-all-data/{nip}/{tahun}")
    @Operation(summary = "Ambil semua data hasil pemantauan berdasarkan NIP dan Tahun")
    public ResponseEntity<ApiResponse<List<HasilPemantauanResDTO>>> getAllData(@PathVariable String nip,
                                                                               @PathVariable String tahun) {
        List<HasilPemantauanResDTO> result = hasilPemantauanService.findAll(nip, tahun);

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data successfully"));
    }

    @GetMapping("/get-detail/{idRekin}")
    @Operation(summary = "Ambil satu data Hasil Pemantauan berdasarkan ID Rekin")
    public ResponseEntity<ApiResponse<HasilPemantauanResDTO>> getByIdRekin(@PathVariable String idRekin) {
        HasilPemantauanResDTO dto = hasilPemantauanService.findOne(idRekin);
        ApiResponse<HasilPemantauanResDTO> response = ApiResponse.success(dto, "Retrieved 1 data successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Simpan data Hasil Pemantauan")
    public ResponseEntity<ApiResponse<?>> saveHasilPemantauan(@Valid @RequestBody HasilPemantauanReqDTO dto,
                                                              BindingResult bindingResult) {
        authorization.checkCanSave(dto.getNip_pembuat());

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

        HasilPemantauanResDTO result = hasilPemantauanService.save(dto);

        return ResponseEntity.ok(ApiResponse.success(result, "Saved successfully"));
    }

    @PutMapping("/{idRekin}")
    @Operation(summary = "Update data Hasil Pemantauan berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<?>> updateHasilPemantauan(@PathVariable String idRekin,
                                                                @Valid @RequestBody HasilPemantauanReqDTO reqDTO,
                                                                BindingResult bindingResult) {
        authorization.checkCanSave(reqDTO.getNip_pembuat());

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

        HasilPemantauanResDTO result = hasilPemantauanService.update(idRekin, reqDTO);

        return ResponseEntity.ok(ApiResponse.success(result, "Updated successfully"));
    }

    @PatchMapping("/{idRekin}")
    @Operation(summary = "Verifikasi Hasil Pemantauan berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<?>> verifyHasilPemantauan(@PathVariable String idRekin,
                                                                @Valid @RequestBody HasilPemantauanReqDTO.UpdateStatusDTO reqDTO,
                                                                BindingResult bindingResult) {
        authorization.checkCanSave(reqDTO.getNip_verifikator());

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

        HasilPemantauanResDTO updated = hasilPemantauanService.verify(idRekin, reqDTO);

        return ResponseEntity.ok(ApiResponse.updated(updated));
    }

    @DeleteMapping("/{idRekin}")
    @Operation(summary = "Hapus data Hasil Pemantauan berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<String>> deleteHasilPemantauan(@PathVariable String idRekin) {
        hasilPemantauanService.delete(idRekin);

        return ResponseEntity.ok(ApiResponse.success(idRekin, "Deleted successfully"));
    }
}
