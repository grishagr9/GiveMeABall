package grisha.tech.bot.data.dto.events;

public record GameJoinEvent(
        String gameName,
        long chatId,
        String userName
) {
}
