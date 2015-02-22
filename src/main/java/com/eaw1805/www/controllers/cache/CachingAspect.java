package com.eaw1805.www.controllers.cache;

import com.eaw1805.data.cache.ClientCachable;
import com.eaw1805.data.cache.GameCachable;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.UserGame;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Caching Aspect.
 */
@Aspect
public class CachingAspect {

    /**
     * Static logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(CachingAspect.class);

    // inject the actual template
    @Autowired
    private org.springframework.data.redis.core.RedisTemplate<String, Object> redisTemplate;

    public static final String CACHE_NAME = "eaw-";

    public CachingAspect() {
        LOGGER.debug("CachingAspect initialized");
    }

    /**
     * This pointcut matches all methods with a <code>@Cachable</code> annotation.
     */
    @Pointcut("@annotation( thisCachename )")
    @SuppressWarnings("unused")
    private void cache(final empire.data.cache.Cachable thisCachename) {
        // do nothing
    }

    /**
     * Returns objects from the cache if necessary.
     *
     * @param thisJoinPoint the JoinPoint
     * @return the result of the given function
     */
    @Around("cache(thisCachename)")
    public Object aroundCache(final ProceedingJoinPoint thisJoinPoint, final empire.data.cache.Cachable thisCachename)
            throws Throwable {
        try {
            if (thisJoinPoint.getKind().equals(ProceedingJoinPoint.METHOD_CALL)) {
                return thisJoinPoint.proceed();

            } else {
                final String trueCacheName = CACHE_NAME + thisCachename.cacheName();
                final long timeStamp = System.currentTimeMillis();

                final String thisJoinPointName = constructCacheKey(thisJoinPoint);
                final String thisJoinPointArgs = getJointPointArgs(thisJoinPoint);
                final String objName = thisJoinPointName + "-" + thisJoinPointArgs;

                //search constant cache for an obj name.
                final Object cacheContent = redisTemplate.opsForHash().get(trueCacheName, objName);
                if (cacheContent == null) {
                    // compute result of invocation
                    final Object result = thisJoinPoint.proceed();

                    // keep result in cache
                    redisTemplate.opsForHash().put(trueCacheName, objName, result);

                    final long timeStampNow = System.currentTimeMillis();
                    LOGGER.debug("[" + trueCacheName + "] MISS "
                            + thisJoinPointName
                            + " ["
                            + thisJoinPointArgs
                            + "] "
                            + (timeStampNow - timeStamp) + " ms");

                    // return result
                    return result;

                } else {
                    // serve cached result
                    final long timeStampNow = System.currentTimeMillis();
                    LOGGER.debug("[" + trueCacheName + "] HIT "
                            + thisJoinPointName
                            + " ["
                            + thisJoinPointArgs
                            + "] "
                            + (timeStampNow - timeStamp) + " ms");

                    return cacheContent;
                }
            }

        } catch (final Exception ex) {
            LOGGER.error("Caching aspect", ex);
            return thisJoinPoint.proceed();
        }
    }

    @Pointcut("@annotation( thisCachename )")
    @SuppressWarnings("unused")
    private void evictCache(final empire.data.cache.EvictCache thisCachename) {
        // do nothing
    }

    @Around("evictCache(thisCachename)")
    public Object processRequest(final ProceedingJoinPoint thisJoinPoint, final empire.data.cache.EvictCache thisCachename)
            throws Throwable {
        try {
            final String thisJoinPointName = constructCacheKey(thisJoinPoint);
            LOGGER.debug("Evict cache invocation for "
                    + thisCachename.cacheName() + "/" + thisCachename.gameId() + "/" + thisCachename.nationId()
                    + " | " + thisJoinPoint.getKind()
                    + " | " + thisJoinPointName);

            if (thisJoinPoint.getKind().equals(ProceedingJoinPoint.METHOD_CALL)) {
                if ("userGame".equals(thisCachename.cacheName())) {
                    //first delete all functions that don't have a game parameter.
                    redisTemplate.delete(CACHE_NAME + "gameCache");
                    LOGGER.debug("[" + thisCachename.cacheName() + "] EVICT " + thisJoinPointName);

                    //then check for game parameter and delete.
                    if (thisJoinPoint.getArgs().length > 0
                            && thisJoinPoint.getArgs()[0] instanceof UserGame) {

                        final Game game = ((UserGame) thisJoinPoint.getArgs()[0]).getGame();
                        redisTemplate.delete(CACHE_NAME + "game-" + game.getGameId());

                        LOGGER.debug("[game-" + game.getGameId() + "] EVICT " + thisJoinPointName);
                    }
                }

                return thisJoinPoint.proceed();

            } else if (thisJoinPoint.getKind().equals(ProceedingJoinPoint.METHOD_EXECUTION)) {
                if ("client".equals(thisCachename.cacheName())) {
                    final int gameId = (Integer) thisJoinPoint.getArgs()[thisCachename.gameId()];
                    final int nationId = (Integer) thisJoinPoint.getArgs()[thisCachename.nationId()];

                    redisTemplate.delete(CACHE_NAME + "client-" + gameId + "-" + nationId);
                    LOGGER.debug("[client-" + gameId + "-" + nationId + "] EVICT " + thisJoinPointName);
                }

                return thisJoinPoint.proceed();
            }

        } catch (final Exception e) {
            LOGGER.error("Failed to evict cache", e);
        }

        return thisJoinPoint.proceed();
    }

