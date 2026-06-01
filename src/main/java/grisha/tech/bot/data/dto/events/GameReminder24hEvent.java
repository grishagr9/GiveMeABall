package grisha.tech.bot.data.dto.events;

import java.util.List;

// За 24 часа (нужна кнопка выхода)
public record GameReminder24hEvent(
        Long gameId,
        String gameTitle,
        List<Long> participantIds
) {}
