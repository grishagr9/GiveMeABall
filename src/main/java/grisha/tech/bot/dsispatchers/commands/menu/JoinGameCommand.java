package grisha.tech.bot.dsispatchers.commands.menu;

import grisha.tech.bot.data.dto.GameResponse;
import grisha.tech.bot.dsispatchers.commands.Command;
import grisha.tech.bot.services.GameService;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.keyBoards.JoinGameKeyboardFactory;
import grisha.tech.bot.services.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class JoinGameCommand implements Command {

    private final MessageUtils utils;
    private String command;

    private final GameService gameService;
    private final SenderMessageService sender;
    private final JoinGameKeyboardFactory keyboardFactory;

    public JoinGameCommand(MessageUtils utils,
                           GameService gameService,
                           SenderMessageService sender, JoinGameKeyboardFactory keyboardFactory
    ) {
        this.utils = utils;

        command = utils.getMessage("menu.joinGame");
        this.gameService = gameService;
        this.sender = sender;
        this.keyboardFactory = keyboardFactory;
    }

    @Override
    public Boolean canHandle(Update update) {
        return update.getMessage().hasText() &&
                update.getMessage().getText().equals(command);
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        Long telegramId = update.getMessage().getFrom().getId();

        var listGame = gameService.findOpened(telegramId);
        if (listGame.isEmpty()) {
            sender.sendMessage(
                    chatId,
                    utils.getMessage("joinGame.empty")
            );

            return;
        }

        sender.sendMessage(
                chatId,
                utils.getMessage("joinGame.select")
        );

        listGame.forEach(game -> sender.sendMessage(
                chatId,
                getGame(game),
                keyboardFactory.joinGameButton(game.id())
        ));
    }

    private String getGame(GameResponse game) {
        return """
                ⚽️ %s
                
                🏟 %s
                ⏰ %s
                
                Осталось мест: *%d*
                
                Организатор - @%s
                """.formatted(
                game.title(),
                game.location(),
                game.gameTime(),
                game.freeSlots(),
                game.organizerUsername()
        );
    }

}
