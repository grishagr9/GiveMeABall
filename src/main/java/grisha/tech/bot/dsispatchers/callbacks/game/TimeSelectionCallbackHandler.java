package grisha.tech.bot.dsispatchers.callbacks.game;

import grisha.tech.bot.data.dto.CreateGameContext;
import grisha.tech.bot.data.enums.UserState;
import grisha.tech.bot.dsispatchers.callbacks.CallbackHandler;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserStateService;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class TimeSelectionCallbackHandler implements CallbackHandler {

    private static final String PREFIX = "TIME:";

    private final UserStateService userStateService;
    private final SenderMessageService telegramSender;
    private final MessageUtils utils;


    @Override
    public boolean supports(String callbackData) {
        return callbackData.startsWith(PREFIX);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        Long telegramId = callbackQuery.getFrom().getId();
        Long chatId = callbackQuery.getMessage().getChatId();

        String callbackData = callbackQuery.getData();
        String rawTime = callbackData.replace(PREFIX, "");
        LocalTime selectedTime = LocalTime.parse(rawTime);

        CreateGameContext context = userStateService.getContext(telegramId);
        context.setGameTime(selectedTime);
        userStateService.saveContext(telegramId, context);

        userStateService.setState(
                telegramId,
                UserState.WAITING_FOR_GAME_SLOTS
        );

        telegramSender.sendMessage(
                chatId,
                """
                        ⚽️ Создание игры
                        
                        📍 Район: %s
                        📅 Дата: %s
                        🕒 Время: %s
                        
                        Введите количество игроков:
                        """.formatted(
                        context.getLocation(),
                        context.getGameDate(),
                        context.getGameTime()
                )
        );

        telegramSender.sendMessage(
                chatId,
                utils.getMessage("createGame.slots"),
                null
        );
    }
}