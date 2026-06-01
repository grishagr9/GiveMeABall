package grisha.tech.bot.services.keyBoards;

import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JoinGameKeyboardFactory {

    private final MessageUtils utils;

    public InlineKeyboardMarkup joinGameButton(Long id) {
        InlineKeyboardButton joinButton =
                inlineButton(utils.getMessage("joinGame.get"), "join_game_" + id);
        InlineKeyboardRow row1 = new InlineKeyboardRow(List.of(
                joinButton
        ));

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1))
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
