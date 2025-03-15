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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class SendMessageFactoryImplTest {

    @Mock private MessageSource messageSource;
    @Mock private CooldownTextFormatter cooldownTextFormatter;

    @InjectMocks private SendMessageFactoryImpl factory;

    @Nested
    @DisplayName("getSendMessage()")
    class GetSendMessage {

        @Test
        @DisplayName("should return SendMessage instance")
        void shouldReturnSendMessageInstance() {
            assertInstanceOf(SendMessage.class, factory.getSendMessage());
        }
    }

    @Nested
    @DisplayName("getDefaultErrorMessage()")
    class GetDefaultErrorMessage {

        @Test
        @DisplayName("should return SendMessage configured for bot user with default error text")
        void shouldReturnSendMessageConfiguredForBotUserWithDefaultErrorText() {
            when(messageSource.getMessage("error", null, Locale.ENGLISH))
                    .thenReturn("%c Sorry, something went wrong.");

            final SendMessage actual = factory.getDefaultErrorMessage(1L, Locale.ENGLISH);

            final SendMessage expected = new SendMessage("1", "\uD83D\uDE30 Sorry, something went wrong.");
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("getNoPictureMessage()")
    class GetNoPictureMessage {
        @Test
        @DisplayName("should return SendMessage configured for bot user with 'no pictures' text")
        void shouldReturnSendMessageConfiguredForBotUserWithNoPicturesText() {
            when(messageSource.getMessage("noPictures", null, Locale.ENGLISH))
                    .thenReturn("%c Sorry, I ran out of photos.");

            final SendMessage actual = factory.getNoPictureMessage(1L, Locale.ENGLISH);

            final SendMessage expected = new SendMessage("1", "\uD83D\uDE30 Sorry, I ran out of photos.");
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("getHelpMessage()")
    class GetHelpMessage {

        @Test
        @DisplayName("should return SendMessage configured for bot user with 'help' text")
        void shouldReturnSendMessageConfiguredForBotUserWithHelpText() {
            when(messageSource.getMessage("help", null, Locale.ENGLISH))
                    .thenReturn("Help text. %c");

            final SendMessage actual = factory.getHelpMessage(1L, Locale.ENGLISH);

            final SendMessage expected = new SendMessage("1", "Help text. \uD83D\uDC47");
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("getStatsMessage()")
    class GetStatsMessage {

        @Test
        @DisplayName("should return SendMessage configured for bot user with statistics")
        void shouldReturnSendMessageConfiguredForBotUserWithHelpText() {
            when(messageSource.getMessage("stats.pictureCount", new String[] { "100" }, Locale.ENGLISH))
                    .thenReturn("%c I have 100 pictures available.");

            final SendMessage actual = factory.getStatsMessage(1L, Locale.ENGLISH, new String[]{ "100" });

            final SendMessage expected = new SendMessage("1", "\uD83C\uDFA8 I have 100 pictures available.");
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("getDefaultMessage()")
    class GetDefaultMessage {

        @Test
        @DisplayName("should return SendMessage configured for bot user with the default text")
        void shouldReturnSendMessageConfiguredForBotUserWithTheDefaultText() {
            when(messageSource.getMessage("default", new String[]{ "/picture" }, Locale.ENGLISH))
                    .thenReturn("""
                            %c Sorry, I don't know what you mean. Only the following commands are accepted:
                            
                            %c /settings to configure the bot,
                            
                            %c /picture to get a picture,
                            
                            %c /stats to display statistics about the bot and
                            
                            %c /version to display the bot version.""");

            final SendMessage actual = factory.getDefaultMessage(1L, Locale.ENGLISH, new String[]{ "/picture" });

            final SendMessage expected = new SendMessage("1", """
                    😰 Sorry, I don't know what you mean. Only the following commands are accepted:

                    👉 /settings to configure the bot,

                    👉 /picture to get a picture,

                    👉 /stats to display statistics about the bot and

                    👉 /version to display the bot version.""");
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("getStartMessage()")
    class GetStartMessage {

        @Test
        @DisplayName("should return SendMessage configured for bot user with a greeting")
        void shouldReturnSendMessageConfiguredForBotUserWithGreeting() {
            when(messageSource.getMessage("start", new String[] { "John", "/picture" }, Locale.ENGLISH))
                    .thenReturn("""
                            %c Hi John! Please configure the bot via the Settings option in the menu, or type
                            
                            %c /settings to configure the bot,
                            
                            %c /picture to get a picture!""");

            final SendMessage actual = factory.getStartMessage(1L, Locale.ENGLISH,
                    new String[]{ "John", "/picture" });

            final SendMessage expected = new SendMessage("1", """
                    👋 Hi John! Please configure the bot via the Settings option in the menu, or type
                    
                    👉 /settings to configure the bot,
                    
                    👉 /picture to get a picture!""");

            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("getCoolDownMessage()")
    class GetCoolDownMessage {

        @Test
        @DisplayName("should return SendMessage configured for bot user with cool down text")
        void shouldReturnSendMessageConfiguredForBotUserWithCoolDownText() {
            when(messageSource.getMessage("coolDown", new String[]{ "4 minutes and 59 seconds" }, Locale.ENGLISH))
                    .thenReturn("%c You can request a new picture in 4 minutes and 59 seconds.");
            when(cooldownTextFormatter.format(299L, Locale.ENGLISH))
                    .thenReturn("4 minutes and 59 seconds");

            final SendMessage actual = factory.getCoolDownMessage(1L, Locale.ENGLISH, 299L);

            final SendMessage expected = new SendMessage("1",
                    "✋ You can request a new picture in 4 minutes and 59 seconds.");
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("GetPhotoUploadConfirmationMessage()")
    class GetPhotoUploadConfirmationMessage {

        @Test
        @DisplayName("should return SendMessage configured for bot user with thanks")
        void shouldReturnSendMessageConfiguredForBotUserWithThanks() {
            when(messageSource.getMessage("upload.thanks", null, Locale.ENGLISH))
                    .thenReturn("Thanks! %c");

            final SendMessage actual = factory.getUploadConfirmationMessage(1L, Locale.ENGLISH);

            final SendMessage expected = new SendMessage("1",
                    "Thanks! ❤");
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("getVersionMessage()")
    class GetVersionMessage {

        @Test
        @DisplayName("should return SendMessage with the application version")
        void shouldReturnSendMessageWithApplicationVersion() {
            when(messageSource.getMessage("version", new String[] { "1.0.0" }, Locale.ENGLISH))
                    .thenReturn("%c Daily Picture Bot version 1.0.0.");

            final SendMessage actual = factory.getVersionMessage(1L, Locale.ENGLISH, new String[]{ "1.0.0" });

            final SendMessage expected = new SendMessage("1",
                    "\uD83D\uDE80 Daily Picture Bot version 1.0.0.");
            assertEquals(expected, actual);
        }
    }
}
