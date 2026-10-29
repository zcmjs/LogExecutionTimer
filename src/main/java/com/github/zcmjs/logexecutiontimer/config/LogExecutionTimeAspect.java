package com.github.zcmjs.logexecutiontimer.config;


import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LogExecutionTimeAspect {
    private final MeterRegistry meterRegistry;

    @Around("@annotation(com.github.zcmjs.logexecutiontimer.config.LogExecutionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String status = "SUCCESS";

        Timer.Sample timer = Timer.start(meterRegistry);
        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            status = "ERROR";
            throw ex;
        } finally {
            long durationNanos = timer.stop(Timer.builder("method.execution.time")
                    .description("Execution time of annotated method")
                    .tag("class", className)
                    .tag("method", methodName)
                    .tag("status", status)
                    .publishPercentileHistogram() // włącza bucket'y dla percentyli
                    .register(meterRegistry));

            long durationInMs = TimeUnit.NANOSECONDS.toMillis(durationNanos);
            log.debug("method.execution.time : {}#{} executed in {} ms (Status: {})", className, methodName, durationInMs, status);
            System.out.println(String.format("method.execution.time : %s#%s executed in %s ms (Status: %s)", className, methodName, durationInMs, status));
        }

    }
}