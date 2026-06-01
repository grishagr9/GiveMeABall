package grisha.tech.bot.dsispatchers.callbacks;

import grisha.tech.bot.data.enums.UserState;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserService;
import grisha.tech.bot.services.UserStateService;
import grisha.tech.bot.services.keyBoards.KeyBoardService;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class SetLevelCallbackHandler implements CallbackHandler {

    private final UserService userService;
    private final UserStateService userStateService;
    private final SenderMessageService telegramSender;
    private final MessageUtils utils;
    private final KeyBoardService keyBoardService;

    @Override
    public boolean supports(String data) {
        return data.startsWith("level_");
    }

    @Override
    public void handle(CallbackQuery callback) {
        String data = callback
                .getData()
                .replace("level_", "");
        Integer level = Integer.parseInt(data);
        Long telegramId = callback.getFrom().getId();
        Long chatId = callback.getMessage().getChatId();

        var currentState = userStateService.getState(telegramId);
        if (currentState == UserState.WAITING_FOR_LEVEL) {
            userService.updateLevel(telegramId, level);
            userStateService.setState(
                    telegramId,
                    UserState.NONE
            );
            telegramSender.answer("Сохранен уровень " + level, callback.getId());
            telegramSender.sendMessage(
                    chatId,
                    utils.getMessage("registration.completed"),
                    keyBoardService.mainMenuKeyboard()
            );
        } else {
            telegramSender.sendMessage(
                    chatId,
                    utils.getMessage("error.invalidInput"),
                    keyBoardService.mainMenuKeyboard()
            );
        }
    }
}