package picturebot.bot.command.webappdata;

import picturebot.entities.botuser.BotUser;
import picturebot.entities.settings.Settings;
import picturebot.entities.timezone.Timezone;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class SettingsUpdaterImplTest {

    private static final LocalDateTime NOON = LocalDateTime.of(2024, 1, 2, 12, 0);
    private final Clock fixedClock = Clock.fixed(NOON.atZone(
            ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    private BotUser botUser;
    @Mock private Timezone timezone;
    @Mock private WebappData data;

    final private SettingsUpdaterImpl settingsUpdater = new SettingsUpdaterImpl(fixedClock);

    @Nested
    @DisplayName("updateSettings()")
    class UpdateSettings {

        @BeforeEach
        void setUp() {
            botUser = new BotUser();

            when(data.getIsEnabled()).thenReturn(true);
            when(data.getSchedule()).thenReturn(LocalTime.now(fixedClock));
            when(data.getIsSpoilerEnabled()).thenReturn(true);
        }

        @Test
        @DisplayName("should create new Settings object, populate it and assign it to BotUser")
        void shouldCreateNewSettingsObjectPopulateItAndAssignItToBotUser() {
            final Settings actualSettings = settingsUpdater.updateSettings(botUser, data, timezone);

            final Settings expectedSettings = new Settings(true, LocalTime.now(fixedClock),
                    true, botUser, LocalDateTime.now(fixedClock), timezone);
            assertThat(actualSettings, is(equalTo(expectedSettings)));
        }

        @Test
        @DisplayName("should update existing Settings object and update its fields")
        void shouldUpdateExistingSettingsObjectAndUpdateItsFields() {
            botUser.setSettings(new Settings(false, LocalTime.now(),
                    false, botUser, LocalDateTime.now(), Mockito.mock(Timezone.class)));

            final Settings actualSettings = settingsUpdater.updateSettings(botUser, data, timezone);

            final Settings expectedSettings = new Settings(true, LocalTime.now(fixedClock),
                    true, botUser, LocalDateTime.now(fixedClock), timezone);
            assertThat(actualSettings, is(equalTo(expectedSettings)));
        }
    }
}
