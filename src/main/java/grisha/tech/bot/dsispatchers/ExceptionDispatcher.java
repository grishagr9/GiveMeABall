package grisha.tech.bot.dsispatchers;

import grisha.tech.bot.exceptions.JoinGameError;
import grisha.tech.bot.services.SenderMessageService;
import grisha.tech.bot.services.keyBoards.KeyBoardService;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionDispatcher {

    private final SenderMessageService sender;
    private final MessageUtils utils;
    private final KeyBoardService keyboard;

    public void handle(Exception exception, Long chatId) {
        if (exception instanceof JoinGameError joinGameError) {
            sender.sendMessage(
                    chatId,
                    utils.getMessage("error.handle",
                            joinGameError.getErrorType().getDescription(),
                            joinGameError.getErrorType().getAction()),
                    keyboard.mainMenuKeyboard()
            );
        } else {
            sender.sendMessage(
                    chatId,
                    utils.getMessage("error.unknown"),
                    keyboard.mainMenuKeyboard()
            );
        }
    }

}
