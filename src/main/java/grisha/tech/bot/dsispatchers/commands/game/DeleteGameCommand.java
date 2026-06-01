package grisha.tech.bot.dsispatchers.commands.game;

import grisha.tech.bot.dsispatchers.commands.Command;
import grisha.tech.bot.mappers.GameMapper;
import grisha.tech.bot.services.GameService;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.keyBoards.DeleteGameKeyboardFactory;
import grisha.tech.bot.services.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DeleteGameCommand implements Command {

    private String command;
    private final MessageUtils utils;
    private final DeleteGameKeyboardFactory deleteGameKeyboardFactory;
    private final SenderMessageService sender;
    private final GameService gameService;
    private final GameMapper mapper;

    public DeleteGameCommand(MessageUtils utils,
                             DeleteGameKeyboardFactory deleteGameKeyboardFactory,
                             SenderMessageService sender,
                             GameService gameService, GameMapper mapper
    ) {
        this.utils = utils;
        command=utils.getMessage("manageGames.delete");
        this.deleteGameKeyboardFactory = deleteGameKeyboardFactory;
        this.sender = sender;
        this.gameService = gameService;
        this.mapper = mapper;
    }

    @Override
    public Boolean canHandle(Update update) {
        return update.getMessage().hasText() &&
                update.getMessage().getText().equals(command);
    }

    @Override
    public void handle(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();

        sender.sendMessage(
                chatId,
                utils.getMessage("deleteGame.select"),
                null
        );

        gameService.getGamesCreated(telegramId).forEach(game -> {
            sender.sendMessage(
                    chatId,
                    utils.getMessage("game.info",
                            game.title(),
                            game.location(),
                            mapper.formatDate(game.gameTime()),
                            game.freeSlots().toString(),
                            game.level().toString(),
                            game.link(),
                            game.organizerUsername()
                    ),
                    deleteGameKeyboardFactory.deleteGameButton(game.id())
            );
        });
    }
}