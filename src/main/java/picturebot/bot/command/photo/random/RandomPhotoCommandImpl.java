package picturebot.bot.command.photo.random;

import picturebot.bot.command.BotCommand;
import picturebot.bot.factory.InputFileFactory;
import picturebot.bot.factory.SendChatActionFactory;
import picturebot.bot.factory.SendMessageFactory;
import picturebot.bot.factory.SendPhotoFactory;
import picturebot.entities.botuser.BotUser;
import picturebot.picture.PicturePicker;
import picturebot.picture.exceptions.PictureException;
import picturebot.repositories.UserRepository;
import picturebot.user.UserCoolDown;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;
import java.util.Optional;

/**
 * This command gets executed when the user sends the 'secret' command and sends a random picture.
 */
@Component
class RandomPhotoCommandImpl implements BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomPhotoCommandImpl.class);

    private final UserCoolDown userCoolDown;
    private final SendMessageFactory sendMessageFactory;
    private final PicturePicker picturePicker;
    private final SendPhotoFactory sendPhotoFactory;
    private final InputFileFactory inputFileFactory;
    private final Environment environment;
    private final UserRepository userRepository;
    private final SendChatActionFactory sendChatActionFactory;

    /* default */ RandomPhotoCommandImpl(final UserCoolDown userCoolDown,
                                         final SendMessageFactory sendMessageFactory,
                                         @Qualifier("randomPicturePickerImpl") final PicturePicker picturePicker,
                                         final SendPhotoFactory sendPhotoFactory,
                                         final InputFileFactory inputFileFactory,
                                         final Environment environment,
                                         final UserRepository userRepository,
                                         final SendChatActionFactory sendChatActionFactory) {

        this.userCoolDown = userCoolDown;
        this.sendMessageFactory = sendMessageFactory;
        this.picturePicker = picturePicker;
        this.sendPhotoFactory = sendPhotoFactory;
        this.inputFileFactory = inputFileFactory;
        this.environment = environment;
        this.userRepository = userRepository;
        this.sendChatActionFactory = sendChatActionFactory;
    }

    /**
     * Respond with a random picture.
     * @param bot the bot
     * @param update the update
     * @throws TelegramApiException thrown when there's a problem with the response
     */
    @Override
    @Transactional
    public void respond(final AbsSender bot, final Update update) throws TelegramApiException {
        final Locale locale = new Locale.Builder()
                .setLanguage(update.getMessage().getFrom().getLanguageCode())
                .build();
        final Long userId = update.getMessage().getFrom().getId();

        try {
            final long secondsLeftOnCoolDown = this.userCoolDown.getSecondsLeftOnCoolDown(userId);

            if (secondsLeftOnCoolDown > 0) {
                bot.execute(sendMessageFactory.getCoolDownMessage(userId, locale, secondsLeftOnCoolDown));
            }
            else {
                final Optional<String> picture = picturePicker.getPicture(
                        environment.getRequiredProperty("bot.regular.subPath"));

                if (picture.isPresent()) {
                    final BotUser botUser = userRepository.findById(userId).orElseThrow();
                    final Boolean hasSpoiler;

                    if (botUser.getSettings() != null) {
                        hasSpoiler = botUser.getSettings().getSpoilerEnabled();
                    }
                    else {
                        hasSpoiler = environment.getRequiredProperty("bot.isSpoiler", Boolean.class);
                    }

                    incrementRequestCountAndSaveUser(botUser);

                    final SendChatAction sendChatAction = sendChatActionFactory.getSendChatActionForPhotoUpload(userId);
                    bot.execute(sendChatAction);

                    final SendPhoto sendPhoto = sendPhotoFactory.getSendPhoto(userId,
                            inputFileFactory.getInputFile(picture.get()), hasSpoiler);

                    bot.execute(sendPhoto);
                }
                else {
                    bot.execute(sendMessageFactory.getNoPictureMessage(userId, locale));
                }
            }
        }
        catch (PictureException e) {
            LOGGER.error("Can't read picture.", e);
            bot.execute(sendMessageFactory.getDefaultErrorMessage(userId, locale));
        }
    }

    private void incrementRequestCountAndSaveUser(final BotUser botUser) {
        botUser.setRequestCount(botUser.getRequestCount() + 1);
        userRepository.save(botUser);
    }
}
