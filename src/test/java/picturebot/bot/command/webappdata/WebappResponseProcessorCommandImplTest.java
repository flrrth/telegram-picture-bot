package picturebot.bot.command.webappdata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Locale;
import java.util.Optional;

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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppData;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.fasterxml.jackson.core.JsonProcessingException;

import picturebot.bot.factory.SendMessageFactory;
import picturebot.entities.botuser.BotUser;
import picturebot.entities.settings.Settings;
import picturebot.entities.timezone.Timezone;
import picturebot.fixtures.UpdateFixture;
import picturebot.repositories.TimezoneRepository;
import picturebot.repositories.UserRepository;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@Tag("unit")
class WebappResponseProcessorCommandImplTest {

    @Mock private WebappDataParser webappDataParser;
    @Mock private TimezoneRepository timezoneRepository;
    @Mock private UserRepository userRepository;
    @Mock private SettingsUpdater settingsUpdater;
    @Mock private AbsSender bot;
    @Mock private SendMessageFactory sendMessageFactory;
    @Mock private SendMessage sendMessage;
    @Mock private BotUser botUser;
    @Mock private Timezone timezone;
    @Mock private Settings settings;

    @InjectMocks private WebappResponseProcessorCommandImpl command;

    @Nested
    @DisplayName("respond()")
    class GetResponse {

        @Test
        @DisplayName("should respond with SendMessage when web application data can't be parsed")
        void shouldRespondWithSendMessageWhenWebApplicationDataCantBeParsed(final CapturedOutput output)
                throws JsonProcessingException, TelegramApiException {

            final Update update = UpdateFixture.createBasicUpdate("en");
            update.getMessage().setWebAppData(new WebAppData());
            update.getMessage().getWebAppData().setData("invalid data");
            
            when(webappDataParser.parse("invalid data")).thenThrow(JsonProcessingException.class);                    
            when(sendMessageFactory.getDefaultErrorMessage(6L, Locale.ENGLISH)).thenReturn(sendMessage);

            // Act:
            command.respond(bot, update);

            verify(bot).execute(sendMessage);
            assertThat(output.getOut(), containsString("ERROR - Could not parse webapp data."));
        }

        @Test
        @DisplayName("should respond with DeleteMessage when web application data was processed successfully")
        void shouldRespondWithDeleteMessageWhenWebApplicationDataWasProcessedSuccessfully()
                throws JsonProcessingException, TelegramApiException {

            final Update update = UpdateFixture.createBasicUpdate("en");
            final String dataString = """
                    {
                        "isEnabled": true,
                        "isSpoilerEnabled": true,
                        "schedule": "07:00:00",
                        "timezone": 1
                    }
                    """;

            update.getMessage().setWebAppData(new WebAppData());
            update.getMessage().getWebAppData().setData(dataString);

            final WebappData data = new WebappData();
            data.setIsEnabled(true);
            data.setIsSpoilerEnabled(true);
            data.setSchedule(LocalTime.of(7, 0, 0));
            data.setTimezone(1L);

            when(webappDataParser.parse(dataString)).thenReturn(data);            
            when(timezoneRepository.findById(1L)).thenReturn(Optional.of(timezone));
            when(userRepository.findById(6L)).thenReturn(Optional.of(botUser));
            when(settingsUpdater.updateSettings(botUser, data, timezone)).thenReturn(settings);
            when(botUser.getSettingsMessageId()).thenReturn(12345);

            // Act:
            command.respond(bot, update);

            verify(settingsUpdater).updateSettings(botUser, data, timezone);
            verify(botUser).setSettings(settings);
            verify(userRepository).save(botUser);
            verify(bot).execute(new DeleteMessage("6", 12345));
        }

        @Test
        @DisplayName("should not respond because user does not have settings message ID")
        void shouldNotRespondBecauseUserDoesNotHaveSettingsMessageId()
                throws JsonProcessingException, TelegramApiException {

            final Update update = UpdateFixture.createBasicUpdate("en");
            final String dataString = """
                    {
                        "isEnabled": true,
                        "isSpoilerEnabled": true,
                        "schedule": "07:00:00",
                        "timezone": 1
                    }
                    """;

            update.getMessage().setWebAppData(new WebAppData());
            update.getMessage().getWebAppData().setData(dataString);

            final WebappData data = new WebappData();
            data.setIsEnabled(true);
            data.setIsSpoilerEnabled(true);
            data.setSchedule(LocalTime.of(7, 0, 0));
            data.setTimezone(1L);
        
            
            when(webappDataParser.parse(dataString)).thenReturn(data);
            when(timezoneRepository.findById(1L)).thenReturn(Optional.of(timezone));
            when(botUser.getSettingsMessageId()).thenReturn(null);
            when(userRepository.findById(6L)).thenReturn(Optional.of(botUser));

            command.respond(bot, update);

            verifyNoInteractions(bot);
        }
    }
}
