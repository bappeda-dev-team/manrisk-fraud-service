package cc.kertaskerja.manrisk_fraud.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* cc.kertaskerja.manrisk_fraud.service..*(..))")
    public Object logExecutionTimeAndErrors(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            log.info("✅ Executed {} in {} ms", methodName, duration);
            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - startTime;

            log.error("❌ Exception in {} after {} ms: {}", methodName, duration, ex.getMessage(), ex);
            throw ex;
        }
    }
}
