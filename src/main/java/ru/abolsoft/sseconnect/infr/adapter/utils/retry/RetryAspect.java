package ru.abolsoft.sseconnect.infr.adapter.utils.retry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
//@Component
public class RetryAspect {

    @Around("@annotation(retryAnnotation)")
    public Object retry(ProceedingJoinPoint joinPoint, RetryWithBackoff retryAnnotation) throws Throwable {
        int maxAttempts = retryAnnotation.maxAttempts();
        long delay = retryAnnotation.delay();
        double multiplier = retryAnnotation.multiplier();

        Throwable lastException;
        do {
            try {
                RetryContext retryContext = new RetryContext(maxAttempts, delay, multiplier);
                Object result = joinPoint.proceed();
                return result;
            } catch (Throwable ex) {
                lastException = ex;
                maxAttempts--;
                if (maxAttempts <= 0) {
                    throw lastException;
                }
                Thread.sleep(delay);
                delay *= multiplier;
            }
        } while (maxAttempts > 0);

        throw lastException;
    }

}


