package picturebot.bot.command.stats;

import picturebot.bot.command.BotCommand;
import picturebot.bot.factory.SendMessageFactory;
import picturebot.picture.counter.PictureCounter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

/**
 * This command gets executed when the user sends the /stats command and sends a message with statistics.
 */
@Component
class StatsCommandImpl implements BotCommand {

    private final Environment environment;
    private final SendMessageFactory sendMessageFactory;
    private final PictureCounter pictureCounter;

    /* default*/ StatsCommandImpl(final Environment environment, final SendMessageFactory sendMessageFactory,
                                  final PictureCounter pictureCounter) {

        this.environment = environment;
        this.sendMessageFactory = sendMessageFactory;
        this.pictureCounter = pictureCounter;
    }

    /**
     * Respond with a text containing statistics.
     * @param bot the bot
     * @param update the update
     * @throws TelegramApiException thrown when there's a problem with the response
     */
    @Override
    public void respond(final AbsSender bot, final Update update) throws TelegramApiException {
        final int total = pictureCounter.count(environment.getRequiredProperty("bot.regular.subPath"));
        final Locale locale = new Locale.Builder()
                .setLanguage(update.getMessage().getFrom().getLanguageCode())
                .build();

        bot.execute(sendMessageFactory.getStatsMessage(update.getMessage().getFrom().getId(), locale,
                new String[]{ String.valueOf(total) }));
    }
}
