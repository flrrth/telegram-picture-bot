package picturebot.bot.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Component
public class SendPhotoFactoryImpl implements SendPhotoFactory {

    @Override
    public SendPhoto getSendPhoto() {
        return new SendPhoto();
    }

    @Override
    public SendPhoto getSendPhoto(final Long chatId, final InputFile photo, final Boolean hasSpoiler) {
        return SendPhoto.builder()
                .chatId(chatId)
                .photo(photo)
                .hasSpoiler(hasSpoiler)
                .build();
    }

    @Override
    public SendPhoto getSendPhoto(final Long chatId, final InputFile photo, final Boolean hasSpoiler,
                                  final String caption) {

        return SendPhoto.builder()
                .chatId(chatId)
                .photo(photo)
                .hasSpoiler(hasSpoiler)
                .caption(caption)
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
    }
}
