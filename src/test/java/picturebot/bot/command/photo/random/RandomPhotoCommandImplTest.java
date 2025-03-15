package picturebot.bot.command.photo.random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import picturebot.bot.factory.InputFileFactory;
import picturebot.bot.factory.SendChatActionFactory;
import picturebot.bot.factory.SendMessageFactory;
import picturebot.bot.factory.SendPhotoFactory;
import picturebot.entities.botuser.BotUser;
import picturebot.fixtures.BotUserFixture;
import picturebot.fixtures.UpdateFixture;
import picturebot.picture.PicturePicker;
import picturebot.picture.exceptions.PictureException;
import picturebot.repositories.UserRepository;
import picturebot.user.UserCoolDown;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@Tag("unit")
class RandomPhotoCommandImplTest {

    @Mock private UserCoolDown userCoolDown;
    @Mock private SendMessageFactory sendMessageFactory;
    @Mock private PicturePicker picturePicker;
    @Mock private SendPhotoFactory sendPhotoFactory;
    @Mock private SendChatActionFactory sendChatActionFactory;
    @Mock private SendChatAction sendChatAction;
    @Mock private InputFileFactory inputFileFactory;
    @Mock private InputFile inputFile;
    @Mock private Environment environment;
    @Mock private SendMessage sendMessage;
    @Mock private SendPhoto sendPhoto;
    @Mock private UserRepository userRepository;
    @Mock private AbsSender bot;

    @InjectMocks private RandomPhotoCommandImpl randomPhotoCommand;

    @Nested
    @DisplayName("respond()")
    class GetResponse {

        @Test
        @DisplayName("should respond with default censored SendPhoto")
        void shouldRespondWithDefaultCensoredSendPhoto() throws PictureException, TelegramApiException {
            final Update update = UpdateFixture.createBasicUpdate("en");
            final BotUser botUser = BotUserFixture.createBasicBotUser("en");
            
            when(userCoolDown.getSecondsLeftOnCoolDown(6L)).thenReturn(0L);
            when(environment.getRequiredProperty("bot.regular.subPath")).thenReturn("animals");
            when(picturePicker.getPicture("animals"))
                    .thenReturn(Optional.of("/pictures/animals/dog.jpg"));
            when(userRepository.findById(6L)).thenReturn(Optional.of(botUser));
            when(inputFileFactory.getInputFile("/pictures/animals/dog.jpg")).thenReturn(inputFile);            
            when(environment.getRequiredProperty("bot.isSpoiler", Boolean.class)).thenReturn(true);
            when(sendChatActionFactory.getSendChatActionForPhotoUpload(6L)).thenReturn(sendChatAction);
            when(sendPhotoFactory.getSendPhoto(6L, inputFile, true)).thenReturn(sendPhoto);

            // Act:
            randomPhotoCommand.respond(bot, update);

            assertEquals(1, botUser.getRequestCount());
            verify(userRepository).save(botUser);
            verify(bot).execute(sendChatAction);
            verify(bot).execute(sendPhoto);
        }

        @Test
        @DisplayName("should respond with uncensored SendPhoto because the user has configured this")
        void shouldRespondWithUncensoredSendPhotoBecauseTheUserHasConfiguredThis()
                throws PictureException, TelegramApiException {

            final Update update = UpdateFixture.createBasicUpdate("en");
            final BotUser botUser = BotUserFixture.createBotUserWithSettings("en");

            when(userCoolDown.getSecondsLeftOnCoolDown(6L)).thenReturn(0L);
            when(environment.getRequiredProperty("bot.regular.subPath")).thenReturn("animals");
            when(picturePicker.getPicture("animals"))
                    .thenReturn(Optional.of("/pictures/animals/dog.jpg"));
            when(userRepository.findById(6L)).thenReturn(Optional.of(botUser));
            when(inputFileFactory.getInputFile("/pictures/animals/dog.jpg")).thenReturn(inputFile);
            when(sendChatActionFactory.getSendChatActionForPhotoUpload(6L)).thenReturn(sendChatAction);
            when(sendPhotoFactory.getSendPhoto(6L, inputFile, false)).thenReturn(sendPhoto);

            // Act:
            randomPhotoCommand.respond(bot, update);

            assertEquals(1, botUser.getRequestCount());
            verify(userRepository).save(botUser);
            verify(bot).execute(sendChatAction);
            verify(bot).execute(sendPhoto);
        }

        @Test
        @DisplayName("should return TelegramResponse with SendMessage informing the user that no photos are available")
        void shouldRespondWithSendMessageNoPhotosAvailable() throws PictureException, TelegramApiException {
            final Update update = UpdateFixture.createBasicUpdate("en");

            when(userCoolDown.getSecondsLeftOnCoolDown(6L)).thenReturn(0L);
            when(environment.getRequiredProperty("bot.regular.subPath")).thenReturn("animals");
            when(picturePicker.getPicture("animals")).thenReturn(Optional.empty());
            when(sendMessageFactory.getNoPictureMessage(6L, Locale.ENGLISH)).thenReturn(sendMessage);

            // Act:
            randomPhotoCommand.respond(bot, update);

            verifyNoInteractions(userRepository);
            verify(bot).execute(sendMessage);
        }

        @Test
        @DisplayName("should respond with SendMessage informing the user that they are on cool down")
        void shouldRespondWithSendMessageUserOnCoolDown() throws TelegramApiException {
            final Update update = UpdateFixture.createBasicUpdate("en");

            when(userCoolDown.getSecondsLeftOnCoolDown(6L)).thenReturn(299L);
            when(sendMessageFactory.getCoolDownMessage(6L, Locale.ENGLISH, 299L))
                    .thenReturn(sendMessage);

            // Act:
            randomPhotoCommand.respond(bot, update);

            verifyNoInteractions(userRepository);
            verify(bot).execute(sendMessage);
        }

        @Test
        @DisplayName("should return TelegramResponse with SendMessage informing the user an error occurred")
        void getResponseShouldReturnTelegramResponseWithSendMessageUnexpectedError(final CapturedOutput output)
                throws PictureException, TelegramApiException {

            final Update update = UpdateFixture.createBasicUpdate("en");

            when(userCoolDown.getSecondsLeftOnCoolDown(6L)).thenReturn(0L);
            when(environment.getRequiredProperty("bot.regular.subPath")).thenReturn("animals");
            when(picturePicker.getPicture("animals")).thenThrow(PictureException.class);
            when(sendMessageFactory.getDefaultErrorMessage(6L, Locale.ENGLISH)).thenReturn(sendMessage);

            // Act:
            randomPhotoCommand.respond(bot, update);

            verifyNoInteractions(userRepository);
            verify(bot).execute(sendMessage);
            assertThat(output.getOut(), containsString("ERROR - Can't read picture."));
        }
    }
}
