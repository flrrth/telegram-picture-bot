package picturebot.bot.command.webappdata;

import picturebot.entities.botuser.BotUser;
import picturebot.entities.settings.Settings;
import picturebot.entities.timezone.Timezone;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * This class is responsible for updating the settings of a bot user.
 */
@Component
class SettingsUpdaterImpl implements SettingsUpdater {

    private final Clock clock;

    /* default */ SettingsUpdaterImpl(final Clock clock) {
        this.clock = clock;
    }

    @Override
    public Settings updateSettings(final BotUser botUser, final WebappData data, final Timezone timezone) {
        final Settings settings;

        if (botUser.getSettings() == null) {
            settings = new Settings(data.getIsEnabled(),
                    data.getSchedule(),
                    data.getIsSpoilerEnabled(),
                    botUser,
                    LocalDateTime.now(clock),
                    timezone);
        }
        else {
            settings = botUser.getSettings();
            settings.setSchedule(data.getSchedule());
            settings.setIsEnabled(data.getIsEnabled());
            settings.setIsSpoilerEnabled(data.getIsSpoilerEnabled());
            settings.setLastModified(LocalDateTime.now(clock));
            settings.setTimezone(timezone);
        }

        return settings;
    }
}
