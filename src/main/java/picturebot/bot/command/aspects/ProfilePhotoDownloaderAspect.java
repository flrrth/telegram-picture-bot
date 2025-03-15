package picturebot.bot.command.aspects;

import picturebot.bot.downloader.TelegramFileDownloader;
import picturebot.bot.factory.GetFileFactory;
import picturebot.bot.factory.GetUserProfilePhotosFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.nio.file.FileSystem;
import java.util.Comparator;

/**
 * Aspect for downloading user profile photos after a command is executed.
 */
@Aspect
@Component
public class ProfilePhotoDownloaderAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfilePhotoDownloaderAspect.class);

    private final TelegramFileDownloader telegramFileDownloader;
    private final GetFileFactory getFileFactory;
    private final Environment environment;
    private final FileSystem fileSystem;
    private final GetUserProfilePhotosFactory getUserProfilePhotosFactory;

    /**
     * Constructor for ProfilePhotoDownloaderAspect.
     *
     * @param telegramFileDownloader the downloader for Telegram files
     * @param getFileFactory the factory for creating GetFile methods
     * @param environment the Spring environment for accessing properties
     * @param fileSystem the file system for creating paths
     * @param getUserProfilePhotosFactory the factory for creating GetUserProfilePhotos methods
     */
    public ProfilePhotoDownloaderAspect(final TelegramFileDownloader telegramFileDownloader,
                                        final GetFileFactory getFileFactory,
                                        final Environment environment,
                                        final FileSystem fileSystem,
                                        final GetUserProfilePhotosFactory getUserProfilePhotosFactory) {

        this.telegramFileDownloader = telegramFileDownloader;
        this.getFileFactory = getFileFactory;
        this.environment = environment;
        this.fileSystem = fileSystem;
        this.getUserProfilePhotosFactory = getUserProfilePhotosFactory;
    }

    /**
     * Advice that runs after the respond-method of a start command is executed.
     * Downloads the user's profile photo if available.
     *
     * @param joinPoint the join point providing access to the method arguments
     */
    @After("CommonPointcuts.respondMethodStartCommand()")
    public void respondAdvice(final JoinPoint joinPoint) {
        final AbsSender bot = (AbsSender) joinPoint.getArgs()[0];
        final Update update = (Update) joinPoint.getArgs()[1];

        if (update.hasMessage()) {
            final User user = update.getMessage().getFrom();

            try {
                final UserProfilePhotos profilePhotos = bot.execute(
                        getUserProfilePhotosFactory.createGetUserProfilePhotos(user.getId()));

                profilePhotos.getPhotos().forEach(photoSizeList -> {
                    final PhotoSize photo = photoSizeList.stream()
                            .max(Comparator.comparing(PhotoSize::getFileSize))
                            .orElse(null);

                    if (photo != null) {
                        final GetFile getFileMethod = getFileFactory.createGetFileMethod(photo.getFileId());

                        try {
                            final File file = bot.execute(getFileMethod);
                            final String destinationFolder = fileSystem.getPath(
                                    environment.getRequiredProperty("bot.profile.photos"),
                                    user.getId().toString()).toString();

                            telegramFileDownloader.download(
                                    (DefaultAbsSender) bot,
                                    update.getMessage().getFrom().getId(),
                                    file,
                                    destinationFolder);
                        }
                        catch (final TelegramApiException e) {
                            LOGGER.error("Could not download profile photo {} of user {}.",
                                    photo.getFileId(),
                                    user.getId(), e);
                        }
                    }
                });
            }
            catch (final TelegramApiException e) {
                LOGGER.error("Could not download profile photo information of user {}.", user.getId(), e);
            }
        }
    }
}
