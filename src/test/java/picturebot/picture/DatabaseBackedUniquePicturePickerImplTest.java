package picturebot.picture;

import picturebot.entities.DailyPicture;
import picturebot.picture.exceptions.PictureException;
import picturebot.picture.randomizer.PictureRandomizer;
import picturebot.picture.walker.PictureWalker;
import picturebot.repositories.DailyPictureRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class DatabaseBackedUniquePicturePickerImplTest {

    private static final LocalDateTime NOON = LocalDateTime.of(2024, 1, 2, 12, 0);
    private final Clock fixedClock = Clock.fixed(NOON.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock private PictureWalker pictureWalker;
    @Mock private PictureRandomizer pictureRandomizer;
    @Mock private FileSystem fileSystem;
    @Mock private Path basePath;
    @Mock private DailyPictureRepository dailyPictureRepository;
    @Mock private DailyPicture dailyPicture;
    @Mock private Path subPath;

    private List<DailyPicture> previouslySharedPictures;
    private Set<String> pictureSet;

    private DatabaseBackedUniquePicturePickerImpl picturePicker;

    @BeforeEach
    void setUp() {
        picturePicker = new DatabaseBackedUniquePicturePickerImpl(pictureWalker, pictureRandomizer, fileSystem,
                basePath, dailyPictureRepository, fixedClock);
    }

    @Nested
    @DisplayName("getPicture()")
    class GetPicture {

        @Test
        @DisplayName("should return picture that was selected earlier today")
        void getPictureShouldReturnPictureThatWasSelectedEarlierToday() throws PictureException {
            when(dailyPicture.getPath()).thenReturn("bar/random.jpg");
            when(basePath.resolve("bar/random.jpg")).thenReturn(Path.of("/bar/random.jpg"));
            when(dailyPictureRepository.findByDateUsed(LocalDate.now(fixedClock)))
                    .thenReturn(Optional.of(dailyPicture));

            final Optional<String> picture = picturePicker.getPicture("bar");

            assertTrue(picture.isPresent());
            assertEquals("/bar/random.jpg", picture.get());
            verify(dailyPictureRepository, never()).save(any(DailyPicture.class));
        }

        @Test
        @DisplayName("should return new picture and store path of selected picture")
        void getPictureShouldReturnNewPictureAndStorePathOfSelectedPicture() throws PictureException {
            when(dailyPictureRepository.findByDateUsed(LocalDate.now(fixedClock))).thenReturn(Optional.empty());
            previouslySharedPictures = new ArrayList<>();
            when(dailyPictureRepository.findAll()).thenReturn(previouslySharedPictures);
            pictureSet = Set.of("/bar/random.jpg");
            when(fileSystem.getPath("bar")).thenReturn(subPath);
            when(pictureWalker.walk(basePath, subPath)).thenReturn(pictureSet);
            when(pictureRandomizer.getRandomPicture(pictureSet)).thenReturn(Optional.of("bar/random.jpg"));
            when(basePath.resolve("bar/random.jpg")).thenReturn(Path.of("/bar/random.jpg"));

            final Optional<String> picture = picturePicker.getPicture("bar");

            assertTrue(picture.isPresent());
            assertEquals("/bar/random.jpg", picture.get());
            verify(dailyPictureRepository).save(new DailyPicture("bar/random.jpg"));
        }


        @Test
        @DisplayName("should not return picture because no pictures where found")
        void getPictureShouldNotReturnPictureBecauseNoPicturesWhereFound() throws PictureException {
            when(dailyPictureRepository.findByDateUsed(LocalDate.now(fixedClock))).thenReturn(Optional.empty());
            previouslySharedPictures = new ArrayList<>();
            when(dailyPictureRepository.findAll()).thenReturn(previouslySharedPictures);
            pictureSet = Set.of();
            when(fileSystem.getPath("bar")).thenReturn(subPath);
            when(pictureWalker.walk(basePath, subPath)).thenReturn(pictureSet);

            final Optional<String> picture = picturePicker.getPicture("bar");

            assertTrue(picture.isEmpty());
            verify(dailyPictureRepository, never()).save(any(DailyPicture.class));
        }

        @Test
        @DisplayName("should not return picture because all pictures were previously shared")
        void getPictureShouldNotReturnPictureBecauseAllPicturesWerePreviouslyShared() throws PictureException {
            when(dailyPictureRepository.findByDateUsed(LocalDate.now(fixedClock))).thenReturn(Optional.empty());
            previouslySharedPictures = List.of(new DailyPicture("bar/random.jpg"));
            when(dailyPictureRepository.findAll()).thenReturn(previouslySharedPictures);
            pictureSet = Set.of("bar/random.jpg");
            when(fileSystem.getPath("bar")).thenReturn(subPath);
            when(pictureWalker.walk(basePath, subPath)).thenReturn(pictureSet);

            final Optional<String> picture = picturePicker.getPicture("bar");

            assertTrue(picture.isEmpty());
            verify(dailyPictureRepository, never()).save(any(DailyPicture.class));
        }
    }
}
