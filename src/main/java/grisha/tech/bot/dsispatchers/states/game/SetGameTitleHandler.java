package grisha.tech.bot.dsispatchers.states.game;

import grisha.tech.bot.data.dto.CreateGameContext;
import grisha.tech.bot.data.enums.UserState;
import grisha.tech.bot.dsispatchers.states.StateHandler;
import grisha.tech.bot.model.entities.GameEntity;
import grisha.tech.bot.services.GameService;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserStateService;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static grisha.tech.bot.data.enums.UserState.WAITING_FOR_GAME_TITLE;

@Component
@RequiredArgsConstructor
public class SetGameTitleHandler implements StateHandler {

    @Value("${telegram.bot.link}")
    private String link;

    private final MessageUtils utils;
    private final UserStateService stateService;
    private final SenderMessageService sender;
    private final UserStateService userStateService;

    private final GameService gameService;

    @Override
    public UserState state() {
        return WAITING_FOR_GAME_TITLE;
    }

    @Override
    public void handle(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();

        String title = update.getMessage().getText();
        CreateGameContext context = userStateService.getContext(telegramId);
        context.setTitle(title);

        userStateService.clearContext(telegramId);

        sender.sendMessage(
                chatId,
                buildTimeSelectionMessage(context),
                null
        );

        sender.sendMessage(
                chatId,
                utils.getMessage("createGame.success"),
                null
        );
        stateService.setState(telegramId, UserState.NONE);

        GameEntity game = gameService.create(context, telegramId);
        String gameLink = buildGameLink(game.getGameLink());
        sender.sendMessage(
                chatId,
                utils.getMessage("game.info",
                        game.getTitle(),
                        game.getLocation(),
                        game.getGameTime().toString(),
                        game.getTotalSlots().toString(),
                        game.getLevel().toString(),
                        gameLink,
                        game.getOrganizer().getUsername()
                ),
                null
        );
    }

    private String buildGameLink(String linkId) {
        return link + "join_" + linkId;
    }

    private String buildTimeSelectionMessage(
            CreateGameContext context
    ) {

        return """
                ⚽️ Создание игры
                
                ⚽️ %s ⚽️
                
                📍 Место: %s
                📅 Дата: %s
                🕒 Время: %s
                ⚽️ Количество участников: %s
                🏆 Уровень игры: %s
                """.formatted(
                context.getTitle(),
                context.getLocation(),
                context.getGameDate(),
                context.getGameTime(),
                context.getTotalSlots(),
                context.getLevel()
        );
    }
}
