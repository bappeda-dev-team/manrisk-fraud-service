package cc.kertaskerja.manrisk_fraud.helper;

import cc.kertaskerja.manrisk_fraud.exception.ForbiddenException;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.service.global.PegawaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Authorization {

    private final PegawaiService pegawaiService;

    public void adminOnly(String nip) {
        authorize(nip, Set.of("super_admin", "admin_opd"));
    }

    public void canVerify(String nip) {
        authorize(nip, Set.of("super_admin", "admin_opd", "level_2"));
    }

    public void checkCanSave(String nip) {
        authorize(nip, Set.of("super_admin", "admin_opd", "level_3"));
    }

    private void authorize(String nip, Set<String> allowedRoles) {
        if (!Crypto.isEncrypted(nip)) {
            throw new ResourceNotFoundException("NIP is not encrypted: " + nip);
        }

        Map<String, Object> pegawaiDetail = pegawaiService.getPegawaiDetail(Crypto.decrypt(nip));
        Map<String, Object> data = (Map<String, Object>) pegawaiDetail.get("data");

        if (data == null || !data.containsKey("role")) {
            throw new RuntimeException("Role information is missing");
        }

        List<Map<String, Object>> roles = (List<Map<String, Object>>) data.get("role");

        boolean authorized = roles.stream()
                .map(role -> String.valueOf(role.get("role")))
                .anyMatch(allowedRoles::contains);

        if (!authorized) {
            throw new ForbiddenException("Access Denied: User does not have permission");
        }
    }
}
