package picturebot.bot.downloader;

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
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@Tag("unit")
class TelegramFileDownloaderImplTest {

    @Mock private DefaultAbsSender bot = mock(DefaultAbsSender.class);
    @Mock private org.telegram.telegrambots.meta.api.objects.File telegramFile;
    @Mock private File downloadFile;

    @InjectMocks private TelegramFileDownloaderImpl fileDownloader;

    @Nested
    @DisplayName("download()")
    class Download {

        @Test
        @DisplayName("should download and store the photo")
        void shouldDownloadAndStoreThePhoto(final CapturedOutput output) throws TelegramApiException {
            when(telegramFile.getFilePath()).thenReturn("some/path");
            when(telegramFile.getFileUniqueId()).thenReturn("fileUniqueId");
            when(bot.downloadFile("some/path", Path.of("/dev/null/photos/fileUniqueId").toFile()))
                    .thenReturn(downloadFile);

            final File actual = fileDownloader.download(bot, 1L, telegramFile, "/dev/null/photos");

            assertEquals(downloadFile, actual);
            assertThat(output.getOut(),
                    containsString("Stored picture of user 1 in /dev/null/photos/fileUniqueId"));
        }
    }
}
