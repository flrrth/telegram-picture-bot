package picturebot.scheduler;

import picturebot.entities.botuser.BotUser;
import picturebot.entities.timezone.Timezone;
import picturebot.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service that is responsible for retrieving a list of users that are scheduled to receive a daily picture at the
 * current time.
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {

    private final UserRepository userRepository;
    private final Clock clock;

    public SchedulerServiceImpl(final UserRepository userRepository, final Clock clock) {
        this.userRepository = userRepository;
        this.clock = clock;
    }

    /**
     * Retrieves a list of users that are scheduled to receive a daily picture at the current time.
     * @param timezone the timezone of the users
     * @return a list of users that are scheduled to receive a daily picture at the current time
     */
    @Override
    public List<BotUser> getUsers(final Timezone timezone) {
        final ZonedDateTime now = ZonedDateTime.now(clock);
        final ZonedDateTime currentDateTime = now.withZoneSameInstant(ZoneId.of(timezone.getCity()));
        final LocalTime localTime = currentDateTime.toLocalTime().truncatedTo(ChronoUnit.SECONDS);
        return userRepository.findAllScheduledAt(localTime, timezone.getId());
    }
}
