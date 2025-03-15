package picturebot.bot.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.GetFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class GetFileFactoryImplTest {

    @InjectMocks private GetFileFactoryImpl getFileFactory;

    @Nested
    @DisplayName("createGetFileMethod()")
    class CreateGetFileMethod {

        @Test
        @DisplayName("shouldReturnGetFileInstanceWithFileId")
        void shouldReturnGetFileInstanceWithFileId() {
            final GetFile expected = new GetFile();
            expected.setFileId("foo");

            final GetFile actual = getFileFactory.createGetFileMethod("foo");

            assertEquals(expected, actual);
        }
    }
}
