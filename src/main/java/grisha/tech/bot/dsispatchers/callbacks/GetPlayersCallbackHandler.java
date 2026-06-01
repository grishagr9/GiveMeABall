package grisha.tech.bot.dsispatchers.callbacks;

import grisha.tech.bot.data.dto.Player;
import grisha.tech.bot.services.GameService;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.keyBoards.KeyBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetPlayersCallbackHandler implements CallbackHandler {

    private final GameService gameService;
    private final SenderMessageService senderMessageService;
    private final KeyBoardService keyBoardService;

    @Override
    public boolean supports(String data) {
        return data.startsWith("players_game_");
    }

    @Override
    public void handle(CallbackQuery callback) {
        String data = callback
                .getData()
                .replace("players_game_", "");
        Long gameId = Long.parseLong(data);
        Long chatId = callback.getMessage().getChatId();

        var players = gameService.getPlayers(gameId);

        senderMessageService.answer("Список участников", callback.getId());

        senderMessageService.sendMessage(
                chatId,
                buildPlayersList(players),
                keyBoardService.mainMenuKeyboard()
        );
    }

    private String buildPlayersList(List<Player> players) {
        StringBuilder sb = new StringBuilder();
        sb.append("Список игроков:\n\n");

        for (Player player : players) {
            sb.append("@");
            sb.append(player.name());
            if (player.ball()) {
                sb.append(" ⚽");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
