package picturebot.picture.walker;

import picturebot.picture.exceptions.PictureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is responsible for gathering all pictures in a given directory and its subdirectories.
 */
@Component
public class PictureWalkerImpl implements PictureWalker {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureWalkerImpl.class);

    @Override
    @Cacheable(cacheNames = "pictureCache", cacheManager = "cacheManager")
    public Set<String> walk(final Path pictureRootDirectory, final Path subPath) throws PictureException {
        LOGGER.info("Gathering the pictures in {}.", pictureRootDirectory.resolve(subPath));

        try (Stream<Path> stream = Files.walk(pictureRootDirectory.resolve(subPath))) {
            return stream.filter(Files::isRegularFile)
                    .filter(path -> !path.getFileName().toString().endsWith(".properties"))
                    .map(path -> path.subpath(pictureRootDirectory.getNameCount(), path.getNameCount()).toString())
                    .collect(Collectors.toSet());
        }
        catch (IOException e) {
            throw new PictureException("Could not read pictures from '" +
                    pictureRootDirectory.resolve(subPath) + "'.", e);
        }
    }
}
