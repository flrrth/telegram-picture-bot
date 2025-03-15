package picturebot.entities.botuser;

import picturebot.entities.botuserdetails.BotUserDetails;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;

@Component
public class BotUserFactoryImpl implements BotUserFactory {

    @Override
    public BotUser createBotUser(final User user, final LocalDateTime firstSeen, final LocalDateTime lastSeen) {
        final BotUser botUser = new BotUser(user.getId(), firstSeen, lastSeen);

        botUser.getUserDetails().add(new BotUserDetails(lastSeen, user.getUserName(), user.getFirstName(),
                user.getLastName(), user.getIsBot(), user.getLanguageCode(), botUser));

        return botUser;
    }
}
