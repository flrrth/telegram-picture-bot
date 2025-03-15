package picturebot.picture.randomizer;

import java.util.Optional;
import java.util.Set;

public interface PictureRandomizer {

    /**
     * Chooses a random picture from the given set.
     * @param pictures the pictures to choose from
     * @return a random picture
     */
    Optional<String> getRandomPicture(Set<String> pictures);
}