    /**
     * This pointcut matches all methods with a <code>@GameCachable</code> annotation.
     */
    @Pointcut("@annotation( thisCachename )")
    @SuppressWarnings("unused")
    private void gameCache(final GameCachable thisCachename) {
        // do nothing
    }

    /**
     * Returns objects from the cache if necessary.
     *
     * @param thisJoinPoint the JoinPoint
     * @return the result of the given function
     */
    @Around("gameCache(thisCachename)")
    public Object aroundGameCache(final ProceedingJoinPoint thisJoinPoint, final GameCachable thisCachename)
            throws Throwable {
        try {
            if (thisJoinPoint.getKind().equals(ProceedingJoinPoint.METHOD_CALL)) {
                return thisJoinPoint.proceed();

            } else {
                final int gameId = getGameId(thisJoinPoint);
                final String trueCacheName = CACHE_NAME + "game-" + gameId;

                final long timeStamp = System.currentTimeMillis();

                final String thisJoinPointName = constructCacheKey(thisJoinPoint);
                final String thisJoinPointArgs = getJointPointArgsExcGame(thisJoinPoint);
                final String objName = thisJoinPointName + "-" + thisJoinPointArgs;

                //search constant cache for an obj name.
                final Object cacheContent = redisTemplate.opsForHash().get(trueCacheName, objName);
                if (cacheContent == null) {
                    // compute result of invocation
                    final Object result = thisJoinPoint.proceed();

                    // keep result in cache
                    redisTemplate.opsForHash().put(trueCacheName, objName, result);

                    final long timeStampNow = System.currentTimeMillis();
                    LOGGER.debug("[" + trueCacheName + "] MISS "
                            + thisJoinPointName
                            + " ["
                            + thisJoinPointArgs
                            + "] "
                            + (timeStampNow - timeStamp) + " ms");

                    // return result
                    return result;

                } else {
                    // serve cached result
                    final long timeStampNow = System.currentTimeMillis();
                    LOGGER.debug("[" + trueCacheName + "] HIT "
                            + thisJoinPointName
                            + " ["
                            + thisJoinPointArgs
                            + "] "
                            + (timeStampNow - timeStamp) + " ms");

                    return cacheContent;
                }
            }

        } catch (final Exception ex) {
            LOGGER.error("Caching aspect", ex);
            return thisJoinPoint.proceed();
        }
    }

    /**
     * This pointcut matches all methods with a <code>@ClientCachable</code> annotation.
     */
    @Pointcut("@annotation( thisCachename )")
    @SuppressWarnings("unused")
    private void clientCache(final ClientCachable thisCachename) {
        // do nothing
    }

