package grisha.tech.bot.dsispatchers.states.registration;

import grisha.tech.bot.data.enums.UserState;
import grisha.tech.bot.dsispatchers.states.StateHandler;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserService;
import grisha.tech.bot.services.UserStateService;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class StateWaitingLevel implements StateHandler {

    private final UserService userService;
    private final UserStateService userStateService;
    private final SenderMessageService telegramSender;
    private final MessageUtils utils;

    @Override
    public UserState state() {
        return UserState.WAITING_FOR_LEVEL;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();

        telegramSender.sendMessage(
                chatId,
                utils.getMessage("registration.level")
        );
    }
}
