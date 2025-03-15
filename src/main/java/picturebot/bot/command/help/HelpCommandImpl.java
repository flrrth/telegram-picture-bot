package picturebot.bot.command.help;

import picturebot.bot.command.BotCommand;
import picturebot.bot.factory.SendMessageFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

/**
 * This command gets executed when the user sends the /help command and explains how to use the bot.
 */
@Component
class HelpCommandImpl implements BotCommand {

    private final SendMessageFactory sendMessageFactory;

    /* default */ HelpCommandImpl(final SendMessageFactory sendMessageFactory) {
        this.sendMessageFactory = sendMessageFactory;
    }

    /**
     * Respond with a generic help text.
     * @param bot the bot
     * @param update the update
     * @throws TelegramApiException thrown when there's a problem with the response
     */
    @Override
    public void respond(final AbsSender bot, final Update update) throws TelegramApiException {
        final Locale locale = new Locale.Builder()
                .setLanguage(update.getMessage().getFrom().getLanguageCode())
                .build();

        bot.execute(sendMessageFactory.getHelpMessage(update.getMessage().getFrom().getId(), locale));
    }
}
