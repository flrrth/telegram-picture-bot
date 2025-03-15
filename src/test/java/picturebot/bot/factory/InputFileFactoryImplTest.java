package picturebot.bot.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@Tag("unit")
class InputFileFactoryImplTest {

    private final InputFileFactoryImpl factory = new InputFileFactoryImpl();

    @Nested
    @DisplayName("getInputFile()")
    class GetInputFile {

        @Test
        @DisplayName("should return InputFile instance when provided with String argument")
        void getInputFileShouldReturnInputFileInstanceWhenProvidedWithStringArgument() {
            assertInstanceOf(InputFile.class, factory.getInputFile("dog.jpg"));
        }

        @Test
        @DisplayName("should return InputFile instance when provided with Path argument")
        void getInputFileShouldReturnInputFileInstanceWhenProvidedWithPathArgument() {
            assertInstanceOf(InputFile.class, factory.getInputFile(Path.of("dog.jpg")));
        }
    }
}
