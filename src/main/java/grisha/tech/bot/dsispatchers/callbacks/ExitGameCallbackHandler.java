package grisha.tech.bot.dsispatchers.callbacks;

import grisha.tech.bot.services.GameService;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.keyBoards.KeyBoardService;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class ExitGameCallbackHandler implements CallbackHandler {

    private final GameService gameService;
    private final MessageUtils utils;
    private final SenderMessageService senderMessageService;

    private final KeyBoardService keyBoardService;

    @Override
    public boolean supports(String data) {
        return data.startsWith("exit_game_");
    }

    @Override
    public void handle(CallbackQuery callback) {
        String data = callback
                .getData()
                .replace("exit_game_", "");
        Long gameId = Long.parseLong(data);
        Long telegramId = callback.getFrom().getId();
        Long chatId = callback.getMessage().getChatId();

        gameService.exitGame(gameId, telegramId);

        senderMessageService.answer("Вы покинули игру", callback.getId());

        senderMessageService.sendMessage(
                chatId,
                utils.getMessage("deleteGame.success"),
                keyBoardService.mainMenuKeyboard()
        );
    }
}
