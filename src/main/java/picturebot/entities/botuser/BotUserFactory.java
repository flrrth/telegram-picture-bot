package picturebot.entities.botuser;

import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;

public interface BotUserFactory {

    BotUser createBotUser(User user, LocalDateTime firstSeen, LocalDateTime lastSeen);
}
