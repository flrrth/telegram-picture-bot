package picturebot.bot.command.settings;

import picturebot.bot.factory.SendMessageFactory;
import picturebot.entities.settings.Settings;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class SendMessageCreatorImplTest {

    private static final LocalDateTime NOON = LocalDateTime.of(2024, 1, 2, 12, 0);
    private final Clock fixedClock = Clock.fixed(NOON.atZone(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault());

    @Mock private MessageSource messageSource;
    @Mock private SendMessageFactory sendMessageFactory;
    @Mock private ReplyKeyboardMarkup replyKeyboardMarkup;
    @Mock private SendMessage sendMessage;
    @Mock private Settings settings;

    @InjectMocks private SendMessageCreatorImpl sendMessageCreator;

    @Nested
    @DisplayName("create()")
    class Create {

        @BeforeEach
        void setUp() {
            when(sendMessageFactory.getSendMessage()).thenReturn(sendMessage);
        }

        @Test
        @DisplayName("should return 'No settings' SendMessage when no settings are available")
        void shouldReturnNoSettingsSendMessageWhenNoSettingsAreAvailable() {
            when(messageSource.getMessage(eq("settings.absent"), isNull(), any()))
                    .thenReturn("No settings");

            final SendMessage sendMessage = sendMessageCreator
                    .create(1L, null, replyKeyboardMarkup, Locale.ENGLISH);

            assertThat(sendMessage, is(notNullValue()));
            verify(sendMessage).setChatId(1L);
            verify(sendMessage).setReplyMarkup(replyKeyboardMarkup);
            verify(sendMessage).setText("No settings");
        }

        @Test
        @DisplayName("should return settings SendMessage with enabled spoiler")
        void shouldReturnSettingsSendMessageWithEnabledSpoiler() {
            when(settings.getIsEnabled()).thenReturn(true);
            when(settings.getSchedule()).thenReturn(LocalTime.now(fixedClock));
            when(settings.getSpoilerEnabled()).thenReturn(true);
            when(messageSource.getMessage(eq("settings.yes"), isNull(), any())).thenReturn("yes");
            when(messageSource.getMessage(eq("settings.enabled"), ArgumentMatchers.any(String[].class), any()))
                    .thenReturn("Scheduler: {0}, spoiler enabled: {1}.");

            final SendMessage sendMessage = sendMessageCreator
                    .create(1L, settings, replyKeyboardMarkup, Locale.ENGLISH);

            assertThat(sendMessage, is(notNullValue()));
            verify(sendMessage).setChatId(1L);
            verify(sendMessage).setReplyMarkup(replyKeyboardMarkup);
            verify(messageSource).getMessage("settings.yes", null, Locale.ENGLISH);
            verify(messageSource).getMessage("settings.enabled", new String[] { "12:00", "yes" }, Locale.ENGLISH);
            verify(sendMessage).setText(anyString());
        }

        @Test
        @DisplayName("should return settings SendMessage with disabled spoiler")
        void shouldReturnSettingsSendMessageWithDisabledSpoiler() {
            when(settings.getIsEnabled()).thenReturn(true);
            when(settings.getSchedule()).thenReturn(LocalTime.now(fixedClock));
            when(settings.getSpoilerEnabled()).thenReturn(false);
            when(messageSource.getMessage(eq("settings.no"), isNull(), any())).thenReturn("no");
            when(messageSource.getMessage(eq("settings.enabled"), ArgumentMatchers.any(String[].class), any()))
                    .thenReturn("Scheduler: {0}, spoiler enabled: {1}.");

            final SendMessage sendMessage = sendMessageCreator
                    .create(1L, settings, replyKeyboardMarkup, Locale.ENGLISH);

            assertThat(sendMessage, is(notNullValue()));
            verify(sendMessage).setChatId(1L);
            verify(sendMessage).setReplyMarkup(replyKeyboardMarkup);
            verify(messageSource).getMessage("settings.no", null, Locale.ENGLISH);
            verify(messageSource).getMessage("settings.enabled", new String[] { "12:00", "no" }, Locale.ENGLISH);
            verify(sendMessage).setText(anyString());
        }

        @Test
        @DisplayName("should return 'Scheduler disabled' SendMessage when no settings are available")
        void shouldReturnSchedulerDisabledSendMessageWhenSchedulerIsDisabled() {
            when(settings.getIsEnabled()).thenReturn(false);
            when(messageSource.getMessage(eq("settings.disabled"), isNull(), any()))
                    .thenReturn("Scheduler disabled");

            final SendMessage sendMessage = sendMessageCreator
                    .create(1L, settings, replyKeyboardMarkup, Locale.ENGLISH);

            assertThat(sendMessage, is(notNullValue()));
            verify(sendMessage).setChatId(1L);
            verify(sendMessage).setReplyMarkup(replyKeyboardMarkup);
            verify(sendMessage).setText("Scheduler disabled");
        }
    }
}
