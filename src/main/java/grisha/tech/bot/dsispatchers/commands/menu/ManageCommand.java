package grisha.tech.bot.dsispatchers.commands.menu;

import grisha.tech.bot.dsispatchers.commands.Command;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.keyBoards.KeyBoardService;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class ManageCommand implements Command {

    private final MessageUtils utils;
    private final KeyBoardService keyboards;
    private final SenderMessageService sender;
    private String command;

    public ManageCommand(MessageUtils utils,
                         KeyBoardService keyboards,
                         SenderMessageService sender
    ) {
        this.utils = utils;
        this.keyboards = keyboards;
        this.sender = sender;
        command = utils.getMessage("menu.manageGames");
    }

    @Override
    public Boolean canHandle(Update update) {
        return update.getMessage().hasText() &&
                update.getMessage().getText().equals(command);
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();

        sender.sendMessage(
                chatId,
                utils.getMessage("manageGames.title"),
                keyboards.manageGamesKeyboard()
        );

    }
}
