package picturebot.bot.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.nio.file.Path;

@Component
public class InputFileFactoryImpl implements InputFileFactory {

    @Override
    public InputFile getInputFile(final String path) {
        return getInputFile(Path.of(path));
    }

    @Override
    public InputFile getInputFile(final Path path) {
        return new InputFile(path.toFile());
    }
}
