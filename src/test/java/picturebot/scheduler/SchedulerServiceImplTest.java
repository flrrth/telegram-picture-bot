package picturebot.scheduler;

import picturebot.entities.botuser.BotUser;
import picturebot.entities.timezone.Timezone;
import picturebot.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class SchedulerServiceImplTest {

    static final LocalDateTime NOON = LocalDateTime.of(2024, 1, 2, 12, 0);
    final Clock fixedClock = Clock.fixed(NOON.atZone(ZoneId.of("Europe/Amsterdam")).toInstant(),
            ZoneId.of("Europe/Amsterdam"));

    @Mock private UserRepository userRepository;
    @Mock private Timezone timezone;
    @Mock private BotUser botUser;

    private SchedulerServiceImpl schedulerService;

    @BeforeEach
    void setUp() {
        schedulerService = new SchedulerServiceImpl(userRepository, fixedClock);
    }

    @Nested
    @DisplayName("getUsers()")
    class GetUsers {

        @Test
        @DisplayName("should return user")
        void shouldReturnUser() {
            when(timezone.getId()).thenReturn(1L);
            when(timezone.getCity()).thenReturn("Europe/Amsterdam");
            when(userRepository.findAllScheduledAt(LocalTime.of(12, 0), 1L))
                    .thenReturn(List.of(botUser));

            final List<BotUser> botUsers = schedulerService.getUsers(timezone);

            assertThat(botUsers, is(notNullValue()));
            assertThat(botUsers, is(not(empty())));
            assertThat(botUsers, hasItem(botUser));
        }
    }
}
