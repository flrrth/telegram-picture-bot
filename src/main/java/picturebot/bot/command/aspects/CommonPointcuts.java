package picturebot.bot.command.aspects;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Common pointcuts for aspect-oriented programming in the Picture Bot application.
 */
class CommonPointcuts {

    /**
     * Pointcut that matches all methods within the picturebot.bot.command package and its subpackages.
     */
    @Pointcut("within(picturebot.bot.command..*)")
    void withinCommandPackage() {}

    /**
     * Pointcut that matches the execution of any public method named 'respond' with any arguments.
     */
    @Pointcut("execution(public * respond(..))")
    void respondMethod() {}

    /**
     * Pointcut that matches the execution of the 'respond' method in the StartCommandImpl class.
     */
    @Pointcut("execution(public * picturebot.bot.command.start.StartCommandImpl.respond(..))")
    void respondMethodStartCommand() {}
}
