package grisha.tech.bot.services.keyBoards;

import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KeyboardRegistrationFactory {

    private final MessageUtils utils;

    public ReplyKeyboardMarkup positionKeyBoard() {

        KeyboardButton goalkeeper =
                new KeyboardButton(utils.getMessage("position.goalkeeper"));

        KeyboardButton defender =
                new KeyboardButton(utils.getMessage("position.defender"));

        KeyboardButton midfielder =
                new KeyboardButton(utils.getMessage("position.midfielder"));

        KeyboardButton forward =
                new KeyboardButton(utils.getMessage("position.forward"));

        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(goalkeeper);
        firstRow.add(defender);

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(midfielder);
        secondRow.add(forward);

        List<KeyboardRow> rows = new ArrayList<>();

        rows.add(firstRow);
        rows.add(secondRow);

        return ReplyKeyboardMarkup.builder()
                .keyboard(rows)
                .resizeKeyboard(true)
                .selective(true)
                .build();
    }
}