package grisha.tech.bot.services.keyBoards;

import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyBoardService {

    private final MessageUtils utils;

    /*
     * =========================
     * MAIN MENU
     * =========================
     */

    public ReplyKeyboardMarkup mainMenuKeyboard() {

        KeyboardButton joinGame =
                new KeyboardButton(utils.getMessage("menu.joinGame"));

        KeyboardButton manageGames =
                new KeyboardButton(utils.getMessage("menu.manageGames"));

        KeyboardButton myGames =
                new KeyboardButton(utils.getMessage("menu.myGames"));

        KeyboardButton profile =
                new KeyboardButton(utils.getMessage("menu.profile"));

        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(joinGame);
        firstRow.add(manageGames);

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(myGames);
        secondRow.add(profile);

        List<KeyboardRow> rows = new ArrayList<>();

        rows.add(firstRow);
        rows.add(secondRow);

        return ReplyKeyboardMarkup.builder()
                .keyboard(rows)
                .resizeKeyboard(true)
                .selective(true)
                .build();
    }

    /*
     * =========================
     * MANAGE GAMES
     * =========================
     */

    public ReplyKeyboardMarkup manageGamesKeyboard() {

        KeyboardButton createGame =
                new KeyboardButton(utils.getMessage("manageGames.create"));

        KeyboardButton deleteGame =
                new KeyboardButton(utils.getMessage("manageGames.delete"));

        KeyboardButton back =
                new KeyboardButton("⬅️ Назад");

        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(createGame);
        firstRow.add(deleteGame);

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(back);

        List<KeyboardRow> rows = new ArrayList<>();

        rows.add(firstRow);
        rows.add(secondRow);

        return ReplyKeyboardMarkup.builder()
                .keyboard(rows)
                .resizeKeyboard(true)
                .build();
    }

    /*
     * =========================
     * LEVELS
     * =========================
     */

    public InlineKeyboardMarkup gameLevelsKeyboard() {

        InlineKeyboardButton level1 =
                inlineButton("1️⃣", "game_level_1");

        InlineKeyboardButton level2 =
                inlineButton("2️⃣", "game_level_2");

        InlineKeyboardButton level3 =
                inlineButton("3️⃣", "game_level_3");

        InlineKeyboardButton level4 =
                inlineButton("4️⃣", "game_level_4");

        InlineKeyboardButton level5 =
                inlineButton("5️⃣", "game_level_5");

        List<InlineKeyboardButton> row = List.of(
                level1,
                level2,
                level3,
                level4,
                level5
        );
        InlineKeyboardRow row1 = new InlineKeyboardRow(row);

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1))
                .build();
    }

    public InlineKeyboardMarkup levelsKeyboard() {

        InlineKeyboardButton level1 =
                inlineButton(utils.getMessage("level.1"), "level_1");

        InlineKeyboardButton level2 =
                inlineButton(utils.getMessage("level.2"), "level_2");

        InlineKeyboardButton level3 =
                inlineButton(utils.getMessage("level.3"), "level_3");

        InlineKeyboardButton level4 =
                inlineButton(utils.getMessage("level.4"), "level_4");

        InlineKeyboardButton level5 =
                inlineButton(utils.getMessage("level.5"), "level_5");

        InlineKeyboardRow row1 = new InlineKeyboardRow(List.of(
                level1
        ));
        InlineKeyboardRow row2 = new InlineKeyboardRow(List.of(
                level2
        ));
        InlineKeyboardRow row3 = new InlineKeyboardRow(List.of(
                level3
        ));
        InlineKeyboardRow row4 = new InlineKeyboardRow(List.of(
                level4
        ));
        InlineKeyboardRow row5 = new InlineKeyboardRow(List.of(
                level5
        ));

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2, row3, row4, row5))
                .build();
    }

    public InlineKeyboardMarkup confirmationKeyboard() {
        InlineKeyboardButton yes = inlineButton("✅ Да", "confirm_game_yes");
        InlineKeyboardButton no = inlineButton("❌ Нет", "confirm_game_no");

        InlineKeyboardRow row = new InlineKeyboardRow(List.of(yes, no));

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .build();
    }

    /*
     * =========================
     * BACK KEYBOARD
     * =========================
     */

    public ReplyKeyboardMarkup backKeyboard() {

        KeyboardButton back =
                new KeyboardButton("⬅️ Назад");

        KeyboardRow row = new KeyboardRow();
        row.add(back);

        ReplyKeyboardMarkup keyboard = ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .resizeKeyboard(true)
                .build();

        return keyboard;
    }

    /*
     * =========================
     * CANCEL KEYBOARD
     * =========================
     */

    public ReplyKeyboardMarkup cancelKeyboard() {

        KeyboardButton cancel =
                new KeyboardButton("❌ Отмена");

        KeyboardRow row = new KeyboardRow();
        row.add(cancel);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .resizeKeyboard(true)
                .build();
    }

    /*
     * =========================
     * UTIL
     * =========================
     */

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
