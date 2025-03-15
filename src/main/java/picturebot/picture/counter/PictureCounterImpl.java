package picturebot.picture.counter;

import picturebot.picture.exceptions.PictureException;
import picturebot.picture.walker.PictureWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 * This class is responsible for counting the pictures in a given subdirectory.
 */
@Component
class PictureCounterImpl implements PictureCounter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureCounterImpl.class);

    private final Path basePath;
    private final PictureWalker pictureWalker;
    private final FileSystem fileSystem;

    /* default */ PictureCounterImpl(final Path basePath, final PictureWalker pictureWalker,
                                     final FileSystem fileSystem) {
        this.basePath = basePath;
        this.pictureWalker = pictureWalker;
        this.fileSystem = fileSystem;
    }

    @Override
    public int count(final String subdirectory) {
        try {
            return pictureWalker.walk(
                    basePath,
                    fileSystem.getPath(subdirectory)).size();
        }
        catch (PictureException e) {
            LOGGER.error("Could not count the pictures in directory '{}'", subdirectory, e);
            return 0;
        }
    }
}
