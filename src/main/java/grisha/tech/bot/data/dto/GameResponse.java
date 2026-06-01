package grisha.tech.bot.data.dto;

import java.time.LocalDateTime;

public record GameResponse(
        Long id,
        String title,
        String location,
        LocalDateTime gameTime,
        Integer freeSlots,
        Integer level,
        String status,
        String link,
        String organizerUsername
) {
}