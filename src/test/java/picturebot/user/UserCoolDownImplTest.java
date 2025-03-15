package picturebot.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.core.env.Environment;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@Tag("unit")
class UserCoolDownImplTest {

    private static final LocalDateTime NOON = LocalDateTime.of(2024, 1, 2, 12, 0);

    @Mock private CacheManager cacheManager;
    @Spy private final Clock fixedClock = Clock.fixed(NOON.atZone(ZoneId.of("Europe/Amsterdam")).toInstant(),
            ZoneId.of("Europe/Amsterdam"));
    @Mock private Environment environment;
    @Mock private CaffeineCache caffeineCache;
    @Mock private SimpleValueWrapper simpleValueWrapper;
    @InjectMocks private UserCoolDownImpl pictureCoolDown;

    @Nested
    @DisplayName("isCoolingDown()")
    class IsCoolingDown {

        @Test
        @DisplayName("should return 0, because user 12345 is not in the cache")
        void shouldReturn0BecauseUser12345IsNotInTheCache() {
            when(caffeineCache.putIfAbsent(12345L, LocalDateTime.now(fixedClock))).thenReturn(null);
            when(cacheManager.getCache("userCache")).thenReturn(caffeineCache);

            assertEquals(0, pictureCoolDown.getSecondsLeftOnCoolDown(12345L));
        }

        @Test
        @DisplayName("should return 299, because user 12345 is in the cache")
        void shouldReturn299BecauseUser12345IsInTheCache(final CapturedOutput output) {
            final LocalDateTime nowMinusOneSecond = LocalDateTime.now(fixedClock).minusSeconds(1);
            when(simpleValueWrapper.get()).thenReturn(nowMinusOneSecond);
            when(caffeineCache.putIfAbsent(12345L, LocalDateTime.now(fixedClock))).thenReturn(simpleValueWrapper);
            when(cacheManager.getCache("userCache")).thenReturn(caffeineCache);
            when(environment.getRequiredProperty("bot.cache.ids.duration", Long.class)).thenReturn(300L);

            assertEquals(299, pictureCoolDown.getSecondsLeftOnCoolDown(12345L));
            assertThat(output.getOut(),
                    containsString("INFO  - User 12345 is on cool down for another 299 seconds."));
        }

        @Test
        @DisplayName("should return 0, because the cache is unavailable")
        void shouldReturn0BecauseTheCacheIsUnavailable(final CapturedOutput output) {
            when(cacheManager.getCache("userCache")).thenReturn(null);

            assertEquals(0, pictureCoolDown.getSecondsLeftOnCoolDown(12345L));
            assertThat(output.getOut(), containsString("ERROR - The user cache is unavailable!"));
        }
    }
}
