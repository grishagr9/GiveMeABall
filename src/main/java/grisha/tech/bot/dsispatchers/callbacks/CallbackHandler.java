package grisha.tech.bot.dsispatchers.callbacks;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackHandler {
    boolean supports(String data);

    void handle(CallbackQuery callback);
}
