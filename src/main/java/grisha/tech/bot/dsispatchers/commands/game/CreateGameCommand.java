package grisha.tech.bot.dsispatchers.commands.game;

import grisha.tech.bot.data.dto.CreateGameContext;
import grisha.tech.bot.dsispatchers.commands.Command;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserStateService;
import grisha.tech.bot.services.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static grisha.tech.bot.data.enums.UserState.WAITING_FOR_GAME_LOCATION;

@Component
public class CreateGameCommand implements Command {

    private String command;
    private final MessageUtils utils;
    private final UserStateService stateService;
    private final SenderMessageService sender;

    public CreateGameCommand(MessageUtils utils,
                             UserStateService stateService,
                             SenderMessageService sender
    ) {
        this.utils = utils;
        command = utils.getMessage("manageGames.create");
        this.stateService = stateService;
        this.sender = sender;
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

        stateService.setState(telegramId, WAITING_FOR_GAME_LOCATION);
        stateService.saveContext(telegramId, new CreateGameContext());
        sender.sendMessage(
                chatId,
                utils.getMessage("createGame.location"),
                null
        );
    }
}
