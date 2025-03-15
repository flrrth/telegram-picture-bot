package picturebot.bot.command.photo.unique;

import picturebot.bot.command.ScheduledCommand;
import picturebot.bot.factory.InputFileFactory;
import picturebot.bot.factory.SendMessageFactory;
import picturebot.bot.factory.SendPhotoFactory;
import picturebot.entities.botuser.BotUser;
import picturebot.picture.PicturePicker;
import picturebot.picture.exceptions.PictureException;
import picturebot.picture.greeting.GreetingRetriever;
import picturebot.picture.path.PicturePathRetriever;
import picturebot.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;

/**
 * This command is executed by the scheduler and sends a unique picture to the user. Unique means that the picture has
 * not been sent to the user before.
 */
@Component
class UniquePhotoCommandImpl implements ScheduledCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniquePhotoCommandImpl.class);

    private final PicturePicker picturePicker;
    private final InputFileFactory inputFileFactory;
    private final SendPhotoFactory sendPhotoFactory;
    private final SendMessageFactory sendMessageFactory;
    private final UserRepository userRepository;
    private final PicturePathRetriever picturePathRetriever;
    private final GreetingRetriever greetingRetriever;

    /* default */ UniquePhotoCommandImpl(final @Qualifier("databaseBackedUniquePicturePickerImpl") PicturePicker picturePicker,
                                  final InputFileFactory inputFileFactory, final SendPhotoFactory sendPhotoFactory,
                                  final SendMessageFactory sendMessageFactory, final UserRepository userRepository,
                                  final PicturePathRetriever picturePathRetriever,
                                  final GreetingRetriever greetingRetriever) {

        this.picturePicker = picturePicker;
        this.inputFileFactory = inputFileFactory;
        this.sendPhotoFactory = sendPhotoFactory;
        this.sendMessageFactory = sendMessageFactory;
        this.userRepository = userRepository;
        this.picturePathRetriever = picturePathRetriever;
        this.greetingRetriever = greetingRetriever;
    }

    @Override
    public void send(final AbsSender bot, final BotUser botUser) throws TelegramApiException {
        if (botUser.getSettings() == null) {
            LOGGER.warn("User {} has no settings.", botUser.getId());
            return;
        }

        final Locale locale = new Locale.Builder()
                .setLanguage(botUser.getUserDetails().get(0).getLanguageCode())
                .build();

        try {
            final Path subPath = picturePathRetriever.getDailyPicturePath(botUser.getSettings().getTimezone());
            final Optional<String> dailyPicture = picturePicker.getPicture(subPath.toString());

            if (dailyPicture.isPresent()) {
                final SendPhoto sendPhoto = sendPhotoFactory.getSendPhoto(
                        botUser.getId(),
                        inputFileFactory.getInputFile(dailyPicture.get()),
                        botUser.getSettings().getSpoilerEnabled(),
                        greetingRetriever.getGreeting(locale, subPath));

                try {
                    bot.execute(sendPhoto);
                }
                catch (TelegramApiException e) {
                    if (e.getMessage().contains("[403] Forbidden: bot was blocked by the user")) {
                        LOGGER.info("User {} has blocked the bot.", botUser.getId());
                        botUser.setHasBlockedBot(true);
                        userRepository.save(botUser);
                    }
                    else {
                        throw e;
                    }
                }
            }
            else {
                bot.execute(sendMessageFactory.getNoPictureMessage(botUser.getId(), locale));
            }
        }
        catch (PictureException e) {
            LOGGER.error("Can't read picture.", e);
            bot.execute(sendMessageFactory.getDefaultErrorMessage(botUser.getId(), locale));
        }
    }
}
