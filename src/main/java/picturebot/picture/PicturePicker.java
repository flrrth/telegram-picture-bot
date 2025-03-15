package picturebot.picture;

import picturebot.picture.exceptions.PictureException;

import java.util.Optional;

public interface PicturePicker {

    /**
     * Returns a random picture.
     * @param subPath the sub path that will be used to select an appropriate picture
     * @return The path to a picture
     * @throws PictureException thrown when an error occurs while selecting a picture
     */
    Optional<String> getPicture(String subPath) throws PictureException;
}
