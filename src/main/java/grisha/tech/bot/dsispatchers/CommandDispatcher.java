package grisha.tech.bot.dsispatchers;

import grisha.tech.bot.dsispatchers.commands.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CommandDispatcher {

    private final Collection<Command> commands;

    public boolean dispatch(Update update) {
        for (Command command : commands) {
            if (command.canHandle(update)) {
                command.handle(update);
                return true;
            }
        }

        return false;
    }
}
