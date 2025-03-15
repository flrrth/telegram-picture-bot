package picturebot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import picturebot.bot.command.version.VersionCommandImpl;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZoneId;
import java.util.Random;

/**
 * Configuration class for the Picture Bot.
 */
@Configuration
@EnableAspectJAutoProxy
public class PictureBotConfiguration {

    /**
     * Creates a Clock bean. It exists to allow the bot to be tested with a fixed time.
     * @param environment the environment
     * @return a Clock bean
     */
    @Bean
    public Clock clock(final Environment environment) {
        return Clock.system(ZoneId.of(environment.getRequiredProperty("bot.timezone")));
    }

    /**
     * Creates a ResourceBundleMessageSource bean. It is used to load the messages from the i18n/messages.properties
     * file.
     * @return a ResourceBundleMessageSource bean
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Creates an ObjectMapper bean. It is used to serialize and deserialize objects to and from JSON.
     * @return an ObjectMapper bean
     */
    @Bean
    public ObjectMapper customObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    /**
     * Creates a FileSystem bean. It exists to allow the bot to be tested with an in-memory file system.
     * @return a FileSystem bean
     */
    @Bean
    public FileSystem fileSystem() {
        return FileSystems.getDefault();
    }

    /**
     * Creates a Path bean. It is used to store the base path of the bot.
     * @param fileSystem the file system
     * @param environment the environment
     * @return a Path bean
     */
    @Bean
    public Path basePath(final FileSystem fileSystem, final Environment environment) {
        return fileSystem.getPath(environment.getRequiredProperty("bot.basePath"));
    }

    /**
     * Creates a Random bean. It exists to allow the bot to be tested with a fixed seed.
     * @return a Random bean
     */
    @Bean
    public Random random() {
        return new Random();
    }

    /**
     * Creates a String bean. It is used to retrieve the version of the bot.
     * @return a String bean
     */
    @Bean
    public String version() {
        if (VersionCommandImpl.class.getPackage().getImplementationVersion() != null) {
            return VersionCommandImpl.class.getPackage().getImplementationVersion();
        }
        else {
            return "??";
        }
    }
}
