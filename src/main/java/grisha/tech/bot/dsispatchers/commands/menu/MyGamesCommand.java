package grisha.tech.bot.dsispatchers.commands.menu;

import grisha.tech.bot.dsispatchers.commands.Command;
import grisha.tech.bot.mappers.GameMapper;
import grisha.tech.bot.services.GameService;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserService;
import grisha.tech.bot.services.keyBoards.DeleteGameKeyboardFactory;
import grisha.tech.bot.services.keyBoards.GameKeyboardFactory;
import grisha.tech.bot.services.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Component
public class MyGamesCommand implements Command {

    private final MessageUtils utils;
    private final GameService gameService;
    private String command;
    private final SenderMessageService sender;
    private final GameKeyboardFactory keyboardFactory;
    private final UserService userService;
    private final GameMapper mapper;

    public MyGamesCommand(MessageUtils utils,
                          GameService gameService, SenderMessageService sender, GameKeyboardFactory keyboardFactory, UserService userService, GameMapper mapper) {
        this.utils = utils;

        command = utils.getMessage("menu.myGames");
        this.gameService = gameService;
        this.sender = sender;
        this.keyboardFactory = keyboardFactory;
        this.userService = userService;
        this.mapper = mapper;
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
        var user = userService.findByTelegramId(telegramId);

        var games = gameService.getMyGame(telegramId);
        if (games.isEmpty()) {

            return;
        }

        games.forEach(game -> {
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
                    keyboardFactory.gameButtons(game.id(),
                            Objects.equals(user.getUsername(), game.organizerUsername()))
            );
        });
    }
}