package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.risikoKecurangan.RisikoKecuranganDTO;
import cc.kertaskerja.manrisk_fraud.helper.Authorization;
import cc.kertaskerja.manrisk_fraud.service.risikoKecurangan.RisikoKecuranganService;
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
@RequestMapping("/risiko-kecurangan")
@RequiredArgsConstructor
@Tag(name = "Risiko Kecurangan", description = "Kolom CRUD Risiko Kecurangan (*ADMIN)")
public class RisikoKecuranganController {

    private final Authorization authorization;
    private final RisikoKecuranganService risikoKecuranganService;

    @GetMapping
    @Operation(summary = "Menampilkan semua data uraian berdasarkan jenis risiko yang dipilih")
    public ResponseEntity<ApiResponse<List<RisikoKecuranganDTO>>> getAllUraian(@RequestParam(value = "jenis", required = false) String jenisRisiko) {
        List<RisikoKecuranganDTO> result = risikoKecuranganService.findAllByJenisRisiko(jenisRisiko);

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data successfully"));
    }

    @PostMapping
    @Operation(summary = "Tambah jenis risiko beserta uraian")
    public ResponseEntity<ApiResponse<?>> saveRisikoKecurangan(@Valid @RequestBody RisikoKecuranganDTO dto,
                                                               BindingResult bindingResult) {
        authorization.adminOnly(dto.getNip_pembuat());

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

        RisikoKecuranganDTO result = risikoKecuranganService.save(dto);

        return ResponseEntity.ok(ApiResponse.success(result, "Saved successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update data Risiko Kecurangan berdasarkan ID")
    public ResponseEntity<ApiResponse<?>> updateRisikoKecurangan(@PathVariable Long id,
                                                                 @Valid @RequestBody RisikoKecuranganDTO dto,
                                                                 BindingResult bindingResult) {
        authorization.adminOnly(dto.getNip_pembuat());

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

        RisikoKecuranganDTO result = risikoKecuranganService.update(id, dto);

        return ResponseEntity.ok(ApiResponse.success(result, "Updated successfully"));
    }

    @DeleteMapping("/{id}/{nip}")
    @Operation(summary = "Hapus data Risiko Kecurangan berdasarkan ID")
    public ResponseEntity<ApiResponse<String>> deleteRisikoKecurangan(@PathVariable Long id,
                                                                      @PathVariable String nip) {
        authorization.adminOnly(nip);

        risikoKecuranganService.delete(id);

        return ResponseEntity.ok(ApiResponse.success("Risiko Kecurangan ", "Deleted successfully"));
    }
}
