package picturebot.bot.downloader;

import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

public interface TelegramFileDownloader {

    /**
     * Download the photo and store it in the configured folder.
     * @param telegramClient the Telegram client,
     * @param id the user ID of the user that sent the photo,
     * @param file the file to download.
     * @param destination the base path of the storage folder.
     * @return a file handle to the downloaded photo.
     * @throws TelegramApiException thrown when the photo couldn't be downloaded.
     */
    File download(TelegramClient telegramClient, Long id, org.telegram.telegrambots.meta.api.objects.File file,
                  String destination) throws TelegramApiException;
}
