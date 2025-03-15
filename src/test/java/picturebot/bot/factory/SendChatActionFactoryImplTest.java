package picturebot.bot.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@Tag("unit")
class SendChatActionFactoryImplTest {

    private final SendChatActionFactoryImpl factory = new SendChatActionFactoryImpl();

    @Nested
    @DisplayName("getSendChatAction()")
    class GetSendMessage {

        @Test
        @DisplayName("should return SendChatAction instance")
        void shouldReturnSendChatActionInstance() {
            assertInstanceOf(SendChatAction.class, factory.getSendChatAction());
        }
    }

    @Nested
    @DisplayName("getSendChatActionForPhotoUplaod()")
    class GetSendChatActionForPhoto {

        @Test
        @DisplayName("should return SendChatAction instance with chat ID and action type")
        void shouldReturnSendChatActionInstanceWithChatIdAndActionType() {
            final SendChatAction expected = new SendChatAction();
            expected.setChatId(1L);
            expected.setAction(ActionType.UPLOADPHOTO);

            assertEquals(expected, factory.getSendChatActionForPhotoUpload(1L));
        }
    }
}
