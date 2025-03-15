package picturebot.bot.factory;

import org.telegram.telegrambots.meta.api.methods.GetFile;

public interface GetFileFactory {

    GetFile createGetFileMethod(String fileId);
}
