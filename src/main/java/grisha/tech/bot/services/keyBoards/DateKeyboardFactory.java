package grisha.tech.bot.services.keyBoards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class DateKeyboardFactory {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("E, d MMM", new Locale("ru"));

    public InlineKeyboardMarkup build() {

        List<InlineKeyboardRow> rows = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (int i = 0; i < 6; i++) {
            LocalDate date = today.plusDays(i);

            String text = capitalize(
                    date.format(FORMATTER)
            );

            String callbackData = "DATE:" + date;

            InlineKeyboardButton button =
                    InlineKeyboardButton.builder()
                            .text(text)
                            .callbackData(callbackData)
                            .build();

            rows.add(new InlineKeyboardRow(button));
        }

        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }

        return value.substring(0, 1).toUpperCase()
                + value.substring(1);
    }
}