package picturebot.fixtures;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import picturebot.entities.botuser.BotUser;
import picturebot.entities.botuserdetails.BotUserDetails;
import picturebot.entities.settings.Settings;
import picturebot.entities.timezone.Timezone;

/**
 * A fixture class for creating instances of {@link BotUser} for testing purposes.
 * This class provides methods to create basic bot users and bot users with settings.
 * The created bot users have a fixed clock set to noon on January 1, 2024, in the Europe/Amsterdam time zone.
 */
public final class BotUserFixture {

    private static final LocalDateTime NOON = LocalDateTime.of(2024, 1, 1, 12, 0);
    private static final Clock FIXED_CLOCK = Clock.fixed(NOON.atZone(ZoneId.of("Europe/Amsterdam")).toInstant(),
            ZoneId.of("Europe/Amsterdam"));
    
    private BotUserFixture() { 
        // private constructor to hide the implicit public one
    }

    /**
     * Creates a basic BotUser with the specified language code.
     *
     * @param languageCode the language code to be set for the BotUserDetails
     * @return a BotUser instance with initialized details and default values
     */
    public static BotUser createBasicBotUser(final String languageCode) {
        final BotUser botUser = new BotUser(6L, LocalDateTime.now(FIXED_CLOCK), LocalDateTime.now(FIXED_CLOCK));
        botUser.setRequestCount(0);
        botUser.setHasBlockedBot(false);

        final BotUserDetails botUserDetails = new BotUserDetails(LocalDateTime.now(FIXED_CLOCK), 
        "john_d", "John", "Doe", false, languageCode, botUser);
        botUser.getUserDetails().add(botUserDetails);

        return botUser;
    }

    /**
     * Creates a BotUser with specific settings.
     *
     * @param languageCode the language code to be used for the BotUser
     * @return a BotUser instance with the specified settings
     */
    public static BotUser createBotUserWithSettings(final String languageCode) {
        final BotUser botUser = createBasicBotUser(languageCode);
        final Timezone timezone = new Timezone(1L, "Europe/Amsterdam");
        final Settings settings = new Settings(false, LocalDateTime.now(FIXED_CLOCK).toLocalTime(),
        false, botUser, LocalDateTime.now(FIXED_CLOCK), timezone);

        botUser.setSettings(settings);

        return botUser;
    }
}
