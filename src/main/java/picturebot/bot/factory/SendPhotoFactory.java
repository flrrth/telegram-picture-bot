package picturebot.bot.factory;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

public interface SendPhotoFactory {

    SendPhoto getSendPhoto();

    SendPhoto getSendPhoto(Long chatId, InputFile photo, Boolean hasSpoiler);

    SendPhoto getSendPhoto(Long chatId, InputFile photo, Boolean hasSpoiler, String caption);
}
