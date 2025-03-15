package picturebot.bot.user;

import picturebot.repositories.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is responsible for saving the ID of the settings message. It is saved, because it is used to clean up
 * the custom keyboard after the user returns from the web application.
 */
@Component
public class SettingsMessageIdSaverImpl implements SettingsMessageIdSaver {

    private final UserRepository userRepository;

    public SettingsMessageIdSaverImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void saveSettingsMessageId(final Long userId, final int messageId) {
        userRepository.saveMessageId(userId, messageId);
    }
}
