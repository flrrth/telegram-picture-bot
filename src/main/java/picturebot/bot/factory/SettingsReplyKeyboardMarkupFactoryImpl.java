package picturebot.bot.factory;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class SettingsReplyKeyboardMarkupFactoryImpl implements SettingsReplyKeyboardMarkupFactory {

    private final MessageSource messageSource;

    public SettingsReplyKeyboardMarkupFactoryImpl(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public ReplyKeyboardMarkup create(final String url, final Locale locale) {
        final KeyboardButton keyboardButton = KeyboardButton.builder()
                .text(messageSource.getMessage("keyboardButton.settings", null, locale))
                .webApp(WebAppInfo.builder().url(url).build())
                .build();

        final List<KeyboardRow> rows = new ArrayList<>();
        final KeyboardRow row = new KeyboardRow();
        row.add(keyboardButton);
        rows.add(row);

        return ReplyKeyboardMarkup.builder()
                .keyboard(rows)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }
}
