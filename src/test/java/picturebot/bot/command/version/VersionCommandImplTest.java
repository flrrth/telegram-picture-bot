package picturebot.bot.command.version;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class VersionCommandImplTest {

    @Mock private SendMessageFactory sendMessageFactory;
    @Mock private SendMessage sendMessage;
    @Mock private AbsSender bot;

    private final String version = "";
    private VersionCommandImpl versionCommand;

    @Nested
    @DisplayName("respond()")
    class GetResponse {

        @BeforeEach
        void setUp() {
            versionCommand = new VersionCommandImpl(sendMessageFactory, version);
        }

        @Test
        @DisplayName("should respond with SendMessage with help text")
        void shouldRespondWithSendMessageWithHelpText() throws TelegramApiException {
            final Update update = UpdateFixture.createBasicUpdate("en");
            
            when(sendMessageFactory.getVersionMessage(6L, Locale.ENGLISH, new String[]{ version })).thenReturn(sendMessage);

            // Act:
            versionCommand.respond(bot, update);

            verify(bot).execute(sendMessage);
        }
    }
}
