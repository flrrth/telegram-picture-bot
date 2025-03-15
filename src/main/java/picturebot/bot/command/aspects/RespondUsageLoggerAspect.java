package picturebot.bot.command.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Aspect that logs all calls to the respond() method of command implementations.
 */
@Aspect
@Component
public class RespondUsageLoggerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RespondUsageLoggerAspect.class);

    /**
     * Advice that runs before the execution of any respond() method within the command package.
     * Logs the user ID and the method signature.
     *
     * @param joinPoint the join point providing access to the method arguments
     */
    @Before("CommonPointcuts.withinCommandPackage() && CommonPointcuts.respondMethod()")
    public void beforeRespond(final JoinPoint joinPoint) {
        final Update update = (Update) joinPoint.getArgs()[1];

        if (update.hasMessage()) {
            if (LOGGER.isInfoEnabled()) {
                final String shortString = joinPoint.getSignature().toShortString();
                final long userId = update.getMessage().getFrom().getId();

                LOGGER.info("User {} called {}", userId, shortString);
            }
        }
        else {
            LOGGER.warn("Received update without message.");
        }
    }
}
