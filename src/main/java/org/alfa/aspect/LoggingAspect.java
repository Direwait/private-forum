package org.alfa.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || "
            + "within(@org.springframework.stereotype.Service *)")
    public void appPointcut() {
    }

    @Around("appPointcut()")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        String target = pjp.getSignature().getDeclaringType().getSimpleName()
                + "." + pjp.getSignature().getName();
        String args = mask(Arrays.toString(pjp.getArgs()));

        log.info("Call: {} | Args: {}", target, args);

        try {
            Object result = pjp.proceed();
            log.info("Success: {} | Result: {}", target, safeResult(result));
            return result;
        } catch (Throwable ex) {
            log.error("Exception in {} | {}: {}", target,
                    ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }
    }

    /**
     * Never call toString() on arbitrary objects - it may contain secrets
     * (password hashes, JWT tokens, personal data). Safe only for
     * collections and ResponseEntity status.
     */
    private static String safeResult(Object result) {
        if (result == null) {
            return "null";
        }
        if (result instanceof ResponseEntity<?> re) {
            return "ResponseEntity[" + re.getStatusCode() + "]";
        }
        if (result instanceof Collection<?> c) {
            return "Collection[size=" + c.size() + "]";
        }
        if (result instanceof Page<?> p) {
            return "Page[number=" + p.getNumber() + ", size=" + p.getSize()
                    + ", total=" + p.getTotalElements() + "]";
        }
        return result.getClass().getSimpleName();
    }

    private static String mask(String s) {
        return s.replaceAll("(?i)(password|token|secret|authorization)=[^,)\\]]+", "$1=***");
    }
}