package picturebot.picture.greeting;

import picturebot.picture.exceptions.PictureException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.PropertyResourceBundle;

/**
 * This class is responsible for retrieving the greeting message from a message bundle. When the user's locale is not
 * supported, the default message bundle is used. Messages are cached to improve performance.
 */
@Component
class GreetingRetrieverImpl implements GreetingRetriever {

    private final Path basePath;

    /* default */ GreetingRetrieverImpl(final Path basePath) {
        this.basePath = basePath;
    }

    @Override
    @Cacheable(cacheNames = "greetingCache", cacheManager = "cacheManager")
    public String getGreeting(final Locale locale, final Path subPath) throws PictureException {
        Path messageBundlePath = basePath.resolve(subPath)
                .resolve(String.format("message_%s.properties", locale.getLanguage()));

        if (!Files.exists(messageBundlePath)) {
            messageBundlePath = basePath.resolve(subPath).resolve("message.properties");
        }

        try(final BufferedReader reader = Files.newBufferedReader(messageBundlePath, java.nio.charset.StandardCharsets.UTF_8)) {
            final PropertyResourceBundle messages = new PropertyResourceBundle(reader);
            return messages.getString("greeting");
        }
        catch (final IOException e) {
            throw new PictureException("Could not load message bundle: " + messageBundlePath, e);
        }
    }
}
