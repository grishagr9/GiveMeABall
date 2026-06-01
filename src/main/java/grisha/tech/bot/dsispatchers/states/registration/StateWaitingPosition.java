package grisha.tech.bot.dsispatchers.states.registration;

import grisha.tech.bot.data.enums.UserState;
import grisha.tech.bot.dsispatchers.states.StateHandler;
import grisha.tech.bot.services.keyBoards.KeyBoardService;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserService;
import grisha.tech.bot.services.UserStateService;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class StateWaitingPosition implements StateHandler {

    private final UserService userService;
    private final UserStateService userStateService;
    private final SenderMessageService telegramSender;
    private final MessageUtils utils;
    private final KeyBoardService keyBoardService;

    @Override
    public UserState state() {
        return UserState.WAITING_FOR_POSITION;
    }

    @Override
    public void handle(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        String textMessage = update.getMessage().getText();

        userService.updatePosition(telegramId, textMessage);
        userStateService.setState(
                telegramId,
                UserState.WAITING_FOR_LEVEL
        );

        telegramSender.sendMessage(
                chatId,
                utils.getMessage("registration.level"),
                keyBoardService.levelsKeyboard()
        );
    }
}
