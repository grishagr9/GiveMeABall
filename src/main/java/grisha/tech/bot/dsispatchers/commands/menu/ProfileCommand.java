package grisha.tech.bot.dsispatchers.commands.menu;

import grisha.tech.bot.dsispatchers.commands.Command;
import grisha.tech.bot.model.entities.UserEntity;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserService;
import grisha.tech.bot.services.keyBoards.KeyBoardService;
import grisha.tech.bot.services.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ProfileCommand implements Command {

    private final MessageUtils utils;

    private final UserService userService;

    private final SenderMessageService senderMessageService;

    private String command;
    private final KeyBoardService keyBoard;

    public ProfileCommand(MessageUtils utils,
                          UserService userService,
                          SenderMessageService senderMessageService, KeyBoardService keyBoard) {
        this.utils = utils;

        command = utils.getMessage("menu.profile");
        this.userService = userService;
        this.senderMessageService = senderMessageService;
        this.keyBoard = keyBoard;
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

        UserEntity user = userService.findByTelegramId(telegramId);
        senderMessageService.sendMessage(
                chatId,
                buildProfile(user),
                keyBoard.mainMenuKeyboard()
        );
    }

    private String buildProfile(
            UserEntity user
    ) {

        return """
                ⚽️ %s
                
                ⭐️ Амплуа: %s
                💎 Уровень: %s
                """.formatted(
                user.getFirstName(),
                user.getPosition(),
                utils.getMessage("level." + user.getSkillLevel())
        );
    }
}
