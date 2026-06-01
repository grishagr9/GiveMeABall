package grisha.tech.bot.dsispatchers.commands;

import grisha.tech.bot.data.enums.UserState;
import grisha.tech.bot.services.InviteService;
import grisha.tech.bot.services.keyBoards.KeyBoardService;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.UserService;
import grisha.tech.bot.services.UserStateService;
import grisha.tech.bot.services.keyBoards.KeyboardRegistrationFactory;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    private final UserService userService;
    private final SenderMessageService telegramSender;
    private final UserStateService userStateService;
    private final MessageUtils utils;

    private final InviteService inviteService;

    private final KeyBoardService keyBoardService;
    private final KeyboardRegistrationFactory keyboardRegistrationFactory;

    @Override
    public Boolean canHandle(Update update) {
        return update.getMessage().hasText() && update.getMessage().getText().startsWith("/start");
    }

    @Override
    public void handle(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        String username = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        boolean exists = userService.existsByTelegramId(telegramId);

        if (text.startsWith("/start join_")) {
            String inviteCode =
                    text.replace("/start join_", "");

            if (!exists) {
                userService.createInitialUser(
                        telegramId,
                        username,
                        firstName,
                        chatId
                );
            }

            inviteService.processInvite(
                    telegramId,
                    inviteCode
            );

            return;
        }

        if (exists) {
            telegramSender.sendMessage(
                    chatId,
                    utils.getMessage("bot.start.return"),
                    keyBoardService.mainMenuKeyboard()
            );

            return;
        }

        userService.createInitialUser(
                telegramId,
                username,
                firstName,
                chatId
        );

        userStateService.setState(
                telegramId,
                UserState.WAITING_FOR_POSITION
        );

        telegramSender.sendMessage(
                chatId,
                utils.getMessage("bot.start.welcome")
        );
        telegramSender.sendMessage(
                chatId,
                utils.getMessage("registration.position"),
                keyboardRegistrationFactory.positionKeyBoard()
        );
    }
}