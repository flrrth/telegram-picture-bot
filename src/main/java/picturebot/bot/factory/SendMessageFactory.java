package picturebot.bot.factory;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

/**
 * Factory for a variety of {@link SendMessage}s.
 */
public interface SendMessageFactory {

    /**
     * Returns a new SendMessage.
     * @return a new SendMessage.
     */
    SendMessage getSendMessage();

    /**
     * Returns a configured SendMessage with the default error text for a specific user.
     * @param chatId the ID of the user
     * @param locale the locale of the user
     * @return a configured SendMessage with the default error text for a specific user.
     */
    SendMessage getDefaultErrorMessage(Long chatId, Locale locale);

    /**
     * Returns a configured SendMessage with a message explaining that there are no pictures available.
     * @param chatId the ID of the user
     * @param locale the locale of the user
     * @return a configured SendMessage with a message explaining that there are no pictures available.
     */
    SendMessage getNoPictureMessage(Long chatId, Locale locale);

    /**
     * Returns a configured SendMessage with the help text.
     * @param chatId the ID of the user
     * @param locale the locale of the user
     * @return a configured SendMessage with the help text.
     */
    SendMessage getHelpMessage(Long chatId, Locale locale);

    /**
     * Returns a configured SendMessage with the bot statistics.
     * @param chatId the ID of the user
     * @param locale the locale of the user
     * @param values the values
     * @return a configured SendMessage with the bot statistics.
     */
    SendMessage getStatsMessage(Long chatId, Locale locale, String[] values);

    /**
     * Returns a configured SendMessage with the default text.
     * @param chatId the ID of the user
     * @param locale the locale of the user
     * @param values the values
     * @return a configured SendMessage with the default text.
     */
    SendMessage getDefaultMessage(Long chatId, Locale locale, String[] values);

    /**
     * Returns a configured SendMessage with a greeting for the user.
     * @param chatId the ID of the user
     * @param locale the locale of the user
     * @param values the values
     * @return a configured SendMessage with a greeting for the user.
     */
    SendMessage getStartMessage(Long chatId, Locale locale, String[] values);

    /**
     * Returns a configured SendMessage with the cool down text.
     * @param chatId the ID of the user
     * @param locale the locale of the user
     * @param secondsLeftOnCoolDown the number of seconds the user is still on 'cool down'
     * @return a configured SendMessage with the cool down text.
     */
    SendMessage getCoolDownMessage(Long chatId, Locale locale, long secondsLeftOnCoolDown);

    /**
     * Returns a configured SendMessage with a message that thanks the user for the upload.
     * @param chatId the ID of the user
     * @param locale the locale of the user
     * @return a configured SendMessage with a message that thanks the user for the upload.
     */
    SendMessage getUploadConfirmationMessage(Long chatId, Locale locale);

    /**
     * Returns a configured SendMessage with the application version.
     * @param chatId the ID of the user
     * @param locale the locale of the user
     * @param values the values
     * @return a configured SendMessage with a greeting for the user.
     */
    SendMessage getVersionMessage(Long chatId, Locale locale, String[] values);
}
