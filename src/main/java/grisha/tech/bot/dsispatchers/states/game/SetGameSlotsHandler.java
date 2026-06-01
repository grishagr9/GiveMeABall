package grisha.tech.bot.dsispatchers.states.game;

import grisha.tech.bot.data.dto.CreateGameContext;
import grisha.tech.bot.data.enums.UserState;
import grisha.tech.bot.dsispatchers.states.StateHandler;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserStateService;
import grisha.tech.bot.services.keyBoards.KeyBoardService;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static grisha.tech.bot.data.enums.UserState.WAITING_FOR_GAME_LEVEL;
import static grisha.tech.bot.data.enums.UserState.WAITING_FOR_GAME_SLOTS;

@Component
@RequiredArgsConstructor
public class SetGameSlotsHandler implements StateHandler {

    private final MessageUtils utils;
    private final UserStateService stateService;
    private final SenderMessageService sender;
    private final UserStateService userStateService;
    private final KeyBoardService keyBoardService;

    @Override
    public UserState state() {
        return WAITING_FOR_GAME_SLOTS;
    }

    @Override
    public void handle(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        Integer slots = Integer.parseInt(update.getMessage().getText());


        CreateGameContext context = userStateService.getContext(telegramId);
        context.setTotalSlots(slots);
        userStateService.saveContext(telegramId, context);

        sender.sendMessage(
                chatId,
                buildTimeSelectionMessage(context),
                null
        );

        sender.sendMessage(
                chatId,
                utils.getMessage("createGame.level"),
                keyBoardService.gameLevelsKeyboard()
        );
        stateService.setState(telegramId, WAITING_FOR_GAME_LEVEL);
    }

    private String buildTimeSelectionMessage(
            CreateGameContext context
    ) {

        return """
                ⚽️ Создание игры
                
                📍 Место: %s
                📅 Дата: %s
                🕒 Время: %s
                ⚽️ Количество участников: %s
                """.formatted(
                context.getLocation(),
                context.getGameDate(),
                context.getGameTime(),
                context.getTotalSlots()
        );
    }
}
