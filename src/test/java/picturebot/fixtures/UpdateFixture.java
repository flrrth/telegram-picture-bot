package picturebot.fixtures;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * Fixture class to create Update objects for testing purposes.
 */
public final class UpdateFixture {

    private UpdateFixture() { 
        // private constructor to hide the implicit public one
    }
    
    /**
     * Creates a basic Update object with user information.
     *
     * @param languageCode the user's language code
     * @return a new Update instance
     */
    public static Update createBasicUpdate(final String languageCode) {
        final User user = new User();
        user.setId(6L);
        user.setUserName("john_d");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setIsBot(false);
        user.setLanguageCode(languageCode);

        final PhotoSize photoSize1 = new PhotoSize();
        photoSize1.setFileSize(100);
        photoSize1.setFileId("photo1");

        final PhotoSize photoSize2 = new PhotoSize();
        photoSize2.setFileSize(200);
        photoSize2.setFileId("photo2");

        final Message message = new Message();
        message.setFrom(user);
        message.setPhoto(List.of(photoSize1, photoSize2));

        final Update update = new Update();
        update.setMessage(message);

        return update;
    }

    /**
     * Creates an Update object with a text message.
     *
     * @param languageCode the user's language code
     * @param text the message text
     * @return a new Update instance
     */
    public static Update createTextUpdate(final String languageCode, final String text) {
        final Update update = createBasicUpdate(languageCode);
        update.getMessage().setText(text);
        
        return update;
    }
}
