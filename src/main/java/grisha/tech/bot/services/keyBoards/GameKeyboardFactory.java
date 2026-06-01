package grisha.tech.bot.services.keyBoards;

import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GameKeyboardFactory {

    private final MessageUtils utils;

    public InlineKeyboardMarkup gameButtons(Long id, boolean isOrganizer) {
        List<InlineKeyboardRow> keyboardButtons = new ArrayList<>();

        InlineKeyboardButton exitButton =
                inlineButton(utils.getMessage("myGames.delete"), "exit_game_" + id);
        InlineKeyboardRow row1 = new InlineKeyboardRow(List.of(
                exitButton
        ));
        keyboardButtons.add(row1);

        if (isOrganizer) {
            InlineKeyboardButton playersButton =
                    inlineButton(utils.getMessage("myGames.players"), "players_game_" + id);
            InlineKeyboardRow row2 = new InlineKeyboardRow(List.of(
                    playersButton
            ));
            keyboardButtons.add(row2);
        }

        return InlineKeyboardMarkup.builder()
                .keyboard(keyboardButtons)
                .build();
    }

    public InlineKeyboardMarkup giveBallButton(Long id) {
        List<InlineKeyboardRow> keyboardButtons = new ArrayList<>();

        InlineKeyboardButton exitButton =
                inlineButton(utils.getMessage("joinGame.ball"), "ball_game_" + id);
        InlineKeyboardRow row1 = new InlineKeyboardRow(List.of(
                exitButton
        ));
        keyboardButtons.add(row1);

        return InlineKeyboardMarkup.builder()
                .keyboard(keyboardButtons)
                .build();
    }

    private InlineKeyboardButton inlineButton(
            String text,
            String callbackData
    ) {

        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }
}
