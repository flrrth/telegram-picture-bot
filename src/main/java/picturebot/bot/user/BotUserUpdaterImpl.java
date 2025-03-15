package picturebot.bot.user;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;

import picturebot.entities.botuser.BotUser;
import picturebot.entities.botuser.BotUserFactory;
import picturebot.entities.botuserdetails.BotUserDetails;
import picturebot.entities.botuserdetails.BotUserDetailsFactory;
import picturebot.repositories.UserRepository;

/**
 * This class updates an existing user or creates a new one if the user does not exist.
 */
@Component
class BotUserUpdaterImpl implements BotUserUpdater {

    private final UserRepository userRepository;
    private final BotUserFactory botUserFactory;
    private final BotUserDetailsFactory botUserDetailsFactory;
    private final Clock clock;

    /* default */ BotUserUpdaterImpl(final UserRepository userRepository, final BotUserFactory botUserFactory,
                              final BotUserDetailsFactory botUserDetailsFactory, final Clock clock) {

        this.userRepository = userRepository;
        this.botUserFactory = botUserFactory;
        this.botUserDetailsFactory = botUserDetailsFactory;
        this.clock = clock;
    }

    @Override
    @Transactional
    public BotUser createOrUpdateUser(final User user) {
        final Optional<BotUser> botUserOptional = userRepository.findById(user.getId());
        final LocalDateTime now = LocalDateTime.now(clock);
        final BotUser botUser;

        if (botUserOptional.isPresent()) {
            botUser = botUserOptional.get();
            botUser.setLastSeen(now);
            botUser.setHasBlockedBot(false);

            final BotUserDetails botUserDetails = botUserDetailsFactory.createBotUserDetails(now, user.getUserName(),
                    user.getFirstName(), user.getLastName(), user.getIsBot(), user.getLanguageCode(), botUser);

            if (!botUser.getUserDetails().get(0).equals(botUserDetails)) {
                botUser.getUserDetails().add(botUserDetails);
            }
        }
        else {
            botUser = botUserFactory.createBotUser(user, now, now);
        }

        return userRepository.save(botUser);
    }
}
