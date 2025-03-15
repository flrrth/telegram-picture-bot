package picturebot.bot.command.help;

import picturebot.DailyPictureBotTestConfiguration;
import picturebot.FlywayTestExecutionListener;
import picturebot.emoticons.Emoticons;
import picturebot.fixtures.UpdateFixture;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Tag("integration")
@Import(DailyPictureBotTestConfiguration.class)
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class
})
public class HelpCommandImplApplicationTest {

    @Autowired private HelpCommandImpl helpCommandImpl;
    @Autowired private AbsSender bot;

    @Captor private ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    @BeforeEach
    void resetContainerInjectedMocks() {
        reset(bot);
    }

    @Nested
    @DisplayName("respond()")
    class Respond {

        @Test
        @DisplayName("should respond with the English help text")
        void shouldReturnWithTheEnglishHelpResponseText() throws TelegramApiException {
            final Update update = UpdateFixture.createTextUpdate("en", "/help");

            // Act:
            helpCommandImpl.respond(bot, update);

            verify(bot).execute(sendMessageArgumentCaptor.capture());
            final SendMessage sendMessage = sendMessageArgumentCaptor.getValue();
            assertEquals("6", sendMessage.getChatId());
            assertEquals(String.format("Use the menu button to access my options. %c",
                    Emoticons.WHITE_DOWN_POINTING_BACKHAND_INDEX), sendMessage.getText());
        }

        @Test
        @DisplayName("should respond with the Dutch help text")
        void shouldReturnWithTheDutchHelpResponseText() throws TelegramApiException {
            final Update update = UpdateFixture.createTextUpdate("nl", "/help");

            // Act:
            helpCommandImpl.respond(bot, update);

            verify(bot).execute(sendMessageArgumentCaptor.capture());
            final SendMessage sendMessage = sendMessageArgumentCaptor.getValue();
            assertEquals("6", sendMessage.getChatId());
            assertEquals(String.format("Gebruik de menuknop om toegang tot mijn opties te krijgen. %c",
                    Emoticons.WHITE_DOWN_POINTING_BACKHAND_INDEX), sendMessage.getText());
        }
    }
}
