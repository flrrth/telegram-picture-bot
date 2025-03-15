package picturebot.bot.factory;

import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;

public interface SendChatActionFactory {

    SendChatAction getSendChatAction();

    SendChatAction getSendChatActionForPhotoUpload(Long chatId);
}
