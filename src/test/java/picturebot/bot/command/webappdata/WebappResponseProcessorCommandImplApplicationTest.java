package picturebot.bot.command.webappdata;

import picturebot.DailyPictureBotTestConfiguration;
import picturebot.FlywayTestExecutionListener;
import picturebot.emoticons.Emoticons;
import picturebot.entities.botuser.BotUser;
import picturebot.fixtures.UpdateFixture;
import picturebot.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppData;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@Tag("integration")
@Import(DailyPictureBotTestConfiguration.class)
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class
})
public class WebappResponseProcessorCommandImplApplicationTest {

    @Autowired private WebappResponseProcessorCommandImpl webappResponseProcessor;
    @Autowired private UserRepository userRepository;

    @Captor private ArgumentCaptor<DeleteMessage> deleteMessageArgumentCaptor;
    @Captor private ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    @Nested
    @DisplayName("respond()")
    class Respond {

        @Test
        @DisplayName("should update the user settings with the values received from the web application")
        void shouldUpdateTheUserSettingsWithTheValuesReceivedFromTheWebApplication() throws TelegramApiException {
            final AbsSender bot = Mockito.mock(AbsSender.class);
            final Update update = UpdateFixture.createBasicUpdate("en");
            update.getMessage().setWebAppData(new WebAppData());
            update.getMessage().getWebAppData().setData("""
                    {
                        "isEnabled": true,
                        "isSpoilerEnabled": true,
                        "schedule": "07:01:00",
                        "timezone": 2
                    }
                    """);

            // Act:
            webappResponseProcessor.respond(bot, update);

            // Verify if the new user settings were stored correctly:
            final BotUser john = userRepository.findById(6L).orElseThrow();
            assertNotNull(john.getSettings());
            assertTrue(john.getSettings().getIsEnabled());
            assertTrue(john.getSettings().getSpoilerEnabled());
            assertEquals(LocalTime.of(7, 1, 0), john.getSettings().getSchedule());
            assertEquals(2, john.getSettings().getTimezone().getId());

            // Verify that the bot send a DeleteMessage to Telegram that cleans up the keyboard:
            verify(bot).execute(deleteMessageArgumentCaptor.capture());
            final DeleteMessage deleteMessage = deleteMessageArgumentCaptor.getValue();
            assertEquals("6", deleteMessage.getChatId());
            assertEquals(1234, deleteMessage.getMessageId());
        }

        @Test
        @DisplayName("should respond with an error message when the data received from the web application is invalid")
        void shouldRespondWithAnErrorMessageWhenTheDataReceivedFromTheWebApplicationIsInvalid(final CapturedOutput output) throws TelegramApiException {
            final AbsSender bot = Mockito.mock(AbsSender.class);
            final Update update = UpdateFixture.createBasicUpdate("en");
            update.getMessage().setWebAppData(new WebAppData());
            update.getMessage().getWebAppData().setData("invalid data");

            // Act:
            webappResponseProcessor.respond(bot, update);

            verify(bot).execute(sendMessageArgumentCaptor.capture());
            final SendMessage sendMessage = sendMessageArgumentCaptor.getValue();
            assertEquals("6", sendMessage.getChatId());
            assertEquals(String.format("%c Sorry, something went wrong.", Emoticons.FACE_WITH_OPEN_MOUTH_AND_COLD_SWEAT), sendMessage.getText());
            assertThat(output.getOut(), containsString("ERROR - Could not parse webapp data."));
        }
    }
}
