package picturebot.entities.botuserdetails;

import picturebot.entities.botuser.BotUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="bot_user_details", schema = "picture_bot")
public class BotUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(name = "user_name", length = 32)
    private String userName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_bot", nullable = false)
    private Boolean isBot;

    @Column(name = "language_code", nullable = false, length = 3)
    private String languageCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BotUser botUser;

    public BotUserDetails() {
    }

    public BotUserDetails(final LocalDateTime timestamp, final String userName, final String firstName,
                          final String lastName, final Boolean isBot, final String languageCode,
                          final BotUser botUser) {
        this.timestamp = timestamp;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isBot = isBot;
        this.languageCode = languageCode;
        this.botUser = botUser;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Boolean getIsBot() {
        return isBot;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public BotUser getBotUser() {
        return botUser;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final BotUserDetails that = (BotUserDetails) other;
        return Objects.equals(getUserName(), that.getUserName()) &&
                Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getIsBot(), that.getIsBot()) &&
                Objects.equals(getBotUser(), that.getBotUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getFirstName(), getLastName(), getIsBot(), getBotUser());
    }
}
