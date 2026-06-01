package grisha.tech.bot.data.dto.events;

public record GameExitEvent(
        String gameName,
        long chatId,
        String userName
) {
}
