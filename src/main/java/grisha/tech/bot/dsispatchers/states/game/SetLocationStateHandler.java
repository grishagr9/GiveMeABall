package grisha.tech.bot.dsispatchers.states.game;

import grisha.tech.bot.data.enums.UserState;
import grisha.tech.bot.dsispatchers.states.StateHandler;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserStateService;
import grisha.tech.bot.services.keyBoards.DateKeyboardFactory;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static grisha.tech.bot.data.enums.UserState.WAITING_FOR_GAME_DAY;
import static grisha.tech.bot.data.enums.UserState.WAITING_FOR_GAME_LOCATION;

@Component
@RequiredArgsConstructor
public class SetLocationStateHandler implements StateHandler {

    private final MessageUtils utils;
    private final UserStateService stateService;
    private final SenderMessageService sender;
    private final DateKeyboardFactory dateKeyboardFactory;

    @Override
    public UserState state() {
        return WAITING_FOR_GAME_LOCATION;
    }

    @Override
    public void handle(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        String location = update.getMessage().getText();

        var context = stateService.getContext(telegramId);
        context.setLocation(location);
        stateService.saveContext(telegramId, context);

        sender.sendMessage(
                chatId,
                utils.getMessage("createGame.date"),
                dateKeyboardFactory.build()
        );

        stateService.setState(telegramId, WAITING_FOR_GAME_DAY);
    }
}
