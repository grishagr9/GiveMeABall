package grisha.tech.bot.services.keyBoards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class TimeKeyboardFactory {

    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (int hour = 10; hour <= 19; hour++) {
            String formatted =
                    String.format("%02d:00", hour);

            InlineKeyboardButton button =
                    InlineKeyboardButton.builder()
                            .text(formatted)
                            .callbackData("TIME:" + formatted)
                            .build();

            rows.add(new InlineKeyboardRow(button));
        }

        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }
}