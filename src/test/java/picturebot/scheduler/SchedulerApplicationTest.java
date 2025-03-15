package picturebot.scheduler;

import picturebot.bot.command.ScheduledCommand;
import picturebot.entities.botuser.BotUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.TelegramBotInitializer;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.mockito.Mockito.verify;

@SpringBootTest
@Tag("integration")
public class SchedulerApplicationTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public Clock fixedClock() {
            final LocalDateTime SEVEN_IN_THE_MORNING = LocalDateTime.of(2024, 1, 2, 7, 0);
            return Clock.fixed(SEVEN_IN_THE_MORNING.atZone(ZoneId.of("Europe/Amsterdam")).toInstant(),
                    ZoneId.of("Europe/Amsterdam"));
        }
    }

    @MockBean private TelegramBotInitializer telegramBotInitializer;
    @MockBean private TelegramLongPollingBot bot;
    @MockBean private ScheduledCommand command;

    @Autowired private Scheduler scheduler;

    @Nested
    @DisplayName("executeJob()")
    class ExecuteJob {

        @Test
        @DisplayName("should send daily picture to user 'Scheduler'")
        void shouldSendDailyPictureToUserAmsterdam() throws TelegramApiException {
            scheduler.executeJob();
            verify(command).send(bot, new BotUser(5L, LocalDateTime.now(), LocalDateTime.now()));
        }
    }
}
