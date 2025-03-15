package picturebot.bot.command.photo.random;

import picturebot.DailyPictureBotTestConfiguration;
import picturebot.FlywayTestExecutionListener;
import picturebot.entities.botuser.BotUser;
import picturebot.fixtures.UpdateFixture;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.FileSystemUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Tag("integration")
@Import(DailyPictureBotTestConfiguration.class)
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class
})
public class RandomPhotoCommandImplApplicationTest {

    @Autowired private RandomPhotoCommandImpl randomPhotoCommand;
    @Autowired private Path mockedBasePath;
    @Autowired private Environment environment;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private AbsSender bot;

    @Captor private ArgumentCaptor<SendPhoto> sendPhotoArgumentCaptor;
    @Captor private ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    @BeforeEach
    void resetMocks() {
        reset(bot);
    }

    @BeforeEach
    void populateFileSystemWithMockImages() throws IOException {
        final String subPath = environment.getRequiredProperty("bot.regular.subPath");

        Files.createFile(Files.createDirectories(mockedBasePath.resolve(subPath)
                .resolve("cats")).resolve("cat.jpg"));
        Files.createFile(Files.createDirectories(mockedBasePath.resolve(subPath)
                .resolve("dogs")).resolve("dog.jpg"));
        Files.createFile(Files.createDirectories(mockedBasePath.resolve(subPath)
                .resolve("mice")).resolve("mouse.jpg"));
    }

    @AfterEach
    void cleanUpFileSystem() throws IOException {
        FileSystemUtils.deleteRecursively(mockedBasePath.resolve(
                environment.getRequiredProperty("bot.regular.subPath")));
    }

    @Nested
    @DisplayName("RandomPhotoCommandImpl#respond()")
    class RandomPhotoCommandImplRespond {

        @Test
        @DisplayName("should respond to first call with photo and respond to second call with cooldown message")
        void shouldRespondToFirstCallWithPhotoAndRespondToSecondCallWithCooldownMessage() throws TelegramApiException {
            final Update update = UpdateFixture.createBasicUpdate("en");

            randomPhotoCommand.respond(bot, update);

            // Verify that the bot sent a photo:
            verify(bot).execute(sendPhotoArgumentCaptor.capture());
            final SendPhoto sendPhoto = sendPhotoArgumentCaptor.getValue();
            assertEquals("6", sendPhoto.getChatId());
            assertNull(sendPhoto.getCaption());
            assertFalse(sendPhoto.getHasSpoiler());
            assertTrue(Set.of("dog.jpg", "cat.jpg", "mouse.jpg").contains(sendPhoto.getFile().getMediaName()));

            // Verify that the request counter was incremented:
            final BotUser botUser = jdbcTemplate.queryForObject(
                    "SELECT * FROM picture_bot.bot_user WHERE id = 6",
                    new BeanPropertyRowMapper<>(BotUser.class));

            assertNotNull(botUser);
            assertEquals(1, botUser.getRequestCount());

            randomPhotoCommand.respond(bot, update);

            // Verify that the bot didn't send a photo, because the user is on cool down:
            verify(bot, times(2)).execute(sendMessageArgumentCaptor.capture());
            assertEquals(1, botUser.getRequestCount());
            final SendMessage sendMessage = sendMessageArgumentCaptor.getValue();
            assertEquals("6", sendMessage.getChatId());
            assertEquals("✋ You can request a new picture in 5 seconds.", sendMessage.getText());
        }
    }
}
