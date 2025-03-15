package picturebot.bot.command.aspects;

import picturebot.bot.user.BotUserUpdater;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class UserUpdaterAspectTest {

    @Mock private BotUserUpdater botUserUpdater;
    @InjectMocks private UserUpdaterAspect userUpdaterAspect;

    @Mock private JoinPoint joinPoint;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private Update update;
    @Mock private User user;

    @Nested
    @DisplayName("beforeRespond()")
    class BeforeRespond {

        @Test
        @DisplayName("should update user data")
        void shouldUpdateUserData() {
            when(joinPoint.getArgs()).thenReturn(new Object[]{ null, update });
            when(update.hasMessage()).thenReturn(true);
            when(update.getMessage().getFrom()).thenReturn(user);

            userUpdaterAspect.beforeRespond(joinPoint);

            verify(botUserUpdater).createOrUpdateUser(user);
        }

        @Test
        @DisplayName("should not update user data, because there is no message")
        void shouldNotUpdateUserDataBecauseThereIsNoMessage() {
            when(joinPoint.getArgs()).thenReturn(new Object[]{ null, update });
            when(update.hasMessage()).thenReturn(false);

            userUpdaterAspect.beforeRespond(joinPoint);

            verifyNoInteractions(botUserUpdater);
        }
    }
}
