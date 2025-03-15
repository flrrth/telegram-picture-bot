package picturebot.bot.command.settings;

import picturebot.entities.settings.Settings;
import org.springframework.lang.Nullable;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Locale;

interface SendMessageCreator {

    /**
     * Creates a customized SendMessage with a keyboard.
     * @param botUserId the ID of the bot user
     * @param settings the bot user settings
     * @param replyKeyboardMarkup the keyboard used for opening the settings web page
     * @param locale the locale used by the bot user
     * @return a SendMessage with custom text and keyboard
     */
    SendMessage create(Long botUserId, @Nullable Settings settings, ReplyKeyboardMarkup replyKeyboardMarkup, Locale locale);
}
