package picturebot.picture.greeting;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import picturebot.picture.exceptions.PictureException;
import org.junit.jupiter.api.*;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GreetingRetrieverImplTest {

    private final FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());
    private final Path basePath = fileSystem.getPath("/pictures");
    private final Path subPath = fileSystem.getPath("regular");

    private final GreetingRetrieverImpl greetingRetriever = new GreetingRetrieverImpl(basePath);

    @BeforeEach
    void createResourceBundles() throws IOException {
        final Path path = basePath.resolve(subPath);
        Files.createDirectories(path);
        Files.write(path.resolve("message.properties"),
                List.of("greeting=Hello World!"), StandardOpenOption.CREATE);
        Files.write(path.resolve("message_nl.properties"),
                List.of("greeting=Hallo Wereld!"), StandardOpenOption.CREATE);
    }

    @AfterEach
    void cleanUpFileSystem() throws IOException {
        FileSystemUtils.deleteRecursively(basePath);
    }

    @Nested
    @DisplayName("getGreeting()")
    class GetGreeting {

        @Test
        @DisplayName("should return default greeting")
        void shouldReturnDefaultGreeting() throws PictureException {
            assertEquals("Hello World!",
                    greetingRetriever.getGreeting(Locale.ENGLISH, subPath));
        }

        @Test
        @DisplayName("should return Dutch greeting")
        void shouldReturnDutchGreeting() throws PictureException {
            assertEquals("Hallo Wereld!",
                    greetingRetriever.getGreeting(new Locale("nl"), subPath));
        }

        @Test
        @DisplayName("should return default greeting, because Russian isn't available")
        void shouldReturnDefaultGreetingBecauseRussianIsNotAvailable() throws PictureException {
            assertEquals("Hello World!",
                    greetingRetriever.getGreeting(new Locale("ru"), subPath));
        }

        @Test
        @DisplayName("should throw PictureException when message bundle is not available")
        void shouldThrowPictureExceptionWhenMessageBundleIsNotAvailable() throws IOException {
            FileSystemUtils.deleteRecursively(basePath);

            // Act & Assert:
            final PictureException exception = Assertions.assertThrows(PictureException.class,
                    () -> greetingRetriever.getGreeting(Locale.ENGLISH, subPath));

            assertEquals("Could not load message bundle: /pictures/regular/message.properties",
                    exception.getMessage());
        }
    }
}
