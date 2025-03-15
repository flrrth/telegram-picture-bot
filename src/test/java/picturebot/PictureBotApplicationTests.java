package picturebot;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Tag("integration")
@Import(DailyPictureBotTestConfiguration.class)
class PictureBotApplicationTests {

	@Test
	void contextLoads() {
	}
}
