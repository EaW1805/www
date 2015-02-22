package com.eaw1805.www.controllers.cache.async;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

/**
 * Caching Aspect.
 */
@Aspect
public class AsyncAspect {

    /**
     * Static logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(AsyncAspect.class);

    /**
     * Async Service.
     */
    private final AsyncService asyncService;

    public AsyncAspect() {
        LOGGER.debug("AsyncAspect initialized");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/empire/webapp/resources/empire-servlet-mini.xml");

        asyncService = (AsyncService) ctx.getBean("asyncService");
    }

    /**
     * This pointcut matches all methods with a <code>@Cachable</code> annotation.
     */
    @Pointcut("@annotation( thisEawAsync )")
    @SuppressWarnings("unused")
    private void eawAsyncCall(final EawAsync thisEawAsync) {
        // do nothing
    }

    /**
     * Returns objects from the cache if necessary.
     *
     * @param thisJoinPoint the JoinPoint
     * @return the result of the given function
     */
    @Around("eawAsyncCall(thisEawAsync)")
    public void aroundEawAsync(final ProceedingJoinPoint thisJoinPoint, final EawAsync thisEawAsync)
            throws Throwable {

        if (thisJoinPoint.getKind().equals(ProceedingJoinPoint.METHOD_CALL)) {
            thisJoinPoint.proceed();

        } else {
            LOGGER.debug("ASYNC CALL "
                    + thisJoinPoint.getSignature().getDeclaringType().getCanonicalName()
                    + "."
                    + thisJoinPoint.getSignature().getName()
                    + "["
                    + Arrays.toString(thisJoinPoint.getArgs())
                    + "]");

            asyncService.processRequest(thisJoinPoint);
        }
    }

}

