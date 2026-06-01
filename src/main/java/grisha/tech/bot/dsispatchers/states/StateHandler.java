package grisha.tech.bot.dsispatchers.states;

import grisha.tech.bot.data.enums.UserState;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface StateHandler {

    UserState state();

    void handle(Update update);

}
