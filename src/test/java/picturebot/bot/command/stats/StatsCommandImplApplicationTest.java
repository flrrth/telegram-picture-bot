package picturebot.bot.command.stats;

import picturebot.DailyPictureBotTestConfiguration;
import picturebot.emoticons.Emoticons;
import picturebot.fixtures.UpdateFixture;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.util.FileSystemUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@Tag("integration")
@Import(DailyPictureBotTestConfiguration.class)
public class StatsCommandImplApplicationTest {

    @Autowired private StatsCommandImpl statsCommandImpl;
    @Autowired private Path mockedBasePath;
    @Autowired private Environment environment;
    @Autowired private AbsSender bot;

    @Captor private ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    @BeforeEach
    void resetContainerInjectedMocks() {
        reset(bot);
    }

    @Nested
    @DisplayName("respond()")
    class Respond {

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

        @Test
        @DisplayName("should respond with the English stats text")
        void shouldReturnWithTheEnglishStatsResponseText() throws TelegramApiException {
            final Update update = UpdateFixture.createBasicUpdate( "en");

            // Act:
            statsCommandImpl.respond(bot, update);

            verify(bot).execute(sendMessageArgumentCaptor.capture());
            final SendMessage sendMessage = sendMessageArgumentCaptor.getValue();
            assertEquals("6", sendMessage.getChatId());
            assertEquals(String.format("%c I have 3 pictures available.",
                    Emoticons.ARTIST_PALETTE), sendMessage.getText());
        }

        @Test
        @DisplayName("should respond with the Dutch stats text")
        void shouldReturnWithTheDutchStatsResponseText() throws TelegramApiException {
            final Update update = UpdateFixture.createBasicUpdate("nl");

            // Act:
            statsCommandImpl.respond(bot, update);

            verify(bot).execute(sendMessageArgumentCaptor.capture());
            final SendMessage sendMessage = sendMessageArgumentCaptor.getValue();
            assertEquals("6", sendMessage.getChatId());
            assertEquals(String.format("%c Ik heb 3 afbeeldingen beschikbaar.",
                    Emoticons.ARTIST_PALETTE), sendMessage.getText());
        }
    }
}
