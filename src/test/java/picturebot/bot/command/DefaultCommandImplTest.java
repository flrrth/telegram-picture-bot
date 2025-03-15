package picturebot.bot.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import picturebot.bot.factory.SendMessageFactory;
import picturebot.fixtures.UpdateFixture;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@Tag("unit")
class DefaultCommandImplTest {

    @Mock private SendMessageFactory sendMessageFactory;
    @Mock private SendMessage sendMessage;
    @Mock private AbsSender bot;
    @Mock private Environment environment;

    @InjectMocks private DefaultCommandImpl command;

    @Nested
    @DisplayName("respond()")
    class GetResponse {

        @Test
        @DisplayName("should respond with a SendMessage with the default response text")
        void shouldReturnWithSendMessageWithTheDefaultResponseText(final CapturedOutput output) throws TelegramApiException {
            final Update update = UpdateFixture.createTextUpdate("en", "Hello World");

            when(environment.getRequiredProperty("bot.randomCommand")).thenReturn("/random");
            when(sendMessageFactory.getDefaultMessage(eq(6L), eq(Locale.ENGLISH),
                    eq(new String[] { "/random" }))).thenReturn(sendMessage);

            // Act:
            command.respond(bot, update);

            verify(sendMessageFactory).getDefaultMessage(eq(6L), eq(Locale.ENGLISH),
                    eq(new String[] { "/random" }));
            verify(bot).execute(sendMessage);
            assertThat(output.getOut(), containsString("INFO  - User 6 says 'Hello World'"));
        }
    }
}
