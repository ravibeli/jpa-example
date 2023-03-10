package com.hr.app.jpaexample.common;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Aspect
@Configuration
@ConditionalOnProperty(value = "aop.log-aspect-enabled",
        havingValue = "true",
        matchIfMissing = false)
public class LoggerAspect {

    /**
     * logMethodExecutionTime will log the execution time of the methods.
     *
     * @param proceedingJoinPoint the join point
     * @throws Throwable the throwable exception
     */
    @Around("logForPoAndRbacPackage()")
    public Object logMethodExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        final StopWatch stopWatch = new StopWatch();

        //calculate method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        //Log method execution time
        log.info("Logging AOP: "
                + methodSignature.getDeclaringType().getSimpleName() // Class Name
                + "." + methodSignature.getName() + " " // Method Name
                + ":: Execution time: " + stopWatch.getTime() + "ms");

        return result;
    }

    // execution(* com.blueyonder.po..*(..)))
    @Pointcut("execution(* com.hr.app.jpaexample..*(..)) || execution(* com.blueyonder.rbac..*(..))")
    void logForPoAndRbacPackage() {}

}
