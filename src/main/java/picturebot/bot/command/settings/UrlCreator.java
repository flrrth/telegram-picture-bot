package picturebot.bot.command.settings;

import picturebot.entities.settings.Settings;
import org.springframework.lang.Nullable;

import java.util.Locale;

interface UrlCreator {

    /**
     * Creates the custom URL needed to open the user's settings.
     * @param settings the user's settings
     * @param locale the user's locale
     * @return a custom URL
     */
    String create(@Nullable Settings settings, Locale locale);
}
