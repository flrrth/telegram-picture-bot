package picturebot.bot.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class SettingsReplyKeyboardMarkupFactoryImplTest {

    @Mock private MessageSource messageSource;

    @InjectMocks private SettingsReplyKeyboardMarkupFactoryImpl factory;

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("create should return ReplyKeyboardMarkup instance")
        void createShouldReturnReplyKeyboardMarkupInstance() {
            when(messageSource.getMessage("keyboardButton.settings", null, Locale.ENGLISH))
                    .thenReturn("Settings");

            final ReplyKeyboardMarkup markup = factory.create("http://localhost", Locale.ENGLISH);

            assertThat(markup.getKeyboard().size(), is(1));
            assertThat(markup.getKeyboard().get(0).size(), is(1));
            assertThat(markup.getKeyboard().get(0).get(0).getText(), is(equalTo("Settings")));
            assertThat(markup.getKeyboard().get(0).get(0).getWebApp().getUrl(), is(equalTo("http://localhost")));
            assertThat(markup.getResizeKeyboard(), is(true));
            assertThat(markup.getOneTimeKeyboard(), is(true));
        }
    }
}
