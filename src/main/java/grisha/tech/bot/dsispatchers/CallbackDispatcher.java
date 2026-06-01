package grisha.tech.bot.dsispatchers;

import grisha.tech.bot.dsispatchers.callbacks.CallbackHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CallbackDispatcher {

    private final List<CallbackHandler> handlers;

    public void dispatch(CallbackQuery callback) {
        String data = callback.getData();

        handlers.stream()
                .filter(h -> h.supports(data))
                .findFirst()
                .ifPresent(h -> h.handle(callback));
    }
}
