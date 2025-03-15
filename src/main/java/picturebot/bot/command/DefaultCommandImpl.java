package picturebot.bot.command;

import picturebot.bot.factory.SendMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

/**
 * Default command implementation. This command is executed when the bot doesn't understand the command it received.
 */
@Component
public class DefaultCommandImpl implements BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCommandImpl.class);

    private final SendMessageFactory sendMessageFactory;
    private final Environment environment;

    public DefaultCommandImpl(final SendMessageFactory sendMessageFactory, final Environment environment) {
        this.sendMessageFactory = sendMessageFactory;
        this.environment = environment;
    }

    /**
     * Respond with the default text. This text is sent back to the user when the bot doesn't understand what command
     * it received.
     * @param bot the bot
     * @param update the update
     * @throws TelegramApiException thrown when there's a problem with the response
     */
    @Override
    public void respond(final AbsSender bot, final Update update) throws TelegramApiException {
        final Locale locale = new Locale.Builder()
                .setLanguage(update.getMessage().getFrom().getLanguageCode())
                .build();

        LOGGER.info("User {} says '{}'",
                update.getMessage().getFrom().getId(),
                update.getMessage().getText());

        bot.execute(sendMessageFactory.getDefaultMessage(
                update.getMessage().getFrom().getId(),
                locale,
                new String[]{ environment.getRequiredProperty("bot.randomCommand") }));
    }
}
