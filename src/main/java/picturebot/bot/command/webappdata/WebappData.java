package picturebot.bot.command.webappdata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalTime;

/**
 * The WebappData class holds all the fields that are received from the webapp.
 */
class WebappData {

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime schedule;
    private Long timezone;

    private Boolean isSpoilerEnabled;

    private Boolean isEnabled;

    public LocalTime getSchedule() {
        return schedule;
    }

    public void setSchedule(final LocalTime schedule) {
        this.schedule = schedule;
    }

    public Long getTimezone() {
        return timezone;
    }

    public void setTimezone(final Long timezone) {
        this.timezone = timezone;
    }

    public Boolean getIsSpoilerEnabled() {
        return isSpoilerEnabled;
    }

    public void setIsSpoilerEnabled(final Boolean spoilerEnabled) {
        isSpoilerEnabled = spoilerEnabled;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(final Boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public String toString() {
        return "WebappData{" +
                "schedule=" + getSchedule() +
                ", timezone=" + getTimezone() +
                ", isSpoilerEnabled=" + getIsSpoilerEnabled() +
                ", isEnabled=" + getIsEnabled() +
                '}';
    }
}
