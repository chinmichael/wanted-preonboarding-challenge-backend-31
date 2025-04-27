package com.wanted.cqrs.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.util.Arrays;

@Aspect
public class LogAspect {

    private final Environment env;
    public LogAspect(Environment env) { this.env = env; }

    /**
     * Spring bean 포인트 컷
     */
    @Pointcut(
            "within(@org.springframework.stereotype.Repository *)" +
                    " || within(@org.springframework.stereotype.Service *)" +
                    " || within(@org.springframework.web.bind.annotation.RestController *)"
    )
    public void springBeanPointcut() {}

    /**
     * app 패키지 포인트 컷
     */
    @Pointcut("within(com.wanted.cqrs.apis..*)")
    public void applicationPackagePointcut() {}

    /**
     * get join point logger
     * @param joinPoint target join point
     * @return Logger
     */
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    /**
     * 예외 로깅 처리 (개발의 경우 예외 정보 전체 로깅)
     * @param joinPoint join point for advice.
     * @param e exception.
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (env.acceptsProfiles(Profiles.of("dev"))) {
            logger(joinPoint)
                    .error(
                            "Exception in {}() with cause = '{}' and exception = '{}'",
                            joinPoint.getSignature().getName(),
                            e.getCause() != null ? e.getCause() : "NULL",
                            e.getMessage(),
                            e
                    );
        } else {
            logger(joinPoint)
                    .error(
                            "Exception in {}() with cause = {}",
                            joinPoint.getSignature().getName(),
                            e.getCause() != null ? e.getCause() : "NULL"
                    );
        }
    }

    /**
     * 프로그램 실행 이력 로깅
     * @param joinPoint join point for advice.
     * @return result.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);

        // bean enter logging
        if (log.isDebugEnabled()) log.debug("Start: {}() \n Arguments = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        else log.info("Start: {}()", joinPoint.getSignature().getName());

        // bean exit logging
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) log.debug("End: {}() \n Result = {}", joinPoint.getSignature().getName(), result);
            else log.info("End: {}()", joinPoint.getSignature().getName());
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}()", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
