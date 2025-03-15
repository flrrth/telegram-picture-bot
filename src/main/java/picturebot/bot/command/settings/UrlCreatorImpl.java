package picturebot.bot.command.settings;

import picturebot.bot.uri.WebAppURIComponents;
import picturebot.entities.settings.Settings;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Locale;

/**
 * This class creates a URL that points to the webapp that hosts the static settings page.
 */
@Component
class UrlCreatorImpl implements UrlCreator {

    private final Environment environment;

    /* default */ UrlCreatorImpl(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public String create(@Nullable final Settings settings, final Locale locale) {
        final WebAppURIComponents.Builder urlBuilder = new WebAppURIComponents.Builder(
                environment.getRequiredProperty("bot.webappHost"),
                environment.getRequiredProperty("bot.webappPath"),
                locale.getLanguage());

        if (settings != null) {
            urlBuilder.schedule(settings.getSchedule());
            urlBuilder.timezone(settings.getTimezone().getId());
            urlBuilder.isEnabled(settings.getIsEnabled());
            urlBuilder.isSpoilerEnabled(settings.getSpoilerEnabled());
        }

        try {
            return urlBuilder.build().getUrl();
        }
        catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException("Could not create the webapp URL.", e);
        }
    }
}
