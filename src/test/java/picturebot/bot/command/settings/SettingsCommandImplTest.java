package picturebot.bot.command.settings;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import picturebot.bot.factory.SettingsReplyKeyboardMarkupFactory;
import picturebot.bot.user.SettingsMessageIdSaver;
import picturebot.entities.botuser.BotUser;
import picturebot.entities.settings.Settings;
import picturebot.fixtures.UpdateFixture;
import picturebot.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class SettingsCommandImplTest {

    @Mock private AbsSender bot;
    @Mock private UserRepository userRepository;
    @Mock private BotUser botUser;
    @Mock private Settings settings;
    @Mock private UrlCreator urlCreator;
    @Mock private SettingsReplyKeyboardMarkupFactory settingsReplyKeyboardMarkupFactory;
    @Mock private ReplyKeyboardMarkup replyKeyboardMarkup;
    @Mock private SendMessageCreator sendMessageCreator;
    @Mock private SendMessage sendMessage;
    @Mock private Message message;
    @Mock private SettingsMessageIdSaver settingsMessageIdSaver;

    @InjectMocks private SettingsCommandImpl command;

    @Nested
    @DisplayName("respond()")
    class GetResponse {

        @Test
        @DisplayName("should respond with SendMessage that contains custom keyboard")
        void shouldRespondWithSendMessageThatContainsCustomKeyboard() throws TelegramApiException {
            final Update update = UpdateFixture.createBasicUpdate("en");

            when(userRepository.findById(6L)).thenReturn(Optional.of(botUser));
            when(botUser.getSettings()).thenReturn(settings);
            when(urlCreator.create(settings, Locale.ENGLISH)).thenReturn("http://localhost");
            when(settingsReplyKeyboardMarkupFactory.create("http://localhost", Locale.ENGLISH))
                    .thenReturn(replyKeyboardMarkup);
            when(botUser.getId()).thenReturn(6L);
            when(sendMessageCreator.create(6L, settings, replyKeyboardMarkup, Locale.ENGLISH))
                    .thenReturn(sendMessage);
            when(bot.execute(sendMessage)).thenReturn(message);
            when(message.getMessageId()).thenReturn(1000);

            // Act:
            command.respond(bot, update);

            verify(userRepository).findById(6L);
            verify(urlCreator).create(settings, Locale.ENGLISH);
            verify(settingsReplyKeyboardMarkupFactory).create("http://localhost", Locale.ENGLISH);
            verify(sendMessageCreator).create(eq(6L), eq(settings), eq(replyKeyboardMarkup), eq(Locale.ENGLISH));
            verify(bot).execute(sendMessage);
            verify(settingsMessageIdSaver).saveSettingsMessageId(6L, 1000);
        }
    }
}
