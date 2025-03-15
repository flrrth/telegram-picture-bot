package picturebot.bot.command.settings;

import picturebot.bot.factory.SendMessageFactory;
import picturebot.entities.settings.Settings;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static picturebot.emoticons.Emoticons.ALARM_CLOCK;
import static picturebot.emoticons.Emoticons.WHITE_HEAVY_CHECK_MARK;

/**
 * This class creates a message that shows the user's settings.
 */
@Component
class SendMessageCreatorImpl implements SendMessageCreator {

    private final MessageSource messageSource;
    private final SendMessageFactory sendMessageFactory;

    /* default */ SendMessageCreatorImpl(final MessageSource messageSource, final SendMessageFactory sendMessageFactory) {
        this.messageSource = messageSource;
        this.sendMessageFactory = sendMessageFactory;
    }

    @Override
    public SendMessage create(final Long botUserId, final @Nullable Settings settings,
                              final ReplyKeyboardMarkup replyKeyboardMarkup, final Locale locale) {

        final SendMessage sendMessage = sendMessageFactory.getSendMessage();
        sendMessage.setChatId(botUserId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        if (settings == null) {
            sendMessage.setText(messageSource.getMessage("settings.absent", null, locale));
        }
        else {
            if (settings.getIsEnabled()) {
                final String[] arguments = {
                        settings.getSchedule().truncatedTo(ChronoUnit.MINUTES).toString(),
                        settings.getSpoilerEnabled() ?
                                messageSource.getMessage("settings.yes", null, locale) :
                                messageSource.getMessage("settings.no", null, locale)
                };

                sendMessage.setText(String.format(
                        messageSource.getMessage("settings.enabled", arguments, locale),
                        ALARM_CLOCK, WHITE_HEAVY_CHECK_MARK));
            }
            else {
                sendMessage.setText(String.format(
                        messageSource.getMessage("settings.disabled", null, locale), ALARM_CLOCK));
            }
        }

        return sendMessage;
    }
}
