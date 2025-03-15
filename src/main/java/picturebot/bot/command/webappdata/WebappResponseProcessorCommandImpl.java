package picturebot.bot.command.webappdata;

import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.fasterxml.jackson.core.JsonProcessingException;

import picturebot.bot.command.BotCommand;
import picturebot.bot.factory.SendMessageFactory;
import picturebot.entities.botuser.BotUser;
import picturebot.entities.timezone.Timezone;
import picturebot.repositories.TimezoneRepository;
import picturebot.repositories.UserRepository;

/**
 * This command gets executed when the bot receives a response from the webapp. It updates the user's settings and
 * sends a DeleteMessage that removes the keyboard with the settings button.
 */
@Component
public class WebappResponseProcessorCommandImpl implements BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebappResponseProcessorCommandImpl.class);

    private final UserRepository userRepository;
    private final TimezoneRepository timezoneRepository;
    private final SendMessageFactory sendMessageFactory;
    private final WebappDataParser webappDataParser;
    private final SettingsUpdater settingsUpdater;

    /* default */ WebappResponseProcessorCommandImpl(final UserRepository userRepository,
                                                     final TimezoneRepository timezoneRepository,
                                                     final SendMessageFactory sendMessageFactory,
                                                     final WebappDataParser webappDataParser,
                                                     final SettingsUpdater settingsUpdater) {

        this.userRepository = userRepository;
        this.timezoneRepository = timezoneRepository;
        this.sendMessageFactory = sendMessageFactory;
        this.webappDataParser = webappDataParser;
        this.settingsUpdater = settingsUpdater;
    }

    /**
     * Responds with a DeleteMessage that removes the keyboard after the settings were successfully updated, otherwise
     * responds with an error message.
     * @param bot the bot
     * @param update the update
     * @throws TelegramApiException thrown when there's a problem with the response
     */
    @Override
    @Transactional
    public void respond(final AbsSender bot, final Update update) throws TelegramApiException {
        try {
            final String dataString = update.getMessage().getWebAppData().getData();
            final WebappData data = webappDataParser.parse(dataString);
            final Optional<Timezone> optionalTimezone = timezoneRepository.findById(data.getTimezone());
            final Timezone timezone = optionalTimezone.orElseThrow();
            final BotUser botUser = userRepository.findById(update.getMessage().getFrom().getId()).orElseThrow();
            botUser.setSettings(settingsUpdater.updateSettings(botUser, data, timezone));
            userRepository.save(botUser);

            if (botUser.getSettingsMessageId() != null) {
                bot.execute(new DeleteMessage(update.getMessage().getFrom().getId().toString(),
                        botUser.getSettingsMessageId()));
            }
        }
        catch (JsonProcessingException e) {
            LOGGER.error("Could not parse webapp data.", e);

            final Locale locale = new Locale.Builder()
                    .setLanguage(update.getMessage().getFrom().getLanguageCode())
                    .build();

            bot.execute(sendMessageFactory.getDefaultErrorMessage(update.getMessage().getFrom().getId(), locale));
        }
    }
}
