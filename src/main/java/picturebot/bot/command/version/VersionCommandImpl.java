package picturebot.bot.command.version;

import picturebot.bot.command.BotCommand;
import picturebot.bot.factory.SendMessageFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

/**
 * This command gets executed when the user sends the /version command and sends a message with the application version.
 */
@Component
public class VersionCommandImpl implements BotCommand {

    private final SendMessageFactory sendMessageFactory;
    private final String version;

    /* default */ VersionCommandImpl(final SendMessageFactory sendMessageFactory, final String version) {
        this.sendMessageFactory = sendMessageFactory;
        this.version = version;
    }

    /**
     * Respond with a message that contains the application version.
     * @param bot the bot
     * @param update the update
     * @throws TelegramApiException thrown when there's a problem with the response
     */
    @Override
    public void respond(final AbsSender bot, final Update update) throws TelegramApiException {
        final Locale locale = new Locale.Builder()
                .setLanguage(update.getMessage().getFrom().getLanguageCode())
                .build();

        bot.execute(sendMessageFactory.getVersionMessage(update.getMessage().getFrom().getId(), locale,
                new String[]{ version }));
    }
}
