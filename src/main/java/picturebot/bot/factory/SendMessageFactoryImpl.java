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
        return SendMessage.builder().build();
    }

    @Override
    public SendMessage getDefaultErrorMessage(final Long chatId, final Locale locale) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(String.format(messageSource.getMessage("error", null, locale),
                        FACE_WITH_OPEN_MOUTH_AND_COLD_SWEAT))
                .build();
    }

    @Override
    public SendMessage getNoPictureMessage(final Long chatId, final Locale locale) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(String.format(messageSource.getMessage("noPictures", null, locale),
                        FACE_WITH_OPEN_MOUTH_AND_COLD_SWEAT))
                .build();
    }

    @Override
    public SendMessage getHelpMessage(final Long chatId, final Locale locale) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(String.format(messageSource.getMessage("help", null, locale),
                        WHITE_DOWN_POINTING_BACKHAND_INDEX))
                .build();
    }

    @Override
    public SendMessage getStatsMessage(final Long chatId, final Locale locale, final String[] values) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(String.format(messageSource.getMessage("stats.pictureCount", values, locale), ARTIST_PALETTE))
                .build();
    }

    @Override
    public SendMessage getDefaultMessage(final Long chatId, final Locale locale, final String[] values) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(String.format(messageSource.getMessage("default", values, locale),
                        FACE_WITH_OPEN_MOUTH_AND_COLD_SWEAT,
                        WHITE_RIGHT_POINTING_BACKHAND_INDEX,
                        WHITE_RIGHT_POINTING_BACKHAND_INDEX,
                        WHITE_RIGHT_POINTING_BACKHAND_INDEX,
                        WHITE_RIGHT_POINTING_BACKHAND_INDEX))
                .build();
    }

    @Override
    public SendMessage getStartMessage(final Long chatId, final Locale locale, final String[] values) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(String.format(
                        messageSource.getMessage("start", values, locale),
                        WAVING_HAND_SIGN,
                        WHITE_RIGHT_POINTING_BACKHAND_INDEX,
                        WHITE_RIGHT_POINTING_BACKHAND_INDEX))
                .build();
    }

    @Override
    public SendMessage getCoolDownMessage(final Long chatId, final Locale locale, final long secondsLeftOnCoolDown) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(String.format(
                        messageSource.getMessage("coolDown",
                                new String[]{ cooldownTextFormatter.format(secondsLeftOnCoolDown, locale) },
                                locale),
                        RAISED_HAND))
                .build();
    }

    @Override
    public SendMessage getUploadConfirmationMessage(final Long chatId, final Locale locale) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(String.format(messageSource.getMessage("upload.thanks", null, locale), HEAVY_BLACK_HEART))
                .build();
    }

    @Override
    public SendMessage getVersionMessage(final Long chatId, final Locale locale, final String[] values) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(String.format(messageSource.getMessage("version", values, locale), ROCKET))
                .build();
    }
}
