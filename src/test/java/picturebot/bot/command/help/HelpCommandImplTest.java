package picturebot.bot.command.help;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import picturebot.bot.factory.SendMessageFactory;
import picturebot.fixtures.UpdateFixture;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class HelpCommandImplTest {

    @Mock private SendMessageFactory sendMessageFactory;
    @Mock private SendMessage sendMessage;
    @Mock private AbsSender bot;

    @InjectMocks private HelpCommandImpl helpCommandImpl;

    @Nested
    @DisplayName("respond()")
    class GetResponse {

        @Test
        @DisplayName("should respond with SendMessage with help text")
        void shouldRespondWithSendMessageWithHelpText() throws TelegramApiException {
            final Update update = UpdateFixture.createTextUpdate("en", "/help");
            when(sendMessageFactory.getHelpMessage(6L, Locale.ENGLISH)).thenReturn(sendMessage);

            helpCommandImpl.respond(bot, update);

            verify(bot).execute(sendMessage);
        }
    }
}
