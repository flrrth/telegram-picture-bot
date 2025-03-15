package picturebot.bot.command.settings;

import java.util.Locale;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import picturebot.bot.command.BotCommand;
import picturebot.bot.factory.SettingsReplyKeyboardMarkupFactory;
import picturebot.bot.user.SettingsMessageIdSaver;
import picturebot.entities.botuser.BotUser;
import picturebot.entities.settings.Settings;
import picturebot.repositories.UserRepository;

/**
 * This command gets executed when the user sends the /settings command and creates a keyboard that opens the
 * settings page.
 */
@Component
class SettingsCommandImpl implements BotCommand {

    private final UserRepository userRepository;
    private final SettingsReplyKeyboardMarkupFactory settingsReplyKeyboardMarkupFactory;
    private final UrlCreator urlCreator;
    private final SendMessageCreator sendMessageCreator;
    private final SettingsMessageIdSaver settingsMessageIdSaver;

    /* default */ SettingsCommandImpl(final UserRepository userRepository,
                                      final SettingsReplyKeyboardMarkupFactory settingsReplyKeyboardMarkupFactory,
                                      final UrlCreator urlCreator,
                                      final SendMessageCreator sendMessageCreator,
                                      final SettingsMessageIdSaver settingsMessageIdSaver) {

        this.userRepository = userRepository;
        this.settingsReplyKeyboardMarkupFactory = settingsReplyKeyboardMarkupFactory;
        this.urlCreator = urlCreator;
        this.sendMessageCreator = sendMessageCreator;
        this.settingsMessageIdSaver = settingsMessageIdSaver;
    }

    /**
     * Respond with a customized keyboard that when clicked by the user opens the settings page.
     * @param bot the bot
     * @param update the update
     * @throws TelegramApiException thrown when there's a problem with the response
     */
    @Override
    public void respond(final AbsSender bot, final Update update) throws TelegramApiException {
        final Locale locale = new Locale.Builder()
                .setLanguage(update.getMessage().getFrom().getLanguageCode())
                .build();

        // Get the user from the database, so we can access their settings:
        final BotUser botUser = userRepository.findById(update.getMessage().getFrom().getId()).orElseThrow();
        final Settings settings = botUser.getSettings();

        // Create the keyboard:
        final String url = urlCreator.create(settings, locale);
        final ReplyKeyboardMarkup replyKeyboardMarkup = settingsReplyKeyboardMarkupFactory.create(url, locale);

        // Send the keyboard and store the ID of the message, so we can delete it (and the keyboard) later.
        final Message message = bot.execute(sendMessageCreator
                .create(botUser.getId(), settings, replyKeyboardMarkup, locale));
        settingsMessageIdSaver.saveSettingsMessageId(botUser.getId(), message.getMessageId());
    }
}
