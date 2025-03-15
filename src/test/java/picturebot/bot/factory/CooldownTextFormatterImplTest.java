package picturebot.bot.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class CooldownTextFormatterImplTest {

    @Mock private MessageSource messageSource;

    @InjectMocks private CooldownTextFormatterImpl cooldownTextFormatter;

    @Nested
    @DisplayName("format()")
    class Format {

        @Test
        @DisplayName("should return empty String when cool down time is 0 seconds")
        void shouldReturnEmptyStringWhenCoolDownTimeIs0Seconds() {
            assertEquals("", cooldownTextFormatter.format(0, Locale.ENGLISH));
        }

        @Test
        @DisplayName("should return String with text '1 second' when cool down time is 1 second")
        void shouldReturnStringWithText1SecondWhenCoolDownTimeIs1Second() {
            when(messageSource.getMessage("coolDown.second", new Integer[] { 1 }, Locale.ENGLISH)).thenReturn("1 second");
            assertEquals("1 second", cooldownTextFormatter.format(1, Locale.ENGLISH));
        }

        @Test
        @DisplayName("should return String with text '2 seconds' when cool down time is 2 seconds")
        void shouldReturnStringWithText2SecondsWhenCoolDownTimeIs2Seconds() {
            when(messageSource.getMessage("coolDown.seconds", new Integer[] { 2 }, Locale.ENGLISH)).thenReturn("1 second");
            assertEquals("1 second", cooldownTextFormatter.format(2, Locale.ENGLISH));
        }

        @Test
        @DisplayName("should return String with text '1 minute' when cool down time is 1 minute")
        void shouldReturnStringWithText1MinuteWhenCoolDownTimeIs1Minute() {
            when(messageSource.getMessage("coolDown.minute", new Integer[] { 1 }, Locale.ENGLISH)).thenReturn("1 minute");
            assertEquals("1 minute", cooldownTextFormatter.format(60, Locale.ENGLISH));
        }

        @Test
        @DisplayName("should return String with text '1 minute and 1 second' when cool down time is 1 minute and 1 second")
        void shouldReturnStringWithText1MinuteAnd1SecondWhenCoolDownTimeIs1MinuteAnd1Second() {
            when(messageSource.getMessage("coolDown.minute", new Integer[] { 1 }, Locale.ENGLISH)).thenReturn("1 minute");
            when(messageSource.getMessage("coolDown.second", new Integer[] { 1 }, Locale.ENGLISH)).thenReturn("1 second");
            when(messageSource.getMessage("coolDown.and", null, Locale.ENGLISH)).thenReturn("and");
            assertEquals("1 minute and 1 second", cooldownTextFormatter.format(61, Locale.ENGLISH));
        }

        @Test
        @DisplayName("should return String with text '2 minutes' when cool down time is 2 minutes")
        void shouldReturnStringWithText2MinutesWhenCoolDownTimeIs2Minutes() {
            when(messageSource.getMessage("coolDown.minutes", new Integer[] { 2 }, Locale.ENGLISH)).thenReturn("2 minutes");
            assertEquals("2 minutes", cooldownTextFormatter.format(120, Locale.ENGLISH));
        }

        @Test
        @DisplayName("should return String with text '1 hour' when cool down time is 1 hour")
        void shouldReturnStringWithText1HourWhenCoolDownTimeIs1Hour() {
            when(messageSource.getMessage("coolDown.hour", new Integer[] { 1 }, Locale.ENGLISH)).thenReturn("1 hour");
            assertEquals("1 hour", cooldownTextFormatter.format(3600, Locale.ENGLISH));
        }

        @Test
        @DisplayName("should return String with text '2 hours' when cool down time is 2 hour")
        void shouldReturnStringWithText2HourWhenCoolDownTimeIs2Hour() {
            when(messageSource.getMessage("coolDown.hours", new Integer[] { 2 }, Locale.ENGLISH)).thenReturn("2 hours");
            assertEquals("2 hours", cooldownTextFormatter.format(7200, Locale.ENGLISH));
        }

        @Test
        @DisplayName("should return String with text '1 hour, 1 minute and 1 second' when cool down time is 1 hour, 1 minute and 1 second")
        void shouldReturnStringWithText1Hour1Minute1SecondWhenCoolDownTimeIs1Hour1Minute1Second() {
            when(messageSource.getMessage("coolDown.hour", new Integer[] { 1 }, Locale.ENGLISH)).thenReturn("1 hour");
            when(messageSource.getMessage("coolDown.minute", new Integer[] { 1 }, Locale.ENGLISH)).thenReturn("1 minute");
            when(messageSource.getMessage("coolDown.second", new Integer[] { 1 }, Locale.ENGLISH)).thenReturn("1 second");
            when(messageSource.getMessage("coolDown.and", null, Locale.ENGLISH)).thenReturn("and");
            assertEquals("1 hour, 1 minute and 1 second", cooldownTextFormatter.format(3661, Locale.ENGLISH));
        }

        @Test
        @DisplayName("should return String with text '2 hours and 2 seconds' when cool down time is 2 hour and 2 seconds")
        void shouldReturnStringWithText2HoursAnd2SecondsWhenCoolDownTimeIs2HoursAnd2Seconds() {
            when(messageSource.getMessage("coolDown.hours", new Integer[] { 2 }, Locale.ENGLISH)).thenReturn("2 hours");
            when(messageSource.getMessage("coolDown.seconds", new Integer[] { 2 }, Locale.ENGLISH)).thenReturn("2 seconds");
            when(messageSource.getMessage("coolDown.and", null, Locale.ENGLISH)).thenReturn("and");
            assertEquals("2 hours and 2 seconds", cooldownTextFormatter.format(7202, Locale.ENGLISH));
        }
    }
}
