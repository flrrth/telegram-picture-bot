package picturebot.bot.downloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.nio.file.Path;

/**
 * This class is responsible for downloading files from Telegram and storing them on the server.
 */
@Component
class TelegramFileDownloaderImpl implements TelegramFileDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramFileDownloaderImpl.class);

    @Override
    public File download(final DefaultAbsSender bot,
                         final Long id,
                         final org.telegram.telegrambots.meta.api.objects.File file,
                         final String destination) throws TelegramApiException {

        final String filePath = file.getFilePath();
        final Path destinationPath = Path.of(destination, file.getFileUniqueId());
        final File downloadedFile = bot.downloadFile(filePath, destinationPath.toFile());

        LOGGER.info("Stored picture of user {} in {}", id, destinationPath);

        return downloadedFile;
    }
}
