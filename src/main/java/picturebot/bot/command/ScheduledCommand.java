package picturebot.bot.command;

import picturebot.entities.botuser.BotUser;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Implementations should send a message to the provided user.
 */
public interface ScheduledCommand {

    void send(AbsSender bot, BotUser botUser) throws TelegramApiException;
}
