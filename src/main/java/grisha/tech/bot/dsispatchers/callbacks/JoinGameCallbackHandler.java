package grisha.tech.bot.dsispatchers.callbacks;

import grisha.tech.bot.services.GameService;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.keyBoards.GameKeyboardFactory;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class JoinGameCallbackHandler implements CallbackHandler {

    private final GameService gameService;
    private final MessageUtils utils;
    private final SenderMessageService senderMessageService;

    private final GameKeyboardFactory keyBoardService;

    @Override
    public boolean supports(String data) {
        return data.startsWith("join_game_");
    }

    @Override
    public void handle(CallbackQuery callback) {
        String data = callback
                .getData()
                .replace("join_game_", "");
        Long gameId = Long.parseLong(data);
        Long telegramId = callback.getFrom().getId();
        Long chatId = callback.getMessage().getChatId();

        gameService.joinGame(gameId, telegramId);

        senderMessageService.answer("Информация отправлена организатору", callback.getId());
        senderMessageService.sendMessage(
                chatId,
                utils.getMessage("joinGame.success"),
                keyBoardService.giveBallButton(gameId)
        );
    }
}
