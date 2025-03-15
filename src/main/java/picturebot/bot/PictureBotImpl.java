package picturebot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import picturebot.bot.command.BotCommand;

/**
 * The main class of the bot. It is responsible for handling the incoming messages and routing them to the appropriate
 * command.
 */
@Component
public class PictureBotImpl extends TelegramLongPollingBot implements PictureBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureBotImpl.class);

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

    public PictureBotImpl(final Environment env,
                          @Qualifier("randomPhotoCommandImpl") final BotCommand randomPhotoCommand,
                          @Qualifier("startCommandImpl") final BotCommand startCommand,
                          @Qualifier("defaultCommandImpl") final BotCommand defaultCommand,
                          @Qualifier("settingsCommandImpl") final BotCommand settingsCommand,
                          @Qualifier("webappResponseProcessorCommandImpl") final BotCommand webappResponseProcessorCommand,
                          @Qualifier("statsCommandImpl") final BotCommand statsCommand,
                          @Qualifier("helpCommandImpl") final BotCommand helpCommand,
                          @Qualifier("uploadPhotoCommandImpl") final BotCommand uploadPhotoCommand,
                          @Qualifier("versionCommandImpl") final BotCommand versionCommand) {

        super(env.getRequiredProperty("PICTURE_BOT_TOKEN"));

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
    }

    @Override
    public void onUpdateReceived(final Update update) {
        if (update.hasMessage()) {
            try {
                if (update.getMessage().hasText()) {
                    final String messageText = update.getMessage().getText();
                    
                    switch (messageText) {
                        case "/start":
                            startCommand.respond(this, update);
                            break;
                        case "/settings":
                            settingsCommand.respond(this, update);
                            break;
                        case "/stats":
                            statsCommand.respond(this, update);
                            break;
                        case "/help":
                            helpCommand.respond(this, update);
                            break;
                        case "/version":
                            versionCommand.respond(this, update);
                            break;
                        default:
                            if (messageText.equalsIgnoreCase(this.randomCommand)) {
                                randomPhotoCommand.respond(this, update);
                            }
                            else {
                                defaultCommand.respond(this, update);
                            }
                            break;
                    }
                }
                else if (update.getMessage().getWebAppData() != null) {
                    webappResponseProcessorCommand.respond(this, update);
                }
                else if (update.getMessage().hasPhoto()) {
                    uploadPhotoCommand.respond(this, update);
                }
            }
            catch (final TelegramApiException e) {
                LOGGER.error("Could not send the message.", e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }
}
