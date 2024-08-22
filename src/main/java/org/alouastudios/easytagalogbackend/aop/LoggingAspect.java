package org.alouastudios.easytagalogbackend.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    // return type, class name.method name(args)

    // Below saws all return types, all classes, all methods, for all args
    @Before("execution(*  org.alouastudios.easytagalogbackend.service.WordService.getAllWords(..)) || execution(* org.alouastudios.easytagalogbackend.service.WordService.getWordById(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        LOGGER.info("Method called: " + joinPoint.getSignature().getName());
    }

    @After("execution(*  org.alouastudios.easytagalogbackend.service.WordService.getAllWords(..)) || execution(* org.alouastudios.easytagalogbackend.service.WordService.getWordById(..))")
    public void logMethodExecuted(JoinPoint joinPoint) {
        LOGGER.info("Method executed: " + joinPoint.getSignature().getName());
    }

    @AfterThrowing("execution(*  org.alouastudios.easytagalogbackend.service.WordService.getAllWords(..)) || execution(* org.alouastudios.easytagalogbackend.service.WordService.getWordById(..))")
    public void logMethodCrash(JoinPoint joinPoint) {
        LOGGER.info("Method has issues: " + joinPoint.getSignature().getName());
    }

    @AfterReturning("execution(*  org.alouastudios.easytagalogbackend.service.WordService.getAllWords(..)) || execution(* org.alouastudios.easytagalogbackend.service.WordService.getWordById(..))")
    public void logMethodExecutedSuccess(JoinPoint joinPoint) {
        LOGGER.info("Method executed successfully: " + joinPoint.getSignature().getName());
    }

}
