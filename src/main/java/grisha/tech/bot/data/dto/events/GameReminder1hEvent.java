package grisha.tech.bot.data.dto.events;

import java.util.List;

// За 1 час (просто напоминание)
public record GameReminder1hEvent(
        String gameTitle,
        List<Long> participantIds
) {}