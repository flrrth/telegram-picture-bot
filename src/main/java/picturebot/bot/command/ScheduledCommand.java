package picturebot.bot.command;

import picturebot.entities.botuser.BotUser;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Implementations should send a message to the provided user.
 */
public interface ScheduledCommand {

    void send(TelegramClient telegramClient, BotUser botUser) throws TelegramApiException;
}
