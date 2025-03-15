package picturebot.bot.factory;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.StringJoiner;

/**
 * This class is responsible for formatting the cooldown time in a human-readable format.
 */
@Component
class CooldownTextFormatterImpl implements CooldownTextFormatter {

    private final MessageSource messageSource;

    public CooldownTextFormatterImpl(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String format(final long seconds, final Locale locale) {
        final RemainingTime remainingTime = getRemainingTime(seconds);
        final StringJoiner stringJoiner = new StringJoiner(", ");

        if (remainingTime.hours > 1) {
            stringJoiner.add(messageSource.getMessage(
                    "coolDown.hours", new Integer[]{ remainingTime.hours }, locale));
        } else if (remainingTime.hours == 1) {
            stringJoiner.add(messageSource.getMessage(
                    "coolDown.hour", new Integer[] { remainingTime.hours }, locale));
        }

        if (remainingTime.minutes > 1) {
            stringJoiner.add(messageSource.getMessage(
                    "coolDown.minutes", new Integer[]{ remainingTime.minutes }, locale));
        } else if (remainingTime.minutes == 1) {
            stringJoiner.add(messageSource.getMessage(
                    "coolDown.minute", new Integer[] { remainingTime.minutes }, locale));
        }

        if (remainingTime.seconds > 1) {
            stringJoiner.add(messageSource.getMessage(
                    "coolDown.seconds", new Integer[]{ remainingTime.seconds }, locale));
        } else if (remainingTime.seconds == 1) {
            stringJoiner.add(messageSource.getMessage(
                    "coolDown.second", new Integer[] { remainingTime.seconds }, locale));
        }

        final String result = stringJoiner.toString();
        final int positionLastComma = result.lastIndexOf(",");

        if (positionLastComma != -1) {
            return result.substring(0, positionLastComma) +
                    " " + messageSource.getMessage("coolDown.and", null, locale) +
                    result.substring(positionLastComma + 1);
        }

        return result;
    }

    private RemainingTime getRemainingTime(final long coolDownInSeconds) {
        final int hours = (int) (coolDownInSeconds / 3600);
        final int minutes = (int) (coolDownInSeconds % 3600 / 60);
        final int seconds = (int) (coolDownInSeconds % 60);

        return new RemainingTime(hours, minutes, seconds);
    }

    private record RemainingTime (int hours, int minutes, int seconds) {}
}
