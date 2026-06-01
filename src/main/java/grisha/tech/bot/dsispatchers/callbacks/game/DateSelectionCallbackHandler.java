package grisha.tech.bot.dsispatchers.callbacks.game;

import grisha.tech.bot.data.dto.CreateGameContext;
import grisha.tech.bot.data.enums.UserState;
import grisha.tech.bot.dsispatchers.callbacks.CallbackHandler;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserStateService;
import grisha.tech.bot.services.keyBoards.TimeKeyboardFactory;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DateSelectionCallbackHandler implements CallbackHandler {

    private static final String PREFIX = "DATE:";

    private final UserStateService userStateService;
    private final SenderMessageService telegramSender;
    private final TimeKeyboardFactory timeKeyboardFactory;
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
        String rawDate = callbackData.replace(PREFIX, "");
        LocalDate selectedDate = LocalDate.parse(rawDate);

        CreateGameContext context = userStateService.getContext(telegramId);
        context.setGameDate(selectedDate);
        userStateService.saveContext(telegramId, context);

        userStateService.setState(
                telegramId,
                UserState.WAITING_FOR_GAME_TIME
        );

        telegramSender.answer("Дата успешно выбрана", callbackQuery.getId());

        telegramSender.sendMessage(
                chatId,
                buildTimeSelectionMessage(context)
        );

        telegramSender.sendMessage(
                chatId,
                utils.getMessage("createGame.time"),
                timeKeyboardFactory.build()
        );
    }

    private String buildTimeSelectionMessage(
            CreateGameContext context
    ) {

        return """
                ⚽️ Создание игры
                
                📍 Место: %s
                📅 Дата: %s
                """.formatted(
                context.getLocation(),
                context.getGameDate()
        );
    }
}