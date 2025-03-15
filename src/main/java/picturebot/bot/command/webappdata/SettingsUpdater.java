package picturebot.bot.command.webappdata;

import picturebot.entities.botuser.BotUser;
import picturebot.entities.settings.Settings;
import picturebot.entities.timezone.Timezone;

interface SettingsUpdater {


    /**
     * Updates the user's settings with the data provided via the web application.
     * @param botUser the bot user
     * @param data the data received via the web application
     * @param timezone the timezone of the bot user
     * @return the updated or newly created settings
     */
    Settings updateSettings(BotUser botUser, WebappData data, Timezone timezone);
}
