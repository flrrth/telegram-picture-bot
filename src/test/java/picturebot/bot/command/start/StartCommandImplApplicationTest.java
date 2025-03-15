package picturebot.bot.command.start;

import picturebot.DailyPictureBotTestConfiguration;
import picturebot.bot.factory.GetUserProfilePhotosFactory;
import picturebot.fixtures.UpdateFixture;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.UserProfilePhotos;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@Tag("integration")
@Import(DailyPictureBotTestConfiguration.class)
public class StartCommandImplApplicationTest {

    @Autowired private StartCommandImpl startCommand;
    @Autowired private AbsSender bot;

    @MockBean private GetUserProfilePhotosFactory getUserProfilePhotosFactory;
    
    @Mock private GetUserProfilePhotos getUserProfilePhotos;
    @Mock private UserProfilePhotos userProfilePhotos;

    @Captor private ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    @BeforeEach
    void resetContainerInjectedMocks() {
        reset(bot);
    }

    @Nested
    @DisplayName("respond()")
    class Respond {

        @BeforeEach
        void configureUserProfilePhotos() throws TelegramApiException {
            when(getUserProfilePhotosFactory.createGetUserProfilePhotos(6L)).thenReturn(getUserProfilePhotos);
            when(bot.execute(getUserProfilePhotos)).thenReturn(userProfilePhotos);
            when(userProfilePhotos.getPhotos()).thenReturn(List.of());            
        }

        @Test
        @DisplayName("should respond with personalised English greeting")
        void shouldRespondWithPersonalisedEnglishGreeting() throws TelegramApiException {
            final Update update = UpdateFixture.createTextUpdate("en", "/picture");
            
            // Act:
            startCommand.respond(bot, update);

            verifyStartCommandResponse("""
                    👋 Hi John! Please configure the bot via the Settings option in the menu, or type
                    
                    👉 /settings to configure the bot,
                    
                    👉 /random to get a picture!""");
        }

        @Test
        @DisplayName("should respond with personalised Dutch greeting")
        void shouldRespondWithPersonalisedDutchGreeting() throws TelegramApiException {
            final Update update = UpdateFixture.createTextUpdate("nl", "/picture");

            // Act:
            startCommand.respond(bot, update);

            verifyStartCommandResponse("""
                    👋 Hoi John! Configureer de bot via de Settings-optie in het menu, of typ
                    
                    👉 /settings om de bot te configureren,
                    
                    👉 /random om een plaatje te ontvangen!""");
        }

        @Test
        @DisplayName("should respond with personalised Russian greeting")
        void shouldRespondWithPersonalisedRussianGreeting() throws TelegramApiException {
            final Update update = UpdateFixture.createTextUpdate("ru", "/picture");

            // Act:
            startCommand.respond(bot, update);

            verifyStartCommandResponse("""
                    👋 Привет John! Пожалуйста, настройте бота через опцию «Settings» в меню или введите
                    
                    👉 /settings для настройки бота,
                    
                    👉 /random, чтобы получить изображение!""");
        }

        private void verifyStartCommandResponse(final String expectedMessage) throws TelegramApiException {
            verify(bot, times(2)).execute(sendMessageArgumentCaptor.capture());
            final List<SendMessage> sendMessages = sendMessageArgumentCaptor.getAllValues();
            assertEquals(2, sendMessages.size());
            
            final SendMessage sendMessage = sendMessages.get(0);
            assertEquals("6", sendMessage.getChatId());
            assertEquals(expectedMessage, sendMessage.getText());
        }
    }
}
