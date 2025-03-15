package picturebot.scheduler;

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
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import picturebot.bot.command.ScheduledCommand;
import picturebot.entities.botuser.BotUser;
import picturebot.entities.timezone.Timezone;
import picturebot.repositories.TimezoneRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@Tag("unit")
class SchedulerTest {

    @Mock private ScheduledCommand command;
    @Mock TimezoneRepository timezoneRepository;
    @Mock TelegramLongPollingBot bot = mock(TelegramLongPollingBot.class);
    @Mock SchedulerService schedulerService;
    @Mock Timezone timezone;
    @Mock BotUser botUser;

    @InjectMocks private Scheduler scheduler;

    @Nested
    @DisplayName("executeJob()")
    class ExecuteJob {

        @Test
        @DisplayName("should not send any message, because no timezones are found")
        void shouldNotSendAnyMessageBecauseNoTimezonesAreFound() {
            when(timezoneRepository.findAll()).thenReturn(List.of());

            scheduler.executeJob();

            verifyNoInteractions(schedulerService);
            verifyNoInteractions(bot);
        }

        @Test
        @DisplayName("should not send any message, because no BotUsers are found")
        void shouldNotSendAnyMessageBecauseNoBotUsersAreFound() {
            when(timezoneRepository.findAll()).thenReturn(List.of(timezone));
            when(schedulerService.getUsers(timezone)).thenReturn(List.of());

            scheduler.executeJob();

            verify(schedulerService).getUsers(timezone);
            verifyNoInteractions(bot);
        }

        @Test
        @DisplayName("should send photo, because BotUser was found")
        void shouldSendPhotoBecauseBotUserWasFound(final CapturedOutput output) throws TelegramApiException {
            when(timezoneRepository.findAll()).thenReturn(List.of(timezone));
            when(schedulerService.getUsers(timezone)).thenReturn(List.of(botUser));
            when(botUser.getId()).thenReturn(1L);

            scheduler.executeJob();

            verify(command).send(bot, botUser);
            assertThat(output.getOut(), containsString("INFO  - Sending daily picture to user 1."));
        }

        @Test
        @DisplayName("should log error, because an error occurred while sending photo")
        void shouldLogErrorBecauseAnErrorOccurredWhileSendingPhoto(final CapturedOutput output)
                throws TelegramApiException {

            when(timezoneRepository.findAll()).thenReturn(List.of(timezone));
            when(schedulerService.getUsers(timezone)).thenReturn(List.of(botUser));
            when(botUser.getId()).thenReturn(1L);
            doThrow(new TelegramApiException("Mocked exception")).when(command).send(bot, botUser);

            scheduler.executeJob();

            assertThat(output.getOut(), containsString("INFO  - Sending daily picture to user 1."));
            assertThat(output.getOut(), containsString("ERROR - Could not send picture to user 1."));
        }
    }
}