    /**
     * Returns objects from the cache if necessary.
     *
     * @param thisJoinPoint the JoinPoint
     * @return the result of the given function
     */
    @Around("clientCache(thisCachename)")
    public Object aroundClientCache(final ProceedingJoinPoint thisJoinPoint, final ClientCachable thisCachename)
            throws Throwable {
        try {
            if (thisJoinPoint.getKind().equals(ProceedingJoinPoint.METHOD_CALL)) {
                return thisJoinPoint.proceed();

            } else {
                final String trueCacheName;
                if (thisCachename.cacheName().equals("gameCache")) {
                    final int gameId = (Integer) thisJoinPoint.getArgs()[thisCachename.gameId()];
                    trueCacheName = CACHE_NAME + "game-" + gameId;

                } else {
                    final int gameId = fixGameId(thisJoinPoint.getArgs()[thisCachename.gameId()]);
                    final int nationId = fixNationId(thisJoinPoint.getArgs()[thisCachename.nationId()]);
                    trueCacheName = CACHE_NAME + "client-" + gameId + "-" + nationId;
                }

                final long timeStamp = System.currentTimeMillis();

                final String thisJoinPointName = constructCacheKey(thisJoinPoint);
                final String thisJoinPointArgs = getJointPointArgs(thisJoinPoint);
                final String objName = thisJoinPointName + "-" + thisJoinPointArgs;

                //search constant cache for an obj name.
                final Object cacheContent = redisTemplate.opsForHash().get(trueCacheName, objName);
                if (cacheContent == null) {
                    // compute result of invocation
                    final Object result = thisJoinPoint.proceed();

                    // keep result in cache
                    redisTemplate.opsForHash().put(trueCacheName, objName, result);

                    final long timeStampNow = System.currentTimeMillis();
                    LOGGER.debug("[" + trueCacheName + "] MISS "
                            + thisJoinPointName
                            + " ["
                            + thisJoinPointArgs
                            + "] "
                            + (timeStampNow - timeStamp) + " ms");

                    // return result
                    return result;

                } else {
                    // serve cached result
                    final long timeStampNow = System.currentTimeMillis();
                    LOGGER.debug("[" + trueCacheName + "] HIT "
                            + thisJoinPointName
                            + " ["
                            + thisJoinPointArgs
                            + "] "
                            + (timeStampNow - timeStamp) + " ms");

                    return cacheContent;
                }
            }

        } catch (final Exception ex) {
            LOGGER.error("Caching aspect", ex);
            return thisJoinPoint.proceed();
        }
    }

    private String constructCacheKey(final ProceedingJoinPoint thisJoinPoint) {
        return new StringBuilder()
                .append(thisJoinPoint.getSignature().getDeclaringType().getCanonicalName())
                .append(".")
                .append(thisJoinPoint.getSignature().getName())
                .toString();
    }

    /**
     * Returns the arguments of the current join point as a string.
     *
     * @param joinPoint the joint point object.
     * @return string representing the arguments of this join point
     */
    public final String getJointPointArgs(final JoinPoint joinPoint) {
        final StringBuilder buf = new StringBuilder();

        for (final Object arg : joinPoint.getArgs()) {
            buf.append(arg.hashCode());
            buf.append("_");
        }
        return buf.toString().replaceAll("\\+$", "");
    }

    /**
     * Returns the arguments of the current join point as a string excluding arguments of type Game.
     *
     * @param joinPoint the joint point object.
     * @return string representing the arguments of this join point
     */
    public final String getJointPointArgsExcGame(final JoinPoint joinPoint) {
        final StringBuilder buf = new StringBuilder();

        for (final Object arg : joinPoint.getArgs()) {
            if (!(arg instanceof Game)) {
                buf.append(arg.hashCode());
            }
        }
        return buf.toString().replaceAll("\\+$", "");
    }

    /**
     * Returns the Game ID after inspecting the arguments.
     *
     * @param joinPoint the joint point object.
     * @return the ID of the game.
     */
    public final int getGameId(final JoinPoint joinPoint) {
        for (final Object arg : joinPoint.getArgs()) {
            if (arg instanceof Game) {
                final Game thisGame = (Game) arg;
                return thisGame.getGameId();
            }
        }

        // for some weird reson, no Game object found
        return 0;
    }

    /**
     * Calculate game id from the given parameter.
     * The parameter should be one of the following:
     * The Game object.
     * The id of the Game object.
     *
     * @param arg The parameter to calculate the id.
     * @return The game id.
     */
    public final int fixGameId(final Object arg) {
        if (arg instanceof Game) {
            return ((Game) arg).getGameId();
        } else {
            return (Integer) arg;
        }
    }

    /**
     * Calculate nation id from the given parameter.
     * The parameter should be one of the following:
     * The Nation object.
     * The id of the Nation object.
     *
     * @param arg The parameter to calculate the id.
     * @return The nation id.
     */
    public final int fixNationId(final Object arg) {
        if (arg instanceof Nation) {
            return ((Nation) arg).getId();
        } else {
            return (Integer) arg;
        }
    }

}

