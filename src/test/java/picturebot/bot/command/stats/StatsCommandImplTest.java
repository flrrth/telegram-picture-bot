package picturebot.bot.command.stats;

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
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import picturebot.bot.factory.SendMessageFactory;
import picturebot.fixtures.UpdateFixture;
import picturebot.picture.counter.PictureCounter;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class StatsCommandImplTest {

    // The class-under-test and its dependencies:
    @Mock private Environment environment;
    @Mock private SendMessageFactory sendMessageFactory;
    @Mock private PictureCounter pictureCounter;
    @InjectMocks private StatsCommandImpl command;

    // Other mocks:
    @Mock private SendMessage sendMessage;
    @Mock private AbsSender bot;

    @Nested
    @DisplayName("respond()")
    class GetResponse {

        @Test
        @DisplayName("should respond with SendMessage that shows the total number of available pictures")
        void shouldRespondWithSendMessageThatShowsTheTotalNumberOfAvailablePictures() throws TelegramApiException {
            final Update update = UpdateFixture.createBasicUpdate("en");
            
            when(environment.getRequiredProperty("bot.regular.subPath")).thenReturn("regular");
            when(pictureCounter.count("regular")).thenReturn(1);
            when(sendMessageFactory.getStatsMessage(eq(6L), eq(Locale.ENGLISH), eq(new String[]{ "1" })))
                    .thenReturn(sendMessage);

            // Act:
            command.respond(bot, update);

            verify(bot).execute(sendMessage);
        }
    }
}
