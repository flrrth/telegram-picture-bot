package picturebot.bot.command.photo.upload;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.List;
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
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import picturebot.bot.downloader.TelegramFileDownloader;
import picturebot.bot.factory.GetFileFactory;
import picturebot.bot.factory.SendMessageFactory;
import picturebot.fixtures.UpdateFixture;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class UploadPhotoCommandImplTest {

    @Mock private PhotoSize photoSize1;
    @Mock private PhotoSize photoSize2;
    final private DefaultAbsSender bot = mock(DefaultAbsSender.class);
    @Mock private org.telegram.telegrambots.meta.api.objects.File telegramFile;
    @Mock private File downloadedFile;
    @Mock private SendMessageFactory sendMessageFactory;
    @Mock private SendMessage sendMessage;
    @Mock private GetFileFactory getFileFactory;
    @Mock private GetFile getFileMethod;
    @Mock private TelegramFileDownloader fileDownloader;
    @Mock private Environment environment;

    @InjectMocks private UploadPhotoCommandImpl uploadPhotoCommand;

    @Nested
    @DisplayName("respond()")
    class Respond {

        @Test
        @DisplayName("should respond with SendMessage thanking the user for the photo upload")
        void shouldRespondWithSendMessageThankingTheUserForThePhotoUpload() throws TelegramApiException {
            final Update update = UpdateFixture.createBasicUpdate("en");

            when(getFileFactory.createGetFileMethod("photo2")).thenReturn(getFileMethod);
            when(bot.execute(getFileMethod)).thenReturn(telegramFile);            
            when(environment.getRequiredProperty("bot.uploads.photos")).thenReturn("/dev/null/avatars");
            when(fileDownloader.download(bot, 6L, telegramFile, "/dev/null/avatars"))
                    .thenReturn(downloadedFile);
            when(sendMessageFactory.getUploadConfirmationMessage(6L, Locale.ENGLISH)).thenReturn(sendMessage);

            // Act:
            uploadPhotoCommand.respond(bot, update);

            verify(bot).execute(sendMessage);
        }

        @Test
        @DisplayName("should respond with default error message when the message does not contain a photo")
        void shouldRespondWithDefaultErrorMessageWhenMessageDoesNotContainPhoto() throws TelegramApiException {
            final Update update = UpdateFixture.createBasicUpdate("en");
            update.getMessage().setPhoto(List.of());

            when(sendMessageFactory.getDefaultErrorMessage(6L, Locale.ENGLISH)).thenReturn(sendMessage);

            // Act:
            uploadPhotoCommand.respond(bot, update);

            verify(bot).execute(sendMessage);
        }
    }
}
