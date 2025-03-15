package picturebot.picture.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Configuration class for the caches used in the bot.
 */
@EnableCaching
@Configuration
public class PictureCacheConfig {

    /**
     * Cache for storing user IDs. As long as an ID is in the cache, the bot will not send a picture to that user.
     * @param environment The environment object to get the cache duration from the properties file.
     * @return The cache object.
     */
    @Bean
    public Cache<Object, Object> userCache(final Environment environment) {
        return Caffeine.newBuilder()
                .expireAfterWrite(
                        environment.getRequiredProperty("bot.cache.ids.duration", Long.class),
                        TimeUnit.SECONDS)
                .build();
    }

    /**
     * Cache for storing paths to pictures. This prevents accessing the file system for every picture request.
     * @param environment The environment object to get the cache duration from the properties file.
     * @return The cache object.
     */
    @Bean
    public Cache<Object, Object> pictureCache(final Environment environment) {
        return Caffeine.newBuilder()
                .expireAfterWrite(
                        environment.getRequiredProperty("bot.cache.pictures.duration", Long.class),
                        TimeUnit.MINUTES)
                .build();
    }

    /**
     * Cache for storing greetings. This prevents accessing the file system every time the scheduler sends a picture.
     * @param environment The environment object to get the cache duration from the properties file.
     * @return The cache object.
     */
    @Bean
    public Cache<Object, Object> greetingCache(final Environment environment) {
        return Caffeine.newBuilder()
                .expireAfterWrite(
                        environment.getRequiredProperty("bot.cache.greetings.duration", Long.class),
                        TimeUnit.MINUTES)
                .build();
    }

    /**
     * Cache manager for the caches.
     * @param userCache The cache for user IDs.
     * @param pictureCache The cache for picture paths.
     * @param greetingCache The cache for greetings.
     * @return The cache manager object.
     */
    @Bean
    public CacheManager cacheManager(final Cache<Object, Object> userCache,
                                     final Cache<Object, Object> pictureCache,
                                     final Cache<Object, Object> greetingCache) {

        final SimpleCacheManager cacheManager = new SimpleCacheManager();

        cacheManager.setCaches(Arrays.asList(
                new CaffeineCache("userCache", userCache),
                new CaffeineCache("pictureCache", pictureCache),
                new CaffeineCache("greetingCache", greetingCache)));

        return cacheManager;
    }
}
