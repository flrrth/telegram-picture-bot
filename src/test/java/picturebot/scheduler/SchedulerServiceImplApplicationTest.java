package picturebot.scheduler;

import picturebot.DailyPictureBotTestConfiguration;
import picturebot.FlywayTestExecutionListener;
import picturebot.entities.botuser.BotUser;
import picturebot.entities.timezone.Timezone;
import picturebot.repositories.TimezoneRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Tag("integration")
@Import(DailyPictureBotTestConfiguration.class)
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class
})
public class SchedulerServiceImplApplicationTest {

    @Autowired private TimezoneRepository timezoneRepository;
    @Autowired private SchedulerServiceImpl schedulerService;

    @Nested
    @DisplayName("getUsers()")
    class GetUsers {

        @Test
        @DisplayName("should return user 'London'")
        void shouldReturnUserLondon() {
            final Optional<Timezone> timezoneOptional = timezoneRepository.findById(0L);

            final List<BotUser> users = schedulerService.getUsers(timezoneOptional.orElseThrow());

            assertEquals(1, users.size());
            assertEquals("london", users.get(0).getUserDetails().get(0).getUserName());
        }

        @Test
        @DisplayName("should return user 'Amsterdam'")
        void shouldReturnUserAmsterdam() {
            final Optional<Timezone> timezoneOptional = timezoneRepository.findById(1L);

            final List<BotUser> users = schedulerService.getUsers(timezoneOptional.orElseThrow());

            assertEquals(1, users.size());
            assertEquals("amsterdam", users.get(0).getUserDetails().get(0).getUserName());
        }

        @Test
        @DisplayName("should return user 'Bucharest'")
        void shouldReturnUserBucharest() {
            final Optional<Timezone> timezoneOptional = timezoneRepository.findById(2L);

            final List<BotUser> users = schedulerService.getUsers(timezoneOptional.orElseThrow());

            assertEquals(1, users.size());
            assertEquals("bucharest", users.get(0).getUserDetails().get(0).getUserName());
        }

        @Test
        @DisplayName("should return user 'Moscow'")
        void shouldReturnUserMoscow() {
            final Optional<Timezone> timezoneOptional = timezoneRepository.findById(3L);

            final List<BotUser> users = schedulerService.getUsers(timezoneOptional.orElseThrow());

            assertEquals(1, users.size());
            assertEquals("moscow", users.get(0).getUserDetails().get(0).getUserName());
        }
    }
}
