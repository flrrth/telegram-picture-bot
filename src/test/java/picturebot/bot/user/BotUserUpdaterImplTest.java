package picturebot.bot.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.User;

import picturebot.entities.botuser.BotUser;
import picturebot.entities.botuser.BotUserFactory;
import picturebot.entities.botuserdetails.BotUserDetails;
import picturebot.entities.botuserdetails.BotUserDetailsFactory;
import picturebot.fixtures.UserFixture;
import picturebot.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class BotUserUpdaterImplTest {

    private static final LocalDateTime NOON = LocalDateTime.of(2024, 1, 2, 12, 0);
    private final Clock fixedClock = Clock.fixed(NOON.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock private UserRepository userRepository;
    @Mock private BotUserFactory botUserFactory;
    @Mock private BotUserDetailsFactory botUserDetailsFactory;

    private LocalDateTime now;
    @Mock private BotUser botUser;
    @Mock private BotUserDetails botUserDetails;
    @Mock private List<BotUserDetails> botUserDetailsArrayList;

    private BotUserUpdaterImpl botUserUpdater;

    @BeforeEach
    void setUp() {
        botUserUpdater = new BotUserUpdaterImpl(userRepository, botUserFactory, botUserDetailsFactory, fixedClock);
        now = LocalDateTime.now(fixedClock);
    }

    @Nested
    @DisplayName("createOrUpdateUser()")
    class CreateOrUpdateUser {

        @Test
        @DisplayName("should create new user when user is not yet known")
        void createOrUpdateUserShouldCreateNewUserWhenUserIsNotYetKnown() {
            final User user = UserFixture.createBasicUser("en");

            when(userRepository.findById(6L)).thenReturn(Optional.empty());
            when(userRepository.save(any())).thenReturn(botUser);
            when(botUserFactory.createBotUser(user, now, now)).thenReturn(botUser);

            final BotUser savedBotUser = botUserUpdater.createOrUpdateUser(user);

            assertEquals(botUser, savedBotUser);
            verify(botUserFactory).createBotUser(user, now, now);
        }

        @Test
        @DisplayName("should update user details when user details have changed")
        void createOrUpdateUserShouldUpdateUserDetailsWhenUserDetailsHaveChanged() {
            final User user = UserFixture.createBasicUser("en");

            when(userRepository.findById(6L)).thenReturn(Optional.of(botUser));
            when(botUserDetailsFactory.createBotUserDetails(eq(now), eq("john_d"), eq("John"),
                    eq("Doe"), eq(false), eq("en"), eq(botUser)))
                    .thenReturn(Mockito.mock(BotUserDetails.class));
            when(botUserDetailsArrayList.get(0)).thenReturn(botUserDetails);
            when(botUser.getUserDetails()).thenReturn(botUserDetailsArrayList);
            when(userRepository.save(any())).thenReturn(botUser);

            final BotUser savedBotUser = botUserUpdater.createOrUpdateUser(user);

            assertEquals(botUser, savedBotUser);
            verify(botUser).setLastSeen(now);
            verify(botUser).setHasBlockedBot(false);
            verify(botUserDetailsArrayList).add(any(BotUserDetails.class));
        }

        @Test
        @DisplayName("should not update user details when user details have not changed")
        void createOrUpdateUserShouldNotUpdateUserDetailsWhenUserDetailsHaveNotChanged() {
            final User user = UserFixture.createBasicUser("en");

            when(userRepository.findById(6L)).thenReturn(Optional.of(botUser));
            when(botUserDetailsFactory.createBotUserDetails(eq(now), eq("john_d"), eq("John"),
                    eq("Doe"), eq(false), eq("en"), eq(botUser)))
                    .thenReturn(botUserDetails);
            when(botUserDetailsArrayList.get(0)).thenReturn(botUserDetails);
            when(botUser.getUserDetails()).thenReturn(botUserDetailsArrayList);
            when(userRepository.save(any())).thenReturn(botUser);

            final BotUser savedBotUser = botUserUpdater.createOrUpdateUser(user);

            assertEquals(botUser, savedBotUser);
            verify(botUser).setLastSeen(now);
            verify(botUser).setHasBlockedBot(false);
            verify(botUserDetailsArrayList, never()).add(any());
        }
    }
}
