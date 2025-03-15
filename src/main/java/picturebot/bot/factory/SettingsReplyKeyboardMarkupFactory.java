package picturebot.bot.factory;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Locale;

public interface SettingsReplyKeyboardMarkupFactory {

    /**
     * Creates a ReplyKeyboardMarkup with a single button that opens the webapp with a customized URL.
     * @param url the URL
     * @param locale the locale
     * @return the ReplyKeyboardMarkup
     */
    ReplyKeyboardMarkup create(String url, Locale locale);
}
