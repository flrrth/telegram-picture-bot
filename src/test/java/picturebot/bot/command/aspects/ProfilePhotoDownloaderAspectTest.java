package picturebot.bot.command.aspects;

import picturebot.bot.downloader.TelegramFileDownloader;
import picturebot.bot.factory.GetFileFactory;
import picturebot.bot.factory.GetUserProfilePhotosFactory;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
class ProfilePhotoDownloaderAspectTest {

    @Mock private TelegramFileDownloader telegramFileDownloader;
    @Mock private GetFileFactory getFileFactory;
    @Mock private Environment environment;
    @Mock private FileSystem fileSystem;
    @Mock private GetUserProfilePhotosFactory getUserProfilePhotosFactory;
    @InjectMocks private ProfilePhotoDownloaderAspect profilePhotoDownloaderAspect;

    @Mock private JoinPoint joinPoint;
    final private AbsSender bot = mock(DefaultAbsSender.class);
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private Update update;
    @Mock private User user;
    @Mock private GetUserProfilePhotos getUserProfilePhotos;
    @Mock private UserProfilePhotos userProfilePhotos;
    @Mock private PhotoSize smallPhotoSize;
    @Mock private PhotoSize largePhotoSize;
    @Mock private GetFile getFile;
    @Mock private File file;
    @Mock private Path path;

    @Captor private ArgumentCaptor<DefaultAbsSender> botCaptor;
    @Captor private ArgumentCaptor<Long> userIdCaptor;
    @Captor private ArgumentCaptor<File> fileCaptor;
    @Captor private ArgumentCaptor<String> destinationCaptor;

    @Nested
    @DisplayName("afterRespond()")
    class BeforeRespond {

        @BeforeEach
        void configureJoinPoint() {
            when(joinPoint.getArgs()).thenReturn(new Object[]{ bot, update });
        }

        @Test
        @DisplayName("should do nothing when Update has no message")
        void shouldDoNothingWhenUpdateHasNoMessage() {
            when(update.hasMessage()).thenReturn(false);

            profilePhotoDownloaderAspect.respondAdvice(joinPoint);

            verifyNoInteractions(bot);
            verifyNoInteractions(getFileFactory);
            verifyNoInteractions(telegramFileDownloader);
        }

        @Test
        @DisplayName("should log that profile photo information cant be downloaded")
        void shouldLogThatProfilePhotoInformationCantBeDownloaded(final CapturedOutput output) throws TelegramApiException {
            when(update.hasMessage()).thenReturn(true);
            when(update.getMessage().getFrom()).thenReturn(user);
            when(user.getId()).thenReturn(1L);
            when(getUserProfilePhotosFactory.createGetUserProfilePhotos(1L)).thenReturn(getUserProfilePhotos);
            when(bot.execute(getUserProfilePhotos)).thenThrow(new TelegramApiException("Stacktrace"));

            profilePhotoDownloaderAspect.respondAdvice(joinPoint);

            assertThat(output.getOut(), containsString("ERROR - Could not download profile photo information of user 1."));
            assertThat(output.getOut(), containsString("Stacktrace"));
        }

        @Test
        @DisplayName("should do nothing when user has no profile photo's")
        void shouldDoNothingWhenUserHasNoProfilePhotos() throws TelegramApiException {
            when(update.hasMessage()).thenReturn(true);
            when(update.getMessage().getFrom()).thenReturn(user);
            when(user.getId()).thenReturn(1L);
            when(getUserProfilePhotosFactory.createGetUserProfilePhotos(1L)).thenReturn(getUserProfilePhotos);
            when(bot.execute(getUserProfilePhotos)).thenReturn(userProfilePhotos);
            when(userProfilePhotos.getPhotos()).thenReturn(List.of());

            profilePhotoDownloaderAspect.respondAdvice(joinPoint);

            verifyNoInteractions(getFileFactory);
            verifyNoInteractions(telegramFileDownloader);
        }

        @Test
        @DisplayName("should download profile photo")
        void shouldDownloadProfilePhoto() throws TelegramApiException {
            when(update.hasMessage()).thenReturn(true);
            when(update.getMessage().getFrom()).thenReturn(user);
            when(user.getId()).thenReturn(1L);
            when(getUserProfilePhotosFactory.createGetUserProfilePhotos(1L)).thenReturn(getUserProfilePhotos);
            when(bot.execute(getUserProfilePhotos)).thenReturn(userProfilePhotos);
            when(userProfilePhotos.getPhotos()).thenReturn(List.of(List.of(smallPhotoSize, largePhotoSize)));
            when(smallPhotoSize.getFileSize()).thenReturn(100);
            when(largePhotoSize.getFileSize()).thenReturn(1000);
            when(largePhotoSize.getFileId()).thenReturn("large");
            when(getFileFactory.createGetFileMethod("large")).thenReturn(getFile);
            when(bot.execute(getFile)).thenReturn(file);
            when(environment.getRequiredProperty("bot.profile.photos")).thenReturn("/dev/null/profile");
            when(path.toString()).thenReturn("/dev/null/profile/1");
            when(fileSystem.getPath("/dev/null/profile", "1")).thenReturn(path);

            profilePhotoDownloaderAspect.respondAdvice(joinPoint);

            verify(telegramFileDownloader).download(botCaptor.capture(), userIdCaptor.capture(), fileCaptor.capture(),
                    destinationCaptor.capture());

            assertEquals(bot, botCaptor.getValue());
            assertEquals(1L, userIdCaptor.getValue());
            assertEquals(fileCaptor.getValue(), file);
            assertEquals("/dev/null/profile/1", destinationCaptor.getValue());
        }

        @Test
        @DisplayName("should log that profile photo cant be downloaded")
        void shouldLogThatProfilePhotoCantBeDownloaded(final CapturedOutput output) throws TelegramApiException {
            when(update.hasMessage()).thenReturn(true);
            when(update.getMessage().getFrom()).thenReturn(user);
            when(user.getId()).thenReturn(1L);
            when(getUserProfilePhotosFactory.createGetUserProfilePhotos(1L)).thenReturn(getUserProfilePhotos);
            when(bot.execute(getUserProfilePhotos)).thenReturn(userProfilePhotos);
            when(userProfilePhotos.getPhotos()).thenReturn(List.of(List.of(smallPhotoSize, largePhotoSize)));
            when(smallPhotoSize.getFileSize()).thenReturn(100);
            when(largePhotoSize.getFileSize()).thenReturn(1000);
            when(largePhotoSize.getFileId()).thenReturn("large");
            when(getFileFactory.createGetFileMethod("large")).thenReturn(getFile);
            when(bot.execute(getFile)).thenThrow(new TelegramApiException("Stacktrace"));

            profilePhotoDownloaderAspect.respondAdvice(joinPoint);

            assertThat(output.getOut(), containsString("ERROR - Could not download profile photo large of user 1."));
            assertThat(output.getOut(), containsString("Stacktrace"));
        }
    }
}
