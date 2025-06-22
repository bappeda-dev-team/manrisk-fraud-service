package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.HasilPemantauanDTO;
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

    private final HasilPemantauanService hasilPemantauanService;

    @GetMapping("/get-all-data/{nip}/{tahun}")
    @Operation(summary = "Ambil semua data hasil pemantauan berdasarkan NIP dan Tahun")
    public ResponseEntity<ApiResponse<List<HasilPemantauanDTO>>> getAllData(@PathVariable String nip,
                                                                            @PathVariable String tahun) {
        List<HasilPemantauanDTO> result = hasilPemantauanService.findAll(nip, tahun);

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data successfully"));
    }

    @PostMapping
    @Operation(summary = "Simpan data Hasil Pemantauan")
    public ResponseEntity<ApiResponse<?>> saveHasilPemantauan(@Valid @RequestBody HasilPemantauanDTO dto,
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

        HasilPemantauanDTO result = hasilPemantauanService.save(dto);

        return ResponseEntity.ok(ApiResponse.success(result, "Saved successfully"));
    }
}
