package picturebot.bot.command.photo.upload;

import picturebot.bot.command.BotCommand;
import picturebot.bot.downloader.TelegramFileDownloader;
import picturebot.bot.factory.GetFileFactory;
import picturebot.bot.factory.SendMessageFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * This command gets executed when the user uploads a photo and saves it to the server.
 */
@Component
class UploadPhotoCommandImpl implements BotCommand {

    private final SendMessageFactory sendMessageFactory;
    private final GetFileFactory getFileFactory;
    private final TelegramFileDownloader fileDownloader;
    private final Environment environment;

    /* default */ UploadPhotoCommandImpl(final SendMessageFactory sendMessageFactory,
                                         final GetFileFactory getFileFactory,
                                         final TelegramFileDownloader fileDownloader,
                                         final Environment environment) {

        this.sendMessageFactory = sendMessageFactory;
        this.getFileFactory = getFileFactory;
        this.fileDownloader = fileDownloader;
        this.environment = environment;
    }

    /**
     * Respond with a thank-you message when a photo is uploaded.
     * @param bot the bot
     * @param update the update
     * @throws TelegramApiException thrown when there's a problem with the response
     */
    @Override
    public void respond(final TelegramClient telegramClient, final Update update) throws TelegramApiException {
        final Locale locale = new Locale.Builder()
                .setLanguage(update.getMessage().getFrom().getLanguageCode())
                .build();

        final List<PhotoSize> photos = update.getMessage().getPhoto();

        // Fetch the largest photo:
        final PhotoSize photo = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);

        if (photo != null) {
            final GetFile getFileMethod = getFileFactory.createGetFileMethod(photo.getFileId());
            final File file = telegramClient.execute(getFileMethod);

            fileDownloader.download((TelegramClient) telegramClient, update.getMessage().getFrom().getId(), file,
                    environment.getRequiredProperty("bot.uploads.photos"));

            telegramClient.execute(sendMessageFactory.getUploadConfirmationMessage(update.getMessage().getFrom().getId(), locale));
        }
        else {
            telegramClient.execute(sendMessageFactory.getDefaultErrorMessage(update.getMessage().getFrom().getId(), locale));
        }
    }
}
