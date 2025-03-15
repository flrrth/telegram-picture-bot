package picturebot.bot.user;

public interface SettingsMessageIdSaver {

    /**
     * Saves the message ID of the message that contained the Settings KeyboardButton. The ID is stored, so that the
     * button can be removed after the user closes the webapp.
     * @param userId the user ID
     * @param messageId the message ID
     */
    void saveSettingsMessageId(Long userId, int messageId);
}
