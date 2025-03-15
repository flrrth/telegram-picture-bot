package picturebot.picture.path;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import picturebot.entities.timezone.Timezone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PicturePathRetrieverImplTest {
    private final FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());
    private final Path basePath = fileSystem.getPath("/pictures");
    private final Path regular = fileSystem.getPath("regular");

    @Mock private Environment environment;
    @Mock private Timezone userTimezone;

    private PicturePathRetrieverImpl picturePathRetriever;

    @Nested
    @DisplayName("getDailyPicturePath()")
    class GetDailyPicturePath {
        private final LocalDateTime newYearsDay = LocalDateTime.of(2024, 1, 1, 0, 0);
        private final Clock fixedClock = Clock.fixed(
                newYearsDay.atZone(ZoneId.of("Europe/Amsterdam")).toInstant(),
                ZoneId.of("Europe/Amsterdam"));

        @BeforeEach
        void initializePicturePathRetriever() {
            picturePathRetriever = new PicturePathRetrieverImpl(environment, fileSystem, fixedClock, basePath);
        }

        @Test
        @DisplayName("should return regular picture path, because today is not a special day")
        void shouldReturnRegularPicturePathBecauseTodayIsNotSpecialDay() {
            when(userTimezone.getCity()).thenReturn("Europe/Amsterdam");
            when(environment.getRequiredProperty("bot.regular.subPath")).thenReturn("regular");

            assertEquals(regular, picturePathRetriever.getDailyPicturePath(userTimezone));
        }

        @Test
        @DisplayName("should return special picture path, because today is New Year's Day")
        void shouldReturnSpecialPicturePathBecauseTodayIsNewYearsDay() throws IOException {
            Files.createDirectories(basePath.resolve("1").resolve("1"));
            when(userTimezone.getCity()).thenReturn("Europe/Amsterdam");

            assertEquals(fileSystem.getPath("1", "1"), picturePathRetriever.getDailyPicturePath(userTimezone));
        }

        @Test
        @DisplayName("should return special picture path for unique event")
        void shouldReturnSpecialPicturePathForUniqueEvent() throws IOException {
            Files.createDirectories(basePath.resolve("2024").resolve("1").resolve("1"));
            when(userTimezone.getCity()).thenReturn("Europe/Amsterdam");

            assertEquals(fileSystem.getPath("2024","1", "1"),
                    picturePathRetriever.getDailyPicturePath(userTimezone));
        }

        @Test
        @DisplayName("should return regular picture path, because it is still New Year's Eve in London")
        void shouldReturnRegularPicturePathBecauseItIsStillNewYearsEveInLondon() throws IOException {
            Files.createDirectories(basePath.resolve("1").resolve("1"));
            when(userTimezone.getCity()).thenReturn("Europe/London");
            when(environment.getRequiredProperty("bot.regular.subPath")).thenReturn("regular");

            assertEquals(regular, picturePathRetriever.getDailyPicturePath(userTimezone));
        }
    }

    @Nested
    @DisplayName("getRegularPicturePath()")
    class GetRegularPicturePath {
        private final LocalDateTime regularDay = LocalDateTime.of(2024, 8, 16, 12, 0);
        private final Clock fixedClock = Clock.fixed(
                regularDay.atZone(ZoneId.of("Europe/Amsterdam")).toInstant(),
                ZoneId.systemDefault());

        @BeforeEach
        void initializePicturePathRetriever() {
            picturePathRetriever = new PicturePathRetrieverImpl(environment, fileSystem, fixedClock, basePath);
        }

        @Test
        @DisplayName("should return regular picture path")
        void shouldReturnRegularPicturePath() {
            when(environment.getRequiredProperty("bot.regular.subPath")).thenReturn("regular");
            assertEquals(regular, picturePathRetriever.getRegularPicturePath());
        }
    }
}
