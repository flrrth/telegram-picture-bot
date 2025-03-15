package picturebot.picture.randomizer;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Component;

/**
 * This class is responsible for selecting a random picture from a set of pictures.
 */
@Component
public class PictureRandomizerImpl implements PictureRandomizer {

    private final Random random;

    public PictureRandomizerImpl(final Random random) {
        this.random = random;
    }

    /**
     * Selects a random picture from the provided set of pictures.
     *
     * @param pictures the set of available pictures
     * @return an Optional containing the selected picture, or empty if the input is null or empty
     */
    @Override
    public Optional<String> getRandomPicture(final Set<String> pictures) {  
        if (pictures == null || pictures.isEmpty()) {
            return Optional.empty();
        }
        
        final int index = random.nextInt(pictures.size());
        return Optional.of(pictures.stream().skip(index).findFirst().get());
    }
}
