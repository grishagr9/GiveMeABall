package grisha.tech.bot.dsispatchers;

import grisha.tech.bot.data.enums.UserState;
import grisha.tech.bot.dsispatchers.states.StateHandler;
import grisha.tech.bot.services.UserStateService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StateDispatcher {

    private final UserStateService stateService;
    private final Map<UserState, StateHandler> handlers = new HashMap<>();

    public StateDispatcher(List<StateHandler> handlerList,
                           UserStateService stateService) {
        this.stateService = stateService;

        for (StateHandler h : handlerList) {
            handlers.put(h.state(), h);
        }
    }

    public void dispatch(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        UserState status = stateService.getState(telegramId);

        if (status == UserState.NONE) {
            return;
        }
        StateHandler stateHandler = handlers.get(status);

        if (stateHandler != null) {
            stateHandler.handle(update);
        }
    }

    public void dispatch(Update update, UserState status) {
        StateHandler stateHandler = handlers.get(status);

        if (stateHandler != null) {
            stateHandler.handle(update);
        }
    }

}
