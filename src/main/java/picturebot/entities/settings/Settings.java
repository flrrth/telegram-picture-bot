package picturebot.entities.settings;

import picturebot.entities.botuser.BotUser;
import picturebot.entities.timezone.Timezone;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "settings", schema = "picture_bot")
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;

    @Column(name = "schedule", nullable = false)
    private LocalTime schedule;

    @Column(name = "is_spoiler_enabled", nullable = false)
    private Boolean isSpoilerEnabled;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private BotUser botUser;

    @Column(name = "last_modified", nullable = false)
    private LocalDateTime lastModified;

    @ManyToOne
    @JoinColumn(name = "timezone_id", nullable = false)
    private Timezone timezone;

    public Settings() {
    }

    public Settings(final Boolean isEnabled, final LocalTime schedule, final Boolean isSpoilerEnabled,
                    final BotUser botUser, final LocalDateTime lastModified, final Timezone timezone) {

        this.isEnabled = isEnabled;
        this.schedule = schedule;
        this.isSpoilerEnabled = isSpoilerEnabled;
        this.botUser = botUser;
        this.lastModified = lastModified;
        this.timezone = timezone;
    }

    public Long getId() {
        return id;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(final Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public LocalTime getSchedule() {
        return schedule;
    }

    public void setSchedule(final LocalTime schedule) {
        this.schedule = schedule;
    }

    public Boolean getSpoilerEnabled() {
        return isSpoilerEnabled != null && isSpoilerEnabled;
    }

    public void setIsSpoilerEnabled(final Boolean isSpoilerEnabled) {
        this.isSpoilerEnabled = isSpoilerEnabled;
    }

    public BotUser getBotUser() {
        return botUser;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(final LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public Timezone getTimezone() {
        return timezone;
    }

    public void setTimezone(final Timezone timezone) {
        this.timezone = timezone;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final Settings settings = (Settings) other;

        return Objects.equals(getId(), settings.getId()) &&
                Objects.equals(getIsEnabled(), settings.getIsEnabled()) &&
                Objects.equals(getSchedule(), settings.getSchedule()) &&
                Objects.equals(getSpoilerEnabled(), settings.getSpoilerEnabled()) &&
                Objects.equals(getBotUser(), settings.getBotUser()) &&
                Objects.equals(getLastModified(), settings.getLastModified()) &&
                Objects.equals(getTimezone(), settings.getTimezone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIsEnabled(), getSchedule(), getSpoilerEnabled(),
                getBotUser(), getLastModified(), getTimezone());
    }
}
