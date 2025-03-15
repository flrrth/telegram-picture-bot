package picturebot.bot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Implementations should respond to a specific command given to the bot by the user.
 */
public interface BotCommand {

    void respond(AbsSender bot, Update update) throws TelegramApiException;
}
