package picturebot.scheduler;

import picturebot.entities.botuser.BotUser;
import picturebot.entities.timezone.Timezone;

import java.util.List;

public interface SchedulerService {

    List<BotUser> getUsers(Timezone timezone);
}
