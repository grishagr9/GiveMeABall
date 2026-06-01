package grisha.tech.bot.dsispatchers.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

    Boolean canHandle(Update update);

    void handle(Update update);
}
