package picturebot.picture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import picturebot.picture.exceptions.PictureException;
import picturebot.picture.randomizer.PictureRandomizer;
import picturebot.picture.walker.PictureWalker;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class RandomPicturePickerImplTest {

    private final Set<String> pictureSet = Set.of(
            "testing/cat.jpg",
            "testing/dog.jpg",
            "testing/duck.jpg");
    private final Set<String> emptyPictureSet = Set.of();

    @Mock private PictureWalker walker;
    @Mock private PictureRandomizer randomizer;
    @Mock private FileSystem fileSystem;
    @Mock private Path basePath;

    @InjectMocks private RandomPicturePickerImpl picker;

    @Nested
    @DisplayName("getPicture()")
    class GetPicture {
        @Test
        @DisplayName("should return a picture, because pictures are available")
        void getPictureShouldReturnPictureWhenPicturesAreAvailable() throws PictureException {
            final Path subPath = Mockito.mock(Path.class);
            when(fileSystem.getPath("testing")).thenReturn(subPath);
            when(walker.walk(basePath, subPath)).thenReturn(pictureSet);
            when(randomizer.getRandomPicture(pictureSet)).thenReturn(Optional.of("testing/dog.jpg"));
            when(basePath.resolve("testing/dog.jpg")).thenReturn(Path.of("/testing/dog.jpg"));

            Optional<String> picture = picker.getPicture("testing");

            assertTrue(picture.isPresent());
            assertEquals("/testing/dog.jpg", picture.get());
        }

        @Test
        @DisplayName("shouldn't return a picture, because there are no pictures available")
        void getPictureShouldReturnNoPictureWhenPicturesAreNotAvailable() throws PictureException {
            final Path subPath = Mockito.mock(Path.class);
            when(fileSystem.getPath("testing")).thenReturn(subPath);
            when(walker.walk(basePath, subPath)).thenReturn(emptyPictureSet);

            Optional<String> picture = picker.getPicture("testing");

            assertFalse(picture.isPresent());
        }
    }
}
