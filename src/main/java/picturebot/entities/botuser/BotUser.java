package picturebot.entities.botuser;

import picturebot.entities.botuserdetails.BotUserDetails;
import picturebot.entities.settings.Settings;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="bot_user", schema = "picture_bot")
public class BotUser {

    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime firstSeen;

    @Column(nullable = false)
    private LocalDateTime lastSeen;

    @Column(columnDefinition = "integer default 0")
    private Integer requestCount = 0;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean hasBlockedBot = false;

    @OneToMany(mappedBy = "botUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("timestamp desc")
    private List<BotUserDetails> botUserDetails = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "settings_id", unique = true)
    private Settings settings;

    @Column(name = "settings_message_id")
    private Integer settingsMessageId;

    public BotUser() {
    }

    public BotUser(final Long id, final LocalDateTime firstSeen, final LocalDateTime lastSeen) {
        this.id = id;
        this.firstSeen = firstSeen;
        this.lastSeen = lastSeen;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFirstSeen() {
        return firstSeen;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(final LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(final Integer requestCount) {
        this.requestCount = requestCount;
    }

    public Boolean getHasBlockedBot() {
        return hasBlockedBot;
    }

    public void setHasBlockedBot(final Boolean hasBlockedBot) {
        this.hasBlockedBot = hasBlockedBot;
    }

    public List<BotUserDetails> getUserDetails() {
        return botUserDetails;
    }

    public @Nullable Settings getSettings() {
        return settings;
    }

    public void setSettings(final Settings settings) {
        this.settings = settings;
    }

    public @Nullable Integer getSettingsMessageId() {
        return settingsMessageId;
    }

    public void setSettingsMessageId(final int settingsMessageId) {
        this.settingsMessageId = settingsMessageId;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final BotUser botUser = (BotUser) other;
        return Objects.equals(getId(), botUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
