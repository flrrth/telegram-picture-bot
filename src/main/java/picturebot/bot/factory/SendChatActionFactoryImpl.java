package picturebot.bot.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;

@Component
public class SendChatActionFactoryImpl implements SendChatActionFactory {

    @Override
    public SendChatAction getSendChatAction() {
        return new SendChatAction();
    }

    @Override
    public SendChatAction getSendChatActionForPhotoUpload(final Long chatId) {
        return SendChatAction.builder()
                .chatId(chatId)
                .action(ActionType.UPLOADPHOTO.toString())
                .build();
    }
}
