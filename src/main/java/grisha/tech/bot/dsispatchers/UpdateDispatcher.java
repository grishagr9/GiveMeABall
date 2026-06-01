package grisha.tech.bot.dsispatchers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final CommandDispatcher commandDispatcher;
    private final StateDispatcher stateDispatcher;
    private final CallbackDispatcher callbackDispatcher;
    private final ExceptionDispatcher exceptionDispatcher;

    public void dispatch(Update update) {
        try {
            if (update.hasCallbackQuery()) {
                callbackDispatcher.dispatch(update.getCallbackQuery());
            }

            if (!update.hasMessage()) {
                return;
            }

            boolean isCommand = commandDispatcher.dispatch(update);
            if (!isCommand) {
                stateDispatcher.dispatch(update);
            }
        } catch (Exception exception) {
            if (update.hasCallbackQuery()) {
                log.error("Error while processing message {}: {}",
                        update.getCallbackQuery().getData(),
                        exception.getMessage(),
                        exception
                );

                exceptionDispatcher.handle(exception,
                        update.getCallbackQuery().getMessage().getChatId());
            } else {
                log.error("Error while processing message {}: {}",
                        update.getMessage().getText(),
                        exception.getMessage(),
                        exception
                );

                exceptionDispatcher.handle(exception,
                        update.getMessage().getChatId());
            }
        }
    }

}
