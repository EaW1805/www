package com.eaw1805.www.controllers.cache.async;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Executes Async calls.
 */
@Component
public class AsyncService {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(AsyncService.class);

//    @Async
//    public void processRequest(final ProceedingJoinPoint thisJoinPoint) throws Throwable {
//        LOGGER.info("Async Call");
//        thisJoinPoint.proceed();
//    }
}
