package picturebot;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import picturebot.scheduler.Scheduler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.env.MockEnvironment;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@TestConfiguration
public class DailyPictureBotTestConfiguration {

    @Bean
    @Primary
    public Clock fixedClock() {
        final LocalDateTime NOON = LocalDateTime.of(2024, 1, 2, 12, 0);
        return Clock.fixed(NOON.atZone(ZoneId.of("Europe/Amsterdam")).toInstant(),
                ZoneId.of("Europe/Amsterdam"));
    }

    @Bean
    public MockEnvironment environment() {
        final MockEnvironment environment = new MockEnvironment();
        environment.setProperty("bot.basePath", "/");
        environment.setProperty("bot.regular.subPath", "animals");
        environment.setProperty("bot.regular.greeting", "Have a nice day!");
        environment.setProperty("bot.isSpoiler", "true");
        environment.setProperty("bot.cache.ids.duration", "5");
        environment.setProperty("bot.cache.pictures.duration", "5");
        environment.setProperty("bot.cache.greetings.duration", "5");
        environment.setProperty("bot.timezone", "Europe/Amsterdam");
        environment.setProperty("bot.randomCommand", "/random");
        environment.setProperty("bot.webappHost", "localhost");
        environment.setProperty("bot.webappPath", "/page/index.html");
        return environment;
    }

    @Bean
    @Primary
    public FileSystem jimfsFileSystem() {
        return Jimfs.newFileSystem(Configuration.unix());
    }

    @Bean
    @Primary
    public Path mockedBasePath(final FileSystem fileSystem, final MockEnvironment environment) {
        return fileSystem.getPath(environment.getRequiredProperty("bot.basePath"));
    }

    @MockBean
    @SuppressWarnings("unused")
    public Scheduler scheduler;

    @MockBean
    public AbsSender bot;
}
