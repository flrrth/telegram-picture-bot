package picturebot.bot.user;

import picturebot.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class SettingsMessageIdSaverImplTest {

    @Mock private UserRepository userRepository;

    @InjectMocks private SettingsMessageIdSaverImpl settingsMessageIdSaver;

    @Nested
    @DisplayName("saveSettingsMessageId()")
    class SaveSettingsMessageId {

        @Test
        @DisplayName("should save the settingsMessageId")
        void shouldSaveTheSettingsMessageId() {
            settingsMessageIdSaver.saveSettingsMessageId(1L, 1);
            verify(userRepository).saveMessageId(1L, 1);
        }
    }
}
