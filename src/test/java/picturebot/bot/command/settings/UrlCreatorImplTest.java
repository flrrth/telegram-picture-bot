package picturebot.bot.command.settings;

import picturebot.entities.settings.Settings;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class UrlCreatorImplTest {
    private static final LocalDateTime NOON = LocalDateTime.of(2024, 1, 2, 12, 0);
    private final Clock fixedClock = Clock.fixed(NOON.atZone(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault());

    @Mock Environment environment;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) Settings settings;

    @InjectMocks UrlCreatorImpl urlCreator;

    @Nested
    @DisplayName("create()")
    class Create {

        @BeforeEach
        void setUp() {
            when(environment.getRequiredProperty("bot.webappHost")).thenReturn("localhost");
            when(environment.getRequiredProperty("bot.webappPath")).thenReturn("/foo");
        }

        @Test
        @DisplayName("should return simplified URL when user has no settings")
        void shouldReturnSimplifiedUrlWhenUserHasNoSettings() {
            final String url = urlCreator.create(null, Locale.ENGLISH);

            assertThat(url, is(notNullValue()));
            assertThat(url, is(equalTo(
                    "https://localhost:443/foo?language=en&isEnabled=true&isSpoilerEnabled=true")));
        }

        @Test
        @DisplayName("should return full URL when user has settings")
        void shouldReturnFullUrlWhenUserHasNoSettings() {
            when(settings.getSchedule()).thenReturn(LocalTime.now(fixedClock));
            when(settings.getTimezone().getId()).thenReturn(1L);
            when(settings.getIsEnabled()).thenReturn(true);
            when(settings.getSpoilerEnabled()).thenReturn(true);

            final String url = urlCreator.create(settings, Locale.ENGLISH);

            assertThat(url, is(notNullValue()));
            assertThat(url, is(equalTo("https://localhost:443/foo?language=en&isEnabled=true" +
                    "&isSpoilerEnabled=true&schedule=12:00&timezone=1")));
        }

        @Test
        @DisplayName("should throw exception when a valid URL can't be constructed")
        void shouldThrowExceptionWhenValidUrlCantBeConstructed() {
            when(environment.getRequiredProperty("bot.webappHost")).thenReturn("");

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> urlCreator.create(settings, Locale.ENGLISH));

            assertThat(exception.getMessage(), is(equalTo("Could not create the webapp URL.")));
        }
    }
}
