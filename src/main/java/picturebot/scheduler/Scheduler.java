package picturebot.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import picturebot.bot.command.ScheduledCommand;
import picturebot.entities.botuser.BotUser;
import picturebot.entities.timezone.Timezone;
import picturebot.repositories.TimezoneRepository;

import java.util.List;

/**
 * Scheduler that runs every minute and sends a daily picture to the users that have opted to receive a daily picture at
 * that time.
 */
@Component
public class Scheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    private final ScheduledCommand command;
    private final TimezoneRepository timezoneRepository;
    private final TelegramLongPollingBot bot;
    private final SchedulerService schedulerService;

    public Scheduler(final ScheduledCommand command,
                     final TimezoneRepository timezoneRepository,
                     final TelegramLongPollingBot bot,
                     final SchedulerService schedulerService) {

        this.command = command;
        this.timezoneRepository = timezoneRepository;
        this.bot = bot;
        this.schedulerService = schedulerService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void executeJob() {
        final List<Timezone> timezones = timezoneRepository.findAll();
        timezones.forEach(this::handleTimezone);
    }

    private void handleTimezone(final Timezone timezone) {
        final List<BotUser> botUsers = schedulerService.getUsers(timezone);
        botUsers.forEach(this::handleBotUser);
    }

    private void handleBotUser(final BotUser botUser) {
        LOGGER.info("Sending daily picture to user {}.", botUser.getId());

        try {
            command.send(bot, botUser);
        }
        catch (TelegramApiException e) {
            LOGGER.error("Could not send picture to user {}.", botUser.getId(), e);
        }
    }
}
