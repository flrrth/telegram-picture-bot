package picturebot.picture;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import picturebot.picture.exceptions.PictureException;
import picturebot.picture.randomizer.PictureRandomizer;
import picturebot.picture.walker.PictureWalker;

/**
 * This class is responsible for picking a random picture from a given sub-path.
 */
@Service
class RandomPicturePickerImpl implements PicturePicker {

    private final PictureWalker pictureWalker;
    private final PictureRandomizer pictureRandomizer;
    private final FileSystem fileSystem;
    private final Path basePath;

    public RandomPicturePickerImpl(final PictureWalker pictureWalker, final PictureRandomizer pictureRandomizer,
                                   final FileSystem fileSystem, final Path basePath) {

        this.pictureWalker = pictureWalker;
        this.pictureRandomizer = pictureRandomizer;
        this.fileSystem = fileSystem;
        this.basePath = basePath;
    }

    /**
     * Selects a random picture from the given sub-path.
     * @param subPath the sub path that will be used to select an appropriate picture
     * @return the path to the selected picture
     * @throws PictureException thrown when an error occurs while selecting the picture
     */
    @Override
    public Optional<String> getPicture(final String subPath) throws PictureException {
        final Set<String> pictures = this.pictureWalker.walk(basePath, this.fileSystem.getPath(subPath));
        final Optional<String> randomPicture = this.pictureRandomizer.getRandomPicture(pictures);

        if (randomPicture.isPresent()) {
            return randomPicture.map(picture -> basePath.resolve(picture).toString());
        }

        return Optional.empty();
    }
}
