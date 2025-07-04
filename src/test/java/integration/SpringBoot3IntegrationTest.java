package integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import picturebot.PictureBotApplication;

/**
 * Simple integration test to verify the application can start with the new TelegramBots library.
 */
@SpringBootTest(classes = PictureBotApplication.class)
@TestPropertySource(properties = {
    "PICTURE_BOT_TOKEN=dummy_token_for_test",
    "bot.name=TestBot",
    "bot.randomCommand=/random",
    "bot.timezone=UTC",
    "bot.basePath=/tmp",
    "bot.cache.greetings.duration=60",
    "bot.cache.picture.duration=60",
    "bot.cache.users.duration=60",
    "bot.cooldown=5",
    "bot.webapp.url=https://example.com"
})
class SpringBoot3IntegrationTest {

    @Test
    void contextLoads() {
        // This test will pass if the Spring context can be loaded successfully
        // with the new TelegramBots library and Spring Boot 3
    }
}