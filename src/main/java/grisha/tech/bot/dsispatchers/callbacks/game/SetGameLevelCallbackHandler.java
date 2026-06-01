package grisha.tech.bot.dsispatchers.callbacks.game;

import grisha.tech.bot.data.dto.CreateGameContext;
import grisha.tech.bot.dsispatchers.callbacks.CallbackHandler;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserStateService;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static grisha.tech.bot.data.enums.UserState.WAITING_FOR_GAME_TITLE;

@Component
@RequiredArgsConstructor
public class SetGameLevelCallbackHandler implements CallbackHandler {

    private final UserStateService stateService;
    private final UserStateService userStateService;
    private final SenderMessageService sender;
    private final MessageUtils utils;

    @Override
    public boolean supports(String data) {
        return data.startsWith("game_level_");
    }

    @Override
    public void handle(CallbackQuery callback) {
        String data = callback
                .getData()
                .replace("game_level_", "");
        Integer level = Integer.parseInt(data);
        Long telegramId = callback.getFrom().getId();
        Long chatId = callback.getMessage().getChatId();

        CreateGameContext context = userStateService.getContext(telegramId);
        context.setLevel(level);
        userStateService.saveContext(telegramId, context);

        sender.sendMessage(
                chatId,
                buildTimeSelectionMessage(context),
                null
        );

        sender.sendMessage(
                chatId,
                utils.getMessage("createGame.name"),
                null
        );
        stateService.setState(telegramId, WAITING_FOR_GAME_TITLE);
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
                🏆 Уровень игры: %s
                """.formatted(
                context.getLocation(),
                context.getGameDate(),
                context.getGameTime(),
                context.getTotalSlots(),
                context.getLevel()
        );
    }
}