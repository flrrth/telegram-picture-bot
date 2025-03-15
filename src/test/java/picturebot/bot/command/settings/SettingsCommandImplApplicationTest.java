package picturebot.bot.command.settings;

import picturebot.DailyPictureBotTestConfiguration;
import picturebot.entities.botuser.BotUser;
import picturebot.fixtures.UpdateFixture;
import picturebot.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@Tag("integration")
@Import(DailyPictureBotTestConfiguration.class)
public class SettingsCommandImplApplicationTest {

    @Autowired private SettingsCommandImpl settingsCommand;
    @Autowired private UserRepository userRepository;
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
        @DisplayName("should respond with message with custom keyboard that opens the web application and update the database")
        void shouldRespondWithMessageWithCustomKeyboardThatOpensTheWebApplicationAndUpdateTheDatabase() throws TelegramApiException {
            final Update update = UpdateFixture.createBasicUpdate("nl");
            final Message message = Mockito.mock(Message.class);
            when(bot.execute(any(SendMessage.class))).thenReturn(message);
            when(message.getMessageId()).thenReturn(1235);

            settingsCommand.respond(bot, update);

            // Check the contents of the response sent to Telegram:
            verify(bot).execute(sendMessageArgumentCaptor.capture());
            final SendMessage sendMessage = sendMessageArgumentCaptor.getValue();
            final ReplyKeyboardMarkup replyKeyboardMarkup = (ReplyKeyboardMarkup) sendMessage.getReplyMarkup();
            final String buttonText = replyKeyboardMarkup.getKeyboard().get(0).get(0).getText();
            final WebAppInfo webAppInfo = replyKeyboardMarkup.getKeyboard().get(0).get(0).getWebApp();
            assertEquals("Instellingen", buttonText);
            assertEquals("https://localhost:443/page/index.html?language=nl&isEnabled=false&isSpoilerEnabled=false&schedule=07:00&timezone=1", webAppInfo.getUrl());

            // Check if the database was updated properly:
            final BotUser botUser = userRepository.findById(6L).orElseThrow();
            assertEquals(1235, botUser.getSettingsMessageId());
        }
    }
}
