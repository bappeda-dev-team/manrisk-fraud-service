package cc.kertaskerja.manrisk_fraud.controller;

import cc.kertaskerja.manrisk_fraud.dto.ApiResponse;
import cc.kertaskerja.manrisk_fraud.dto.EncryptDTO;
import cc.kertaskerja.manrisk_fraud.service.external.EncryptService;
import cc.kertaskerja.manrisk_fraud.service.external.OPDService;
import cc.kertaskerja.manrisk_fraud.service.external.RencanaKinerjaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/external")
public class ExternalAPIController {

    private final OPDService opdService;
    private final RencanaKinerjaService rekinList;
    private final EncryptService encryptService;

    @GetMapping("/opdlist")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getOpdList() {
        List<Map<String, Object>> result = opdService.findAll();

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data Perangkat Daerah successfully"));
    }

    @GetMapping("/rencana-kinerja/{nip}/{tahun}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRekinList(@PathVariable String nip,
                                                                               @PathVariable String tahun) {
        List<Map<String, Object>> result = rekinList.findAll(nip, tahun);

        return ResponseEntity.ok(ApiResponse.success(result, "Retrieved " + result.size() + " data Rencana Kinerja successfully"));
    }


    @PostMapping("/encrypt")
    public ResponseEntity<ApiResponse<?>> encrypt(@Valid @RequestBody EncryptDTO request,
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

        String encrypted = encryptService.encrypt(request.getData());

        return ResponseEntity.ok(ApiResponse.success(encrypted, "Encrypted successfully"));
    }

    @PostMapping("/decrypt")
    public ResponseEntity<ApiResponse<?>> decrypt(@Valid @RequestBody EncryptDTO request,
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

        String decrypted = encryptService.decrypt(request.getData());

        return ResponseEntity.ok(ApiResponse.success(decrypted, "Decrypted successfully"));
    }
}
