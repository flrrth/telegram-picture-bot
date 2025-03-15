package picturebot.bot.command.photo.unique;

import picturebot.DailyPictureBotTestConfiguration;
import picturebot.FlywayTestExecutionListener;
import picturebot.entities.botuser.BotUser;
import picturebot.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.FileSystemUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest
@Tag("integration")
@Import(DailyPictureBotTestConfiguration.class)
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class
})
public class UniquePhotoCommandImplApplicationTest {

    @Autowired private UserRepository userRepository;
    @Autowired private UniquePhotoCommandImpl uniquePhotoCommand;
    @Autowired private Path mockedBasePath;
    @Autowired private Environment environment;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private AbsSender bot;

    private BotUser botUser;

    @BeforeEach
    void resetMocks() {
        reset(bot);
    }

    @BeforeEach
    void retrieveBotUserFromDatabase() {
        botUser = userRepository.findById(6L).orElseThrow();
    }

    @BeforeEach
    void populateFileSystemWithMockImages() throws IOException {
        final String subPath = environment.getRequiredProperty("bot.regular.subPath");
        final Path path = mockedBasePath.resolve(subPath);

        // Create 'pictures':
        Files.createFile(Files.createDirectories(path.resolve("cats")).resolve("cat.jpg"));
        Files.createFile(Files.createDirectories(path.resolve("dogs")).resolve("dog.jpg"));
        Files.createFile(Files.createDirectories(path.resolve("mice")).resolve("mouse.jpg"));

        // Create the message bundles:
        Files.write(path.resolve("message.properties"),
                List.of("greeting=Hello World!"), StandardOpenOption.CREATE);
        Files.write(path.resolve("message_nl.properties"),
                List.of("greeting=Hallo Wereld!"), StandardOpenOption.CREATE);
    }

    @AfterEach
    void cleanUpFileSystem() throws IOException {
        FileSystemUtils.deleteRecursively(mockedBasePath.resolve(
                environment.getRequiredProperty("bot.regular.subPath")));
    }

    @Nested
    @DisplayName("UniquePhotoCommandImpl#send()")
    class UniquePhotoCommandImplSend {

        @Test
        @DisplayName("should set BotUser 'hasBlockedBot' to true because the user has blocked the bot")
        void shouldSetBotUserHasBlockedBotToTrueBecauseTheUserHasBlockedTheBot() throws TelegramApiException {
            when(bot.execute(any(SendPhoto.class)))
                    .thenThrow(new TelegramApiException("[403] Forbidden: bot was blocked by the user"));

            uniquePhotoCommand.send(bot, botUser);

            // Verify that the user was updated:
            final BotUser botUser = jdbcTemplate.queryForObject(
                    "SELECT * FROM picture_bot.bot_user WHERE id = 6",
                    new BeanPropertyRowMapper<>(BotUser.class));

            assert botUser != null;
            assertTrue(botUser.getHasBlockedBot());
        }
    }
}
