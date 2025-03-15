package picturebot.bot.command.photo.unique;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import picturebot.bot.factory.InputFileFactory;
import picturebot.bot.factory.SendMessageFactory;
import picturebot.bot.factory.SendPhotoFactory;
import picturebot.entities.botuser.BotUser;
import picturebot.entities.timezone.Timezone;
import picturebot.fixtures.BotUserFixture;
import picturebot.picture.PicturePicker;
import picturebot.picture.exceptions.PictureException;
import picturebot.picture.greeting.GreetingRetriever;
import picturebot.picture.path.PicturePathRetriever;
import picturebot.repositories.UserRepository;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@Tag("unit")
class UniquePhotoCommandImplTest {

    // The class-under-test and its dependencies:
    @Mock private PicturePicker picturePicker;
    @Mock private InputFileFactory inputFileFactory;
    @Mock private SendPhotoFactory sendPhotoFactory;
    @Mock private SendMessageFactory sendMessageFactory;
    @Mock private UserRepository userRepository;
    @Mock private PicturePathRetriever picturePathRetriever;
    @Mock private GreetingRetriever greetingRetriever;
    @InjectMocks private UniquePhotoCommandImpl uniquePhotoCommand;

    // Other mocks:
    @Mock private InputFile inputFile;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) BotUser botUser;
    @Mock private SendMessage sendMessage;
    @Mock private SendPhoto sendPhoto;
    @Mock private AbsSender bot;
    @Mock private Timezone timezone;

    @Nested
    @DisplayName("getResponse()")
    class GetResponse {

        @Test
        @DisplayName("should log warning when user does not have settings")
        void shouldLogWarningWhenUserDoesNotHaveSettings(final CapturedOutput output) throws TelegramApiException {
            final BotUser botUser = BotUserFixture.createBasicBotUser("en");

            // Act:
            uniquePhotoCommand.send(bot, botUser);

            assertThat(output.getOut(), containsString("WARN  - User 6 has no settings."));
            verifyNoInteractions(bot);
        }

        @Test
        @DisplayName("should respond with censored SendPhoto")
        void shouldRespondWithCensoredSendPhoto() throws PictureException, TelegramApiException {
            final BotUser botUser = BotUserFixture.createBotUserWithSettings("en");
            botUser.getSettings().setIsSpoilerEnabled(true);
            final Path animals = Path.of("animals");

            when(picturePathRetriever.getDailyPicturePath(botUser.getSettings().getTimezone())).thenReturn(animals);
            when(picturePicker.getPicture("animals"))
                    .thenReturn(Optional.of("/pictures/animals/dog.jpg"));
            when(inputFileFactory.getInputFile("/pictures/animals/dog.jpg")).thenReturn(inputFile);
            when(greetingRetriever.getGreeting(Locale.ENGLISH, animals)).thenReturn("Hello World!");
            when(sendPhotoFactory.getSendPhoto(6L, inputFile, true, "Hello World!"))
                    .thenReturn(sendPhoto);

            //Act:
            uniquePhotoCommand.send(bot, botUser);

            verify(bot).execute(sendPhoto);
        }

        @Test
        @DisplayName("should return TelegramResponse with SendMessage informing the user that no photos are available")
        void shouldRespondWithSendMessageNoPhotosAvailable() throws PictureException, TelegramApiException {
            final BotUser botUser = BotUserFixture.createBotUserWithSettings("en");
            botUser.getSettings().setIsSpoilerEnabled(true);
            
            when(picturePathRetriever.getDailyPicturePath(botUser.getSettings().getTimezone())).thenReturn(Path.of("animals"));
            when(picturePicker.getPicture("animals")).thenReturn(Optional.empty());
            when(sendMessageFactory.getNoPictureMessage(6L, Locale.ENGLISH)).thenReturn(sendMessage);

            // Act:
            uniquePhotoCommand.send(bot, botUser);

            verify(bot).execute(sendMessage);
        }

        @Test
        @DisplayName("should return TelegramResponse with SendMessage informing the user an error occurred")
        void shouldReturnTelegramResponseWithSendMessageUnexpectedError(final CapturedOutput output)
                throws PictureException, TelegramApiException {

            final BotUser botUser = BotUserFixture.createBotUserWithSettings("en");
            botUser.getSettings().setIsSpoilerEnabled(true);

            when(picturePathRetriever.getDailyPicturePath(botUser.getSettings().getTimezone())).thenReturn(Path.of("animals"));
            when(picturePicker.getPicture("animals")).thenThrow(PictureException.class);
            when(sendMessageFactory.getDefaultErrorMessage(6L, Locale.ENGLISH)).thenReturn(sendMessage);

            // Act:
            uniquePhotoCommand.send(bot, botUser);

            verify(bot).execute(sendMessage);
            assertThat(output.getOut(), containsString("ERROR - Can't read picture."));
        }

        @Test
        @DisplayName("should set BotUser 'hasBlockedBot' to true because the user has blocked the bot")
        void shouldSetBotUserHasBlockedBotToTrueBecauseTheUserHasBlockedTheBot(final CapturedOutput output) throws PictureException,
                TelegramApiException {

            final BotUser botUser = BotUserFixture.createBotUserWithSettings("en");
            botUser.getSettings().setIsSpoilerEnabled(true);            
            final Path animals = Path.of("animals");

            when(picturePathRetriever.getDailyPicturePath(botUser.getSettings().getTimezone())).thenReturn(animals);
            when(picturePicker.getPicture("animals"))
                    .thenReturn(Optional.of("/pictures/animals/dog.jpg"));
            when(inputFileFactory.getInputFile("/pictures/animals/dog.jpg")).thenReturn(inputFile);            
            when(greetingRetriever.getGreeting(Locale.ENGLISH, animals)).thenReturn("Hello World!");
            when(sendPhotoFactory.getSendPhoto(6L, inputFile, true, "Hello World!"))
                    .thenReturn(sendPhoto);
            when(bot.execute(sendPhoto))
                    .thenThrow(new TelegramApiException("[403] Forbidden: bot was blocked by the user"));

            // Act:
            uniquePhotoCommand.send(bot, botUser);

            assertTrue(botUser.getHasBlockedBot());
            verify(userRepository).save(botUser);
            assertThat(output.getOut(), containsString("INFO  - User 6 has blocked the bot."));
        }

        @Test
        @DisplayName("should rethrow TelegramApiException")
        void shouldRethrowTelegramApiException() throws TelegramApiException, PictureException {
            final BotUser botUser = BotUserFixture.createBotUserWithSettings("en");
            botUser.getSettings().setIsSpoilerEnabled(true);  
            final Path animals = Path.of("animals");

            when(picturePathRetriever.getDailyPicturePath(botUser.getSettings().getTimezone())).thenReturn(animals);
            when(picturePicker.getPicture("animals"))
                    .thenReturn(Optional.of("/pictures/animals/dog.jpg"));
            when(inputFileFactory.getInputFile("/pictures/animals/dog.jpg")).thenReturn(inputFile);            
            when(greetingRetriever.getGreeting(Locale.ENGLISH, animals)).thenReturn("Hello World!");
            when(sendPhotoFactory.getSendPhoto(6L, inputFile, true, "Hello World!"))
                    .thenReturn(sendPhoto);
            when(bot.execute(sendPhoto))
                    .thenThrow(new TelegramApiException("error"));

            // Act:
            final TelegramApiException exception = assertThrows(TelegramApiException.class,
                    () -> uniquePhotoCommand.send(bot, botUser));

            assertThat(exception.getMessage(), is(equalTo("error")));
        }
    }
}
