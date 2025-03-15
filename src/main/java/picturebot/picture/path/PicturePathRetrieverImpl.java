package picturebot.picture.path;

import picturebot.entities.timezone.Timezone;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * This class is responsible for retrieving the path to a picture.
 */
@Component
class PicturePathRetrieverImpl implements PicturePathRetriever {

    private final Environment environment;
    private final FileSystem fileSystem;
    private final Clock clock;
    private final Path basePath;

    /* default */ PicturePathRetrieverImpl(final Environment environment,
                                    final FileSystem fileSystem,
                                    final Clock clock,
                                    final Path basePath) {

        this.environment = environment;
        this.fileSystem = fileSystem;
        this.clock = clock;
        this.basePath = basePath;
    }

    /**
     * Returns the path to the daily picture. If there is a special picture for the current day, it will be returned.
     * Otherwise, the regular picture will be returned.
     * @param userTimezone the timezone of the user
     * @return the path to the daily picture
     */
    @Override
    public Path getDailyPicturePath(final Timezone userTimezone) {
        final ZonedDateTime now = ZonedDateTime.now(clock).withZoneSameInstant(ZoneId.of(userTimezone.getCity()));

        // Create a sub path that does not include the year, such paths are for yearly events like Christmas:
        Path subPath = fileSystem.getPath(
                String.valueOf(now.getMonth().getValue()),
                String.valueOf(now.getDayOfMonth())
        );

        if (Files.isDirectory(basePath.resolve(subPath))) {
            return subPath;
        }
        else {
            // Create a sub path that includes the year, such paths are for unique events:
            subPath = fileSystem.getPath(
                    String.valueOf(now.getYear()),
                    String.valueOf(now.getMonth().getValue()),
                    String.valueOf(now.getDayOfMonth()));

            if (Files.isDirectory(basePath.resolve(subPath))) {
                return subPath;
            }
        }

        return getRegularPicturePath();
    }

    /**
     * Returns the path to the regular picture.
     * @return the path to the regular picture
     */
    @Override
    public Path getRegularPicturePath() {
        return fileSystem.getPath(environment.getRequiredProperty("bot.regular.subPath"));
    }
}
