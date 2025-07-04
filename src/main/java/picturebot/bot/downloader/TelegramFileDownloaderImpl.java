package picturebot.bot.downloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * This class is responsible for downloading files from Telegram and storing them on the server.
 */
@Component
class TelegramFileDownloaderImpl implements TelegramFileDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramFileDownloaderImpl.class);

    @Override
    public File download(final TelegramClient telegramClient,
                         final Long id,
                         final org.telegram.telegrambots.meta.api.objects.File file,
                         final String destination) throws TelegramApiException {

        final Path destinationPath = Path.of(destination, file.getFileUniqueId());
        
        try {
            // Download the file using the new API
            final File tempFile = telegramClient.downloadFile(file);
            
            // Move the temporary file to our desired location
            Files.move(tempFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            
            final File downloadedFile = destinationPath.toFile();
            LOGGER.info("Stored picture of user {} in {}", id, destinationPath);
            
            return downloadedFile;
        } catch (IOException e) {
            throw new TelegramApiException("Failed to move downloaded file to destination", e);
        }
    }
}
