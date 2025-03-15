package picturebot.bot.command.webappdata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Tag("unit")
class WebappDataTest {

    private static final LocalDateTime NOON = LocalDateTime.of(2024, 1, 2, 12, 0);
    private final Clock fixedClock = Clock.fixed(NOON.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Nested
    @DisplayName("toString()")
    class ToString {

        @Test
        @DisplayName("should return String representation of the instance (all true)")
        void shouldReturnStringRepresentationOfInstanceAllTrue() {
            final WebappData data = new WebappData();
            data.setIsEnabled(true);
            data.setIsSpoilerEnabled(true);
            data.setSchedule(LocalTime.now(fixedClock));
            data.setTimezone(1L);

            assertThat(data.toString(), is(equalTo(
                    "WebappData{schedule=12:00, timezone=1, isSpoilerEnabled=true, isEnabled=true}")));
        }

        @Test
        @DisplayName("should return String representation of the instance (all false)")
        void shouldReturnStringRepresentationOfInstanceAllFalse() {
            final WebappData data = new WebappData();
            data.setIsEnabled(false);
            data.setIsSpoilerEnabled(false);
            data.setSchedule(LocalTime.now(fixedClock));
            data.setTimezone(1L);

            assertThat(data.toString(), is(equalTo(
                    "WebappData{schedule=12:00, timezone=1, isSpoilerEnabled=false, isEnabled=false}")));
        }
    }
}
