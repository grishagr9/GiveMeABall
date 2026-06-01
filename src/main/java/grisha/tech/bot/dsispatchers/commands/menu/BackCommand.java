package grisha.tech.bot.dsispatchers.commands.menu;

import grisha.tech.bot.dsispatchers.commands.Command;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.keyBoards.KeyBoardService;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BackCommand implements Command {

    private final MessageUtils utils;
    private String command;
    private final SenderMessageService sender;
    private final KeyBoardService keyBoardService;

    public BackCommand(MessageUtils utils, SenderMessageService sender, KeyBoardService keyBoardService) {
        this.utils = utils;

        command = utils.getMessage("common.back");
        this.sender = sender;
        this.keyBoardService = keyBoardService;
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
                utils.getMessage("menu.main"),
                keyBoardService.mainMenuKeyboard()
        );

    }
}
