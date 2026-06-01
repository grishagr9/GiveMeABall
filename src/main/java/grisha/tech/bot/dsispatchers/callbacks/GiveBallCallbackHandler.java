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
public class GiveBallCallbackHandler implements CallbackHandler {

    private final GameService gameService;
    private final MessageUtils utils;
    private final SenderMessageService senderMessageService;

    private final KeyBoardService keyBoardService;

    @Override
    public boolean supports(String data) {
        return data.startsWith("ball_game_");
    }

    @Override
    public void handle(CallbackQuery callback) {
        String data = callback
                .getData()
                .replace("ball_game_", "");
        Long gameId = Long.parseLong(data);
        Long telegramId = callback.getFrom().getId();
        Long chatId = callback.getMessage().getChatId();

        gameService.addBall(gameId, telegramId);

        senderMessageService.sendMessage(
                chatId,
                utils.getMessage("joinGame.withBall"),
                keyBoardService.mainMenuKeyboard()
        );
    }
}
