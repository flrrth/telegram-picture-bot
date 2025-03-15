package picturebot.picture.counter;

import picturebot.picture.exceptions.PictureException;
import picturebot.picture.walker.PictureWalker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
class PictureCounterImplTest {

    @Mock private Path basePath;
    @Mock private PictureWalker pictureWalker;
    @Mock private FileSystem fileSystem;

    @InjectMocks private PictureCounterImpl pictureCounter;

    @Nested
    @DisplayName("count()")
    class Count {

        @Test
        @DisplayName("should return the number of pictures in the given directory")
        void shouldReturnTheNumberOfPicturesInTheGivenDirectory() throws PictureException {
            final Path subPath = Mockito.mock(Path.class);
            when(fileSystem.getPath("animals")).thenReturn(subPath);
            final Set<String> pictures = Set.of("cats/cat.jpg", "dogs/dog.jpg", "mice/mouse.jpg");
            when(pictureWalker.walk(basePath, subPath)).thenReturn(pictures);

            final int result = pictureCounter.count("animals");

            assertEquals(3, result);
        }

        @Test
        @DisplayName("should return zero when an error occurred while reading the pictures")
        void shouldReturnZeroWhenAnErrorOccurredWhileReadingThePictures(final CapturedOutput output) throws PictureException {
            final Path subPath = Mockito.mock(Path.class);
            when(fileSystem.getPath("animals")).thenReturn(subPath);
            when(pictureWalker.walk(basePath, subPath)).thenThrow(PictureException.class);

            final int result = pictureCounter.count("animals");

            assertEquals(0, result);
            assertThat(output.getOut(), containsString("ERROR - Could not count the pictures in directory 'animals'"));
        }
    }
}
