package picturebot.bot.command.aspects;

import picturebot.bot.user.BotUserUpdater;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * Aspect that updates the user record whenever the user interacts with the bot.
 */
@Aspect
@Component
public class UserUpdaterAspect {

    private final BotUserUpdater botUserUpdater;

    public UserUpdaterAspect(final BotUserUpdater botUserUpdater) {
        this.botUserUpdater = botUserUpdater;
    }

    /**
     * Advice that runs before the respond-method of any command that is executed.
     * Updates the user record whenever the user interacts with the bot.
     *
     * @param joinPoint the join point providing access to the method arguments
     */
    @Before("CommonPointcuts.withinCommandPackage() && CommonPointcuts.respondMethod()")
    public void beforeRespond(final JoinPoint joinPoint) {
        final Update update = (Update) joinPoint.getArgs()[1];

        if (update.hasMessage()) {
            final User user = update.getMessage().getFrom();
            botUserUpdater.createOrUpdateUser(user);
        }
    }
}
