package picturebot.picture.exceptions;

import java.io.Serial;

/**
 * Thrown when an error occurs related to picture operations.
 */
public class PictureException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public PictureException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
