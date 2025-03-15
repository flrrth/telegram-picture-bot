package picturebot.bot.command.start;

import picturebot.bot.factory.SendMessageFactory;
import picturebot.fixtures.UpdateFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class StartCommandImplTest {

    @Mock private SendMessageFactory sendMessageFactory;
    @Mock private SendMessage sendMessage;
    @Mock private AbsSender bot;
    @Mock private Environment environment;

    @InjectMocks private StartCommandImpl startCommand;

    @Nested
    @DisplayName("respond()")
    class GetResponse {

        @Test
        @DisplayName("should respond with personalised greeting")
        void shouldRespondWithPersonalisedGreeting() throws TelegramApiException {
            final Update update = UpdateFixture.createTextUpdate("en", "/random");

            when(sendMessageFactory.getStartMessage(6L, Locale.ENGLISH, new String[] { "John", "/random" }))
                    .thenReturn(sendMessage);
            when(environment.getRequiredProperty("bot.randomCommand")).thenReturn("/random");

            // Act:
            startCommand.respond(bot, update);

            verify(sendMessageFactory).getStartMessage(eq(6L), eq(Locale.ENGLISH),
                    eq(new String[] { "John", "/random" }));
            verify(bot).execute(sendMessage);
        }
    }
}
