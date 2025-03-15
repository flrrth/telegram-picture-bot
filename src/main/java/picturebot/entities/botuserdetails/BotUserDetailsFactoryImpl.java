package picturebot.entities.botuserdetails;

import picturebot.entities.botuser.BotUser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BotUserDetailsFactoryImpl implements BotUserDetailsFactory {

    @Override
    public BotUserDetails createBotUserDetails(final LocalDateTime timestamp, final String userName,
                                               final String firstName, final String lastName, final Boolean isBot,
                                               final String languageCode, final BotUser botUser) {

        return new BotUserDetails(timestamp, userName, firstName, lastName, isBot, languageCode, botUser);
    }
}
