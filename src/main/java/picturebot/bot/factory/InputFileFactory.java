package picturebot.bot.factory;

import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.nio.file.Path;

public interface InputFileFactory {

    InputFile getInputFile(String path);

    InputFile getInputFile(Path path);
}
