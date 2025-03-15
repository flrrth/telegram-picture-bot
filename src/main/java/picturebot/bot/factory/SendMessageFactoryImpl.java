package picturebot.bot.factory;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

import static picturebot.emoticons.Emoticons.*;

/**
 * This factory class is responsible for creating the different types of SendMessages that the bot can send.
 */
@Component
public class SendMessageFactoryImpl implements SendMessageFactory {

    private final MessageSource messageSource;
    private final CooldownTextFormatter cooldownTextFormatter;

    public SendMessageFactoryImpl(final MessageSource messageSource,
                                  final CooldownTextFormatter cooldownTextFormatter) {

        this.messageSource = messageSource;
        this.cooldownTextFormatter = cooldownTextFormatter;
    }

    @Override
    public SendMessage getSendMessage() {
        return new SendMessage();
    }

    @Override
    public SendMessage getDefaultErrorMessage(final Long chatId, final Locale locale) {
        return new SendMessage(
                chatId.toString(),
                String.format(messageSource.getMessage("error", null, locale),
                        FACE_WITH_OPEN_MOUTH_AND_COLD_SWEAT)
        );
    }

    @Override
    public SendMessage getNoPictureMessage(final Long chatId, final Locale locale) {
        return new SendMessage(
                chatId.toString(),
                String.format(messageSource.getMessage("noPictures", null, locale),
                        FACE_WITH_OPEN_MOUTH_AND_COLD_SWEAT)
        );
    }

    @Override
    public SendMessage getHelpMessage(final Long chatId, final Locale locale) {
        return new SendMessage(
                chatId.toString(),
                String.format(messageSource.getMessage("help", null, locale),
                        WHITE_DOWN_POINTING_BACKHAND_INDEX)
        );
    }

    @Override
    public SendMessage getStatsMessage(final Long chatId, final Locale locale, final String[] values) {
        return new SendMessage(
                chatId.toString(),
                String.format(messageSource.getMessage("stats.pictureCount", values, locale), ARTIST_PALETTE)
        );
    }

    @Override
    public SendMessage getDefaultMessage(final Long chatId, final Locale locale, final String[] values) {
        return new SendMessage(
                chatId.toString(),
                String.format(messageSource.getMessage("default", values, locale),
                        FACE_WITH_OPEN_MOUTH_AND_COLD_SWEAT,
                        WHITE_RIGHT_POINTING_BACKHAND_INDEX,
                        WHITE_RIGHT_POINTING_BACKHAND_INDEX,
                        WHITE_RIGHT_POINTING_BACKHAND_INDEX,
                        WHITE_RIGHT_POINTING_BACKHAND_INDEX)
        );
    }

    @Override
    public SendMessage getStartMessage(final Long chatId, final Locale locale, final String[] values) {
        return new SendMessage(
                chatId.toString(),
                String.format(
                        messageSource.getMessage("start", values, locale),
                        WAVING_HAND_SIGN,
                        WHITE_RIGHT_POINTING_BACKHAND_INDEX,
                        WHITE_RIGHT_POINTING_BACKHAND_INDEX)
        );
    }

    @Override
    public SendMessage getCoolDownMessage(final Long chatId, final Locale locale, final long secondsLeftOnCoolDown) {
        return new SendMessage(
                chatId.toString(),
                String.format(
                        messageSource.getMessage("coolDown",
                                new String[]{ cooldownTextFormatter.format(secondsLeftOnCoolDown, locale) },
                                locale),
                        RAISED_HAND)
        );
    }

    @Override
    public SendMessage getUploadConfirmationMessage(final Long chatId, final Locale locale) {
        return new SendMessage(
                chatId.toString(),
                String.format(messageSource.getMessage("upload.thanks", null, locale), HEAVY_BLACK_HEART)
        );
    }

    @Override
    public SendMessage getVersionMessage(final Long chatId, final Locale locale, final String[] values) {
        return new SendMessage(
                chatId.toString(),
                String.format(messageSource.getMessage("version", values, locale), ROCKET)
        );
    }
}
