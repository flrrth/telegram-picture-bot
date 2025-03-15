package picturebot.fixtures;

import org.telegram.telegrambots.meta.api.objects.User;

public final class UserFixture {
    
    private UserFixture() { 
        // private constructor to hide the implicit public one
    }

    public static User createBasicUser(final String languageCode) {
        final User user = new User();

        user.setId(6L);
        user.setUserName("john_d");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setIsBot(false);
        user.setLanguageCode(languageCode);

        return user;
    }
}
