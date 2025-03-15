package picturebot.bot.user;

import picturebot.entities.botuser.BotUser;
import org.telegram.telegrambots.meta.api.objects.User;

public interface BotUserUpdater {

    /**
     * Creates a new record for the given user or updates an already existing one.
     */
    BotUser createOrUpdateUser(User user);
}
