package picturebot.bot.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class SendPhotoFactoryImplTest {

    private final SendPhotoFactoryImpl factory = new SendPhotoFactoryImpl();

    @Nested
    @DisplayName("getSendPhoto()")
    class GetSendPhoto {

        @Test
        @DisplayName("should return new SendPhoto instance")
        void shouldReturnNewSendPhotoInstance() {
            assertInstanceOf(SendPhoto.class, factory.getSendPhoto());
        }

        @Test
        @DisplayName("should return configured SendPhoto instance with chat ID, photo and spoiler")
        void shouldReturnConfiguredSendPhotoInstanceWithChatIdAndPhotoAndSpoiler() {
            final SendPhoto actual = factory.getSendPhoto(1L, new InputFile("dog.jpg"), true);

            // SendPhoto doesn't seem to have a custom equals method, so fields need to be compared one by one:
            assertEquals("1", actual.getChatId());
            assertEquals("dog.jpg", actual.getPhoto().getAttachName());
            assertTrue(actual.getHasSpoiler());
        }

        @Test
        @DisplayName("should return configured SendPhoto instance with chat ID, photo, spoiler and")
        void shouldReturnConfiguredSendPhotoInstanceWithChatIdAndPhotoAndSpoilerAnd() {
            final SendPhoto actual = factory.getSendPhoto(1L, new InputFile("dog.jpg"), true,
                    "Hello World!");

            // SendPhoto doesn't seem to have a custom equals method, so fields need to be compared one by one:
            assertEquals("1", actual.getChatId());
            assertEquals("dog.jpg", actual.getPhoto().getAttachName());
            assertTrue(actual.getHasSpoiler());
            assertEquals(actual.getCaption(), "Hello World!");
        }
    }
}
