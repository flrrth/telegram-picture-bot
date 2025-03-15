package picturebot.bot.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GetUserProfilePhotosFactoryImplTest {

    @Nested
    @DisplayName("createGetUserProfilePhotos()")
    class CreateGetUserProfilePhotos {

        @Test
        @DisplayName("should return GetUserProfilePhotos instance")
        void shouldReturnGetUserProfilePhotosInstance() {
            final GetUserProfilePhotosFactoryImpl factory = new GetUserProfilePhotosFactoryImpl();
            final GetUserProfilePhotos result = factory.createGetUserProfilePhotos(1L);
            assertNotNull(result);
            assertEquals(1L, result.getUserId());
        }
    }
}
