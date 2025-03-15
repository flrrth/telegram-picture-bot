package picturebot.picture.walker;

import picturebot.picture.exceptions.PictureException;

import java.nio.file.Path;
import java.util.Set;

public interface PictureWalker {

    /**
     * Returns all picture paths found under the given root directory.
     * @param pictureRootDirectory the root directory that contains all pictures
     * @param subPath the sub path of a specific picture library
     * @return A set with unique picture paths.
     * @throws PictureException thrown when an error occurs while collecting the picture paths.
     */
    Set<String> walk(Path pictureRootDirectory, Path subPath) throws PictureException;
}
