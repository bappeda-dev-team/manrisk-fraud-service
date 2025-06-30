package cc.kertaskerja.manrisk_fraud.aop;

import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaReqDTO;
import cc.kertaskerja.manrisk_fraud.helper.Authorization;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {
    private final Authorization authorization;

    @Before("@annotation(canSave) && args(.., @RequestBody body)")
    public void checkAuthorization(CanSave canSave, Object body) {
        if (body instanceof AnalisaReqDTO dto) {
            authorization.checkCanSave(dto.getNip_pembuat());
        }
    }
}
