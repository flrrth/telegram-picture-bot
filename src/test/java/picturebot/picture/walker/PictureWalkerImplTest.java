package picturebot.picture.walker;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import picturebot.picture.exceptions.PictureException;
import org.junit.jupiter.api.*;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unit")
class PictureWalkerImplTest {

    private final PictureWalkerImpl pictureWalker = new PictureWalkerImpl();

    private final FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());
    private final Path basePath = fileSystem.getPath("/");
    private final Path animalsPath = basePath.resolve("animals");

    @BeforeEach
    void populateFileSystemWithMockImages() throws IOException {
        Files.createFile(Files.createDirectories(animalsPath.resolve("cats")).resolve("cat.jpg"));
        Files.createFile(Files.createDirectories(animalsPath.resolve("cats")).resolve("message.properties"));
        Files.createFile(Files.createDirectories(animalsPath.resolve("dogs")).resolve("dog.jpg"));
        Files.createFile(Files.createDirectories(animalsPath.resolve("dogs")).resolve("message.properties"));
        Files.createFile(Files.createDirectories(animalsPath.resolve("mice")).resolve("mouse.jpg"));
        Files.createFile(Files.createDirectories(animalsPath.resolve("mice")).resolve("message.properties"));
    }

    @AfterEach
    void cleanUpFileSystem() throws IOException {
        FileSystemUtils.deleteRecursively(animalsPath);
    }

    @Nested
    @DisplayName("walk()")
    class Walk {

        @Test
        @DisplayName("should gather all pictures from the provided directory tree")
        void shouldGatherThreePicturesFromTheGivenRootDirectory() throws PictureException {
            final Set<String> pictures = pictureWalker.walk(basePath, fileSystem.getPath("animals"));

            assertEquals(3, pictures.size());
            assertTrue(pictures.contains(Path.of("animals","cats", "cat.jpg").toString()));
            assertTrue(pictures.contains(Path.of("animals","dogs", "dog.jpg").toString()));
            assertTrue(pictures.contains(Path.of("animals","mice", "mouse.jpg").toString()));
        }

        @Test
        @DisplayName("should throw PictureException when IOException is thrown")
        void shouldThrowPictureExceptionWhenIOExceptionIsThrown() {
            final PictureException exception = Assertions.assertThrows(PictureException.class,
                    () -> pictureWalker.walk(basePath, fileSystem.getPath("cars")));

            assertEquals("Could not read pictures from '/cars'.", exception.getMessage());
        }
    }
}
