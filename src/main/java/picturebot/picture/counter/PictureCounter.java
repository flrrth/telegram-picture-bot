package picturebot.picture.counter;

/**
 * Implementations should return the number of pictures inside the given directory.
 */
public interface PictureCounter {

    /**
     * Returns the number of pictures inside the given subdirectory.
     * @param subdirectory the subdirectory relative to the base path.
     * @return the number of pictures inside the given subdirectory.
     */
    int count(final String subdirectory);
}
