package picturebot.entities.botuserdetails;

import picturebot.entities.botuser.BotUser;

import java.time.LocalDateTime;

public interface BotUserDetailsFactory {

    BotUserDetails createBotUserDetails(LocalDateTime timestamp, String userName, String firstName, String lastName,
                                        Boolean isBot, String languageCode, BotUser botUser);
}
