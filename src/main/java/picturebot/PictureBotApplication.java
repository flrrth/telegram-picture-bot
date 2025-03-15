package picturebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PictureBotApplication {

	public static void main(final String[] args) {
		SpringApplication.run(PictureBotApplication.class, args);
	}
}
