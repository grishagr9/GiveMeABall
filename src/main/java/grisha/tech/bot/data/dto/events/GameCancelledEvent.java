package grisha.tech.bot.data.dto.events;

import java.util.List;

public record GameCancelledEvent(
        String gameTitle,
        List<Long> participantTelegramIds
) {}