package picturebot.picture;

import picturebot.entities.DailyPicture;
import picturebot.picture.exceptions.PictureException;
import picturebot.picture.randomizer.PictureRandomizer;
import picturebot.picture.walker.PictureWalker;
import picturebot.repositories.DailyPictureRepository;
import org.springframework.stereotype.Component;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A PicturePicker implementation that retrieves a picture from the database. If a picture has already been used for
 * the current date, it returns that picture. Otherwise, it selects a new picture that hasn't been used before.
 */
@Component
public class DatabaseBackedUniquePicturePickerImpl implements PicturePicker {

    private final PictureWalker pictureWalker;
    private final PictureRandomizer pictureRandomizer;
    private final FileSystem fileSystem;
    private final Path basePath;
    private final DailyPictureRepository dailyPictureRepository;
    private final Clock clock;


    public DatabaseBackedUniquePicturePickerImpl(final PictureWalker pictureWalker,
                                                 final PictureRandomizer pictureRandomizer,
                                                 final FileSystem fileSystem,
                                                 final Path basePath,
                                                 final DailyPictureRepository dailyPictureRepository,
                                                 final Clock clock) {

        this.pictureWalker = pictureWalker;
        this.pictureRandomizer = pictureRandomizer;
        this.fileSystem = fileSystem;
        this.basePath = basePath;
        this.dailyPictureRepository = dailyPictureRepository;
        this.clock = clock;
    }

    /**
     * Retrieves a picture for the given subPath. If a picture has already been used for the current date,
     * it returns that picture. Otherwise, it selects a new picture that hasn't been used before.
     * @param subPath the subdirectory path to search for pictures
     * @return an Optional containing the path to the picture if found, otherwise an empty Optional
     * @throws PictureException if an error occurs while retrieving the picture
     */
    @Override
    public Optional<String> getPicture(final String subPath) throws PictureException {
        final LocalDate localDate = LocalDate.now(clock);
        final Optional<DailyPicture> dailyPicture = dailyPictureRepository.findByDateUsed(localDate);

        if (dailyPicture.isPresent()) {
            return Optional.of(basePath.resolve(dailyPicture.get().getPath()).toString());
        }
        else {
            List<DailyPicture> previouslySharedPictures = dailyPictureRepository.findAll();

            final Set<String> pictures = this.pictureWalker.walk(basePath, this.fileSystem.getPath(subPath)).stream()
                    .filter(picture -> !previouslySharedPictures.contains(new DailyPicture(picture)))
                    .collect(Collectors.toSet());

            final Optional<String> randomPicture = this.pictureRandomizer.getRandomPicture(pictures);

            if (randomPicture.isPresent()) {
                dailyPictureRepository.save(new DailyPicture(randomPicture.get()));
                return Optional.of(basePath.resolve(randomPicture.get()).toString());
            }

            return Optional.empty();
        }
    }
}
