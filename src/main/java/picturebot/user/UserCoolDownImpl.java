package picturebot.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class that is responsible for checking if a user is on cool down.
 */
@Component
public class UserCoolDownImpl implements UserCoolDown {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCoolDownImpl.class);
    private final CacheManager cacheManager;
    private final Clock clock;
    private final Environment environment;

    public UserCoolDownImpl(final CacheManager cacheManager,
                            final Clock clock,
                            final Environment environment) {

        this.cacheManager = cacheManager;
        this.clock = clock;
        this.environment = environment;
    }

    /**
     * Checks if the user is on cool down.
     * @param id the user ID
     * @return the seconds left on cool down
     */
    @Override
    public long getSecondsLeftOnCoolDown(final Long id) {
        final Cache cache = cacheManager.getCache("userCache");
        long isUserOnCoolDown = 0;

        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.putIfAbsent(id, LocalDateTime.now(clock));

            if (valueWrapper != null) {
                final LocalDateTime now = LocalDateTime.now(clock);
                final LocalDateTime timeWhenCached = (LocalDateTime) valueWrapper.get();
                final long cacheDuration = environment.getRequiredProperty("bot.cache.ids.duration", Long.class);
                final Duration duration = Duration.between(Objects.requireNonNull(timeWhenCached), now);

                if (duration.toSeconds() < cacheDuration) {
                    // Calculate the seconds of cool down left:
                    isUserOnCoolDown = cacheDuration - duration.toSeconds();
                }

                LOGGER.info("User {} is on cool down for another {} seconds.", id, isUserOnCoolDown);
            }
        }
        else {
            LOGGER.error("The user cache is unavailable!");
        }

        return isUserOnCoolDown;
    }
}
