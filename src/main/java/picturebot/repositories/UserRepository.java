package picturebot.repositories;

import picturebot.entities.botuser.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface UserRepository extends JpaRepository<BotUser, Long> {

    @Query("select bu from BotUser bu " +
            "join bu.settings s " +
            "join s.timezone t " +
            "where bu.hasBlockedBot = false and s.schedule = :schedule and s.isEnabled = true and t.id = :timezoneId")
    List<BotUser> findAllScheduledAt(@Param("schedule") LocalTime schedule, @Param("timezoneId") Long timezoneId);

    @Modifying
    @Query("update BotUser bu set bu.settingsMessageId = :messageId where bu.id = :userId")
    void saveMessageId(@Param("userId") Long userId, @Param("messageId") int messageId);
}
