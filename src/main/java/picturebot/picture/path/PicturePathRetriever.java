package picturebot.picture.path;

import picturebot.entities.timezone.Timezone;

import java.nio.file.Path;

/**
 * Component that produces the picture path.
 */
public interface PicturePathRetriever {

    /**
     * Produces a picture path based on the current date. It takes the timezone of the user into account.
     * @param userTimezone the timezone of the user
     * @return the path that applies to the current day, or the 'regular' path if no applicable directory was found
     */
    Path getDailyPicturePath(Timezone userTimezone);

    /**
     * Produces the 'regular' picture path regardless of the current day.
     * @return the regular picture path
     */
    Path getRegularPicturePath();
}
