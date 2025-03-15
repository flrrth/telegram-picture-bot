package picturebot.bot.factory;

import java.util.Locale;

/**
 * This class formats the cool down time.
 */
public interface CooldownTextFormatter {

    /**
     * Formats the cool down time in seconds into a more user-friendly way: 70 seconds will be displayed as '1 minute
     * and 10 seconds'.
     * @param coolDownInSeconds the remaining cool down time in seconds
     * @return a formatted <code>String</code> in the user's language.
     */
    String format(long coolDownInSeconds, Locale locale);
}
