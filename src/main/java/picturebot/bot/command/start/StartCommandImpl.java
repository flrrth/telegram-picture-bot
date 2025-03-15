package picturebot.bot.command.start;

import picturebot.bot.command.BotCommand;
import picturebot.bot.factory.SendMessageFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

/**
 * This command gets executed when the user sends the /start command and sends a welcome message.
 */
@Component
public class StartCommandImpl implements BotCommand {

    private final SendMessageFactory sendMessageFactory;
    private final Environment environment;

    public StartCommandImpl(final SendMessageFactory sendMessageFactory, final Environment environment) {
        this.sendMessageFactory = sendMessageFactory;
        this.environment = environment;
    }

    @Override
    public void respond(final AbsSender bot, final Update update) throws TelegramApiException {
        final User user = update.getMessage().getFrom();
        final Locale locale = new Locale.Builder()
                .setLanguage(update.getMessage().getFrom().getLanguageCode())
                .build();

        bot.execute(sendMessageFactory.getStartMessage(
                user.getId(),
                locale,
                new String[]{ user.getFirstName(), environment.getRequiredProperty("bot.randomCommand") })
        );
    }
}
