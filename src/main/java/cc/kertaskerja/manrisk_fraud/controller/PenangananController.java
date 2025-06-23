package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.PenangananDTO;
import cc.kertaskerja.manrisk_fraud.service.penangangan.PenangananService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/penanganan")
@Tag(name = "Penanganan", description = "API Penanganan untuk Manajemen Risiko Fraud")
public class PenangananController {

    private final PenangananService penangananService;

    public PenangananController(PenangananService penangananService) {
        this.penangananService = penangananService;
    }

    @GetMapping("/get-all-data/{nip}/{tahun}")
    @Operation(summary = "Ambil semua data Penanganan berdasarkan NIP dan Tahun")
    public ResponseEntity<ApiResponse<List<PenangananDTO>>> getAllData(@PathVariable String nip,
                                                                       @PathVariable String tahun) {
        List<PenangananDTO> result = penangananService.findAllPenanganan(nip, tahun);

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data successfully"));
    }

    @GetMapping("/get-detail/{idRekin}")
    @Operation(summary = "Ambil satu data Penanganan berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<PenangananDTO>> getByIdRekin(@PathVariable String idRekin) {
        PenangananDTO dto = penangananService.findOnePenanganan(idRekin);
        ApiResponse<PenangananDTO> response = ApiResponse.success(dto, "Retrieved 1 data successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Simpan data Penanganan baru")
    public ResponseEntity<ApiResponse<?>> savePenanganan(@Valid @RequestBody PenangananDTO penangananDto,
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

        PenangananDTO result = penangananService.savePenanganan(penangananDto);

        return ResponseEntity.ok(ApiResponse.success(result, "Saved successfully"));
    }

    @PutMapping("/{idRekin}")
    @Operation(summary = "Update data Penanganan berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<?>> updatePenanganan(@PathVariable String idRekin,
                                                           @Valid @RequestBody PenangananDTO penangananDto,
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

        PenangananDTO result = penangananService.updatePenanganan(idRekin, penangananDto);

        return ResponseEntity.ok(ApiResponse.success(result, "Updated successfully"));
    }

    @PatchMapping("/{idRekin}")
    @Operation(summary = "Update status Penanganan berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<?>> updateStatusPenanganan(@PathVariable String idRekin,
                                                                 @Valid @RequestBody PenangananDTO.UpdateStatusDTO dto,
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

        PenangananDTO updated = penangananService.updateStatusPenanganan(idRekin, dto);

        return ResponseEntity.ok(ApiResponse.updated(updated));
    }


    @DeleteMapping("/{idRekin}")
    @Operation(summary = "Hapus data Penanganan berdasarkan ID Rencana Kinerja")
    public ResponseEntity<ApiResponse<String>> deletePenanganan(@PathVariable String idRekin) {
        penangananService.deletePenanganan(idRekin);

        return ResponseEntity.ok(ApiResponse.success(idRekin, "Deleted successfully"));
    }
}
