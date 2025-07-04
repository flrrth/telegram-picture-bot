package picturebot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import picturebot.bot.command.BotCommand;

import java.util.List;

/**
 * The main class of the bot. It is responsible for handling the incoming messages and routing them to the appropriate
 * command.
 */
@Component
public class PictureBotImpl implements SpringLongPollingBot, PictureBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureBotImpl.class);

    private final String botToken;
    private final String botName;
    private final String randomCommand;
    private final BotCommand randomPhotoCommand;
    private final BotCommand startCommand;
    private final BotCommand defaultCommand;
    private final BotCommand settingsCommand;
    private final BotCommand webappResponseProcessorCommand;
    private final BotCommand statsCommand;
    private final BotCommand helpCommand;
    private final BotCommand uploadPhotoCommand;
    private final BotCommand versionCommand;
    private final TelegramClient telegramClient;

    public PictureBotImpl(final Environment env,
                          @Qualifier("randomPhotoCommandImpl") final BotCommand randomPhotoCommand,
                          @Qualifier("startCommandImpl") final BotCommand startCommand,
                          @Qualifier("defaultCommandImpl") final BotCommand defaultCommand,
                          @Qualifier("settingsCommandImpl") final BotCommand settingsCommand,
                          @Qualifier("webappResponseProcessorCommandImpl") final BotCommand webappResponseProcessorCommand,
                          @Qualifier("statsCommandImpl") final BotCommand statsCommand,
                          @Qualifier("helpCommandImpl") final BotCommand helpCommand,
                          @Qualifier("uploadPhotoCommandImpl") final BotCommand uploadPhotoCommand,
                          @Qualifier("versionCommandImpl") final BotCommand versionCommand,
                          final TelegramClient telegramClient) {

        this.botToken = env.getRequiredProperty("PICTURE_BOT_TOKEN");
        this.botName = env.getRequiredProperty("bot.name");
        this.randomCommand = env.getRequiredProperty("bot.randomCommand");
        this.randomPhotoCommand = randomPhotoCommand;
        this.startCommand = startCommand;
        this.defaultCommand = defaultCommand;
        this.settingsCommand = settingsCommand;
        this.webappResponseProcessorCommand = webappResponseProcessorCommand;
        this.statsCommand = statsCommand;
        this.helpCommand = helpCommand;
        this.uploadPhotoCommand = uploadPhotoCommand;
        this.versionCommand = versionCommand;
        this.telegramClient = telegramClient;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this::handleUpdates;
    }

    private void handleUpdates(final List<Update> updates) {
        for (Update update : updates) {
            onUpdateReceived(update);
        }
    }

    private void onUpdateReceived(final Update update) {
        if (update.hasMessage()) {
            try {
                if (update.getMessage().hasText()) {
                    final String messageText = update.getMessage().getText();
                    
                    switch (messageText) {
                        case "/start":
                            startCommand.respond(telegramClient, update);
                            break;
                        case "/settings":
                            settingsCommand.respond(telegramClient, update);
                            break;
                        case "/stats":
                            statsCommand.respond(telegramClient, update);
                            break;
                        case "/help":
                            helpCommand.respond(telegramClient, update);
                            break;
                        case "/version":
                            versionCommand.respond(telegramClient, update);
                            break;
                        default:
                            if (messageText.equalsIgnoreCase(this.randomCommand)) {
                                randomPhotoCommand.respond(telegramClient, update);
                            }
                            else {
                                defaultCommand.respond(telegramClient, update);
                            }
                            break;
                    }
                }
                else if (update.getMessage().getWebAppData() != null) {
                    webappResponseProcessorCommand.respond(telegramClient, update);
                }
                else if (update.getMessage().hasPhoto()) {
                    uploadPhotoCommand.respond(telegramClient, update);
                }
            }
            catch (final TelegramApiException e) {
                LOGGER.error("Could not send the message.", e);
            }
        }
    }

    public String getBotUsername() {
        return this.botName;
    }
}
