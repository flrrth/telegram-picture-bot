package picturebot.bot.command;

import picturebot.DailyPictureBotTestConfiguration;
import picturebot.FlywayTestExecutionListener;
import picturebot.fixtures.UpdateFixture;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@Tag("integration")
@Import(DailyPictureBotTestConfiguration.class)
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class
})
public class DefaultCommandImplApplicationTest {

    @Autowired private DefaultCommandImpl defaultCommandImpl;
    @Autowired private TelegramClient telegramClient;

    @Captor private ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    @BeforeEach
    void resetContainerInjectedMocks() {
        reset(bot);
    }

    @Nested
    @DisplayName("respond()")
    class Respond {

        @Test
        @DisplayName("should respond with the English default response text")
        void shouldReturnWithTheEnglishDefaultResponseText() throws TelegramApiException {
            final Update update = UpdateFixture.createTextUpdate("en", "Hello World");

            // Act:
            defaultCommandImpl.respond(bot, update);

            verify(bot).execute(sendMessageArgumentCaptor.capture());
            final SendMessage sendMessage = sendMessageArgumentCaptor.getValue();
            assertEquals("6", sendMessage.getChatId());
            assertEquals("""
                    😰 Sorry, I don't know what you mean. Only the following commands are accepted:
                    
                    👉 /settings to configure the bot,
                    
                    👉 /random to get a picture,
                    
                    👉 /stats to display statistics about the bot and
                    
                    👉 /version to display the bot version.""",
                    sendMessage.getText());
        }

        @Test
        @DisplayName("should respond with the Dutch default response text")
        void shouldReturnWithTheDutchDefaultResponseText(final CapturedOutput output) throws TelegramApiException {
            final Update update = UpdateFixture.createTextUpdate("nl", "Hello World");

            // Act:
            defaultCommandImpl.respond(bot, update);

            verify(bot).execute(sendMessageArgumentCaptor.capture());
            final SendMessage sendMessage = sendMessageArgumentCaptor.getValue();
            assertEquals("6", sendMessage.getChatId());
            assertEquals("""
                    😰 Sorry, ik begrijp niet wat je bedoelt. Alleen de volgende commando's worden geaccepteerd:
                    
                    👉 /settings om de bot te configureren,
                    
                    👉 /random om een plaatje te ontvangen,
                    
                    👉 /stats om statistieken over de bot te tonen en
                    
                    👉 /version om de bot-versie te tonen.""", sendMessage.getText());
            assertThat(output.getOut(), containsString("INFO  - User 6 says 'Hello World'"));
        }
    }
}
