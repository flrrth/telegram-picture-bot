package picturebot.bot.uri;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * This class is responsible for building the URL that is used to open the web application.
 */
public class WebAppURIComponents {
    private final String host;
    private final String path;
    private final String language;
    private final boolean isEnabled;
    private final LocalTime schedule;
    private final boolean isSpoilerEnabled;
    private final Long timezone;

    public static class Builder {

        // Required fields:
        private final String host;
        private final String path;
        private final String language;

        // Optional fields:
        private boolean isEnabled = true;
        private LocalTime schedule;
        private boolean isSpoilerEnabled = true;
        private Long timezone;

        public Builder(final String host, final String path, final String language) {
            this.host = host;
            this.path = path;
            this.language = language;
        }

        public WebAppURIComponents.Builder isEnabled(final boolean isEnabled) {
            this.isEnabled = isEnabled;
            return this;
        }

        public WebAppURIComponents.Builder schedule(final LocalTime schedule) {
            this.schedule = schedule;
            return this;
        }

        public WebAppURIComponents.Builder isSpoilerEnabled(final boolean isSpoilerEnabled) {
            this.isSpoilerEnabled = isSpoilerEnabled;
            return this;
        }

        public WebAppURIComponents.Builder timezone(final Long timezone) {
            this.timezone = timezone;
            return this;
        }

        public WebAppURIComponents build() {
            return new WebAppURIComponents(this);
        }
    }

    private WebAppURIComponents(final WebAppURIComponents.Builder builder) {
        this.host = builder.host;
        this.path = builder.path;
        this.language = builder.language;
        this.isEnabled = builder.isEnabled;
        this.schedule = builder.schedule;
        this.isSpoilerEnabled = builder.isSpoilerEnabled;
        this.timezone = builder.timezone;
    }

    public String getUrl() throws URISyntaxException, MalformedURLException {
        final StringBuilder query = new StringBuilder(100);
        query.append("language=").append(language)
                .append("&isEnabled=")
                .append(isEnabled)
                .append("&isSpoilerEnabled=")
                .append(isSpoilerEnabled);

        if (schedule != null) {
            query.append("&schedule=").append(schedule.truncatedTo(ChronoUnit.MINUTES).toString());
        }

        if (timezone != null) {
            query.append("&timezone=").append(timezone);
        }

        final URL url = new URI("https", null, host, 443, path, query.toString(), null).toURL();
        return url.toString();
    }
}
