package grisha.tech.bot.services;

import grisha.tech.bot.model.entities.GameEntity;
import grisha.tech.bot.model.repo.GameRepository;
import grisha.tech.bot.services.keyBoards.JoinGameKeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final GameRepository gameRepository;
    private final SenderMessageService telegramSender;
    private final JoinGameKeyboardFactory keyboardFactory;

    public void processInvite(
            Long telegramId,
            String inviteCode
    ) {
        GameEntity game = gameRepository
                        .findByGameLink(inviteCode)
                        .orElseThrow();

        telegramSender.sendMessage(
                telegramId,
                buildGameInviteMessage(game),
                keyboardFactory.joinGameButton(game.getId())
        );
    }

    //TODO текст вынести в ресурсы
    private String buildGameInviteMessage(
            GameEntity game
    ) {

        return """
                ⚽️ Вас пригласили на игру
                
                📍 %s
                🕒 %s
                
                Нажмите кнопку ниже, чтобы присоединиться.
                """.formatted(
                game.getLocation(),
                game.getGameTime()
        );
    }
}