package picturebot.bot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Implementations should respond to a specific command given to the bot by the user.
 */
public interface BotCommand {

    void respond(TelegramClient telegramClient, Update update) throws TelegramApiException;
}
