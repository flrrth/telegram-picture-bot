package picturebot.picture.greeting;

import picturebot.picture.exceptions.PictureException;

import java.nio.file.Path;
import java.util.Locale;

public interface GreetingRetriever {

    /**
     * Gets the greeting in the language of the user if available, otherwise the default English greeting is returned.
     * @param locale the locale of the user
     * @param subPath the path to the directory where the appropriate images are stored
     * @return the greeting
     */
    String getGreeting(Locale locale, Path subPath) throws PictureException;
}
