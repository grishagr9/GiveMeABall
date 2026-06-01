package grisha.tech.bot.data.dto;

import java.time.LocalDateTime;

public record CreateGameRequest(
        String title,
        String location,
        LocalDateTime gameTime,
        Integer totalSlots,
        Integer level,
        Long organizerTelegramId
) {
}