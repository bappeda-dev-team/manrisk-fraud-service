package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaResDTO;
import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.helper.Authorization;
import cc.kertaskerja.manrisk_fraud.service.analisa.AnalisaService;
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
@RequestMapping("/analisa")
@RequiredArgsConstructor
@Tag(name = "Analisa", description = "API Identifikasi untuk Manajemen Risiko Fraud")
public class AnalisaController {

    private final Authorization authorization;
    private final AnalisaService analisaService;

    @GetMapping("/get-all-data/{nip}/{tahun}")
    @Operation(summary = "Ambil semua data Analisa berdasarkan NIP dan Tahun")
    public ResponseEntity<ApiResponse<List<AnalisaResDTO>>> getAllData(@PathVariable String nip,
                                                                       @PathVariable String tahun) {
        List<AnalisaResDTO> result = analisaService.findAllAnalisa(nip, tahun);

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data successfully"));
    }

    @GetMapping("/get-detail/{idRekin}")
    @Operation(summary = "Ambil satu data Analisa berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<AnalisaResDTO>> getByIdRekin(@PathVariable String idRekin) {
        AnalisaResDTO dto = analisaService.findOneAnalisa(idRekin);
        ApiResponse<AnalisaResDTO> response = ApiResponse.success(dto, "Retrieved 1 data successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Simpan data Analisa baru")
    public ResponseEntity<ApiResponse<?>> saveAnalisa(@Valid @RequestBody AnalisaReqDTO reqDTO,
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

        AnalisaResDTO result = analisaService.saveAnalisa(reqDTO);

        return ResponseEntity.ok(ApiResponse.success(result, "Saved successfully"));
    }

    @PutMapping("/{idRekin}")
    @Operation(summary = "Update data identifikasi berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<?>> updateAnalisa(@PathVariable String idRekin,
                                                        @Valid @RequestBody AnalisaReqDTO reqDTO,
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

        AnalisaResDTO result = analisaService.updateAnalisa(idRekin, reqDTO);

        return ResponseEntity.ok(ApiResponse.success(result, "Updated successfully"));
    }

    @PatchMapping("/{idRekin}")
    @Operation(summary = "Verifikasi Analisa berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<?>> updateStatusAnalisa(@PathVariable String idRekin,
                                                              @Valid @RequestBody AnalisaReqDTO.UpdateStatusDTO updateDto,
                                                              BindingResult bindingResult) {
        authorization.checkCanSave(updateDto.getNip_verifikator());

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

        AnalisaResDTO updated = analisaService.verifyAnalisa(idRekin, updateDto);

        return ResponseEntity.ok(ApiResponse.updated(updated));
    }

    @DeleteMapping("/{idRekin}")
    @Operation(summary = "Hapus data identifikasi berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<String>> deleteAnalisa(@PathVariable String idRekin) {
        analisaService.deleteAnalisa(idRekin);

        return ResponseEntity.ok(ApiResponse.success(idRekin, "Deleted successfully"));
    }
}
