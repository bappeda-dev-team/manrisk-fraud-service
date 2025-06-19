package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.PemantauanDTO;
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
    @Operation(summary = "Ambil semua data Pemantauan berdasarkan NIP dan Tahun")
    public ResponseEntity<ApiResponse<List<PemantauanDTO>>> getAllData(@PathVariable String nip,
                                                                       @PathVariable String tahun) {
        List<PemantauanDTO> result = pemantauanService.findAllPemantauan(nip, tahun);

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data successfully"));
    }

    @PostMapping
    @Operation(summary = "Simpan data Pemantauan baru")
    public ResponseEntity<ApiResponse<?>> savePemantauan(
            @Valid @RequestBody PemantauanDTO dto,
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

        PemantauanDTO result = pemantauanService.savePemantauan(dto);

        return ResponseEntity.ok(ApiResponse.success(result, "Saved successfully"));
    }
}
