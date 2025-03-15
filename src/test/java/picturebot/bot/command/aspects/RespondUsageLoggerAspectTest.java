package picturebot.bot.command.aspects;

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
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
class RespondUsageLoggerAspectTest {

    @InjectMocks private RespondUsageLoggerAspect respondUsageLoggerAspect;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private JoinPoint joinPoint;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private Update update;

    @Nested
    @DisplayName("beforeRespond()")
    class BeforeRespond {

        @Test
        @DisplayName("should log user ID and class called")
        void shouldLogUserIdAndClassCalled(final CapturedOutput output) {
            when(joinPoint.getArgs()).thenReturn(new Object[]{ null, update });
            when(update.hasMessage()).thenReturn(true);
            when(joinPoint.getSignature().toShortString()).thenReturn("SomeClass.respond(..)");
            when(update.getMessage().getFrom().getId()).thenReturn(1L);

            respondUsageLoggerAspect.beforeRespond(joinPoint);

            assertThat(output.getOut(), containsString("INFO  - User 1 called SomeClass.respond(..)"));
        }

        @Test
        @DisplayName("should log warning that update was received without message")
        void shouldLogWarningThatUpdateWasReceivedWithoutMessage(final CapturedOutput output) {
            when(joinPoint.getArgs()).thenReturn(new Object[]{ null, update });
            when(update.hasMessage()).thenReturn(false);

            respondUsageLoggerAspect.beforeRespond(joinPoint);

            assertThat(output.getOut(), containsString("WARN  - Received update without message."));
        }
    }
}
