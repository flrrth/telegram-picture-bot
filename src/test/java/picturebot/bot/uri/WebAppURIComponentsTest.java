package picturebot.bot.uri;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
class WebAppURIComponentsTest {

    private static final LocalDateTime NOON = LocalDateTime.of(2024, 1, 2, 12, 0);
    private final Clock fixedClock = Clock.fixed(NOON.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Nested
    @DisplayName("getUrl()")
    class GetUrl {

        @Test
        @DisplayName("should return URL with all parameters")
        void shouldReturnURLWithAllParameters() throws MalformedURLException, URISyntaxException {
            final String url = new WebAppURIComponents.Builder("localhost", "/bot.html", "en")
                    .isEnabled(true)
                    .isSpoilerEnabled(true)
                    .schedule(LocalTime.now(fixedClock))
                    .timezone(1L)
                    .build().getUrl();

            assertEquals("https://localhost:443/bot.html?language=en&isEnabled=true&isSpoilerEnabled=true&schedule=12:00&timezone=1", url);
        }

        @Test
        @DisplayName("should return URL with all parameters when default fields are omitted")
        void shouldReturnURLWithAllParametersWhenDefaultFieldsAreOmitted() throws MalformedURLException, URISyntaxException {
            final String url = new WebAppURIComponents.Builder("localhost", "/bot.html", "en")
                    .schedule(LocalTime.now(fixedClock))
                    .timezone(1L)
                    .build().getUrl();

            assertEquals("https://localhost:443/bot.html?language=en&isEnabled=true&isSpoilerEnabled=true&schedule=12:00&timezone=1", url);
        }

        @Test
        @DisplayName("should return URL with default parameters when optional fields are omitted")
        void shouldReturnURLWithDefaultParametersWhenOptionalFieldsAreOmitted() throws MalformedURLException, URISyntaxException {
            final String url = new WebAppURIComponents.Builder("localhost", "/bot.html", "en").build().getUrl();

            assertEquals("https://localhost:443/bot.html?language=en&isEnabled=true&isSpoilerEnabled=true", url);
        }
    }
}
