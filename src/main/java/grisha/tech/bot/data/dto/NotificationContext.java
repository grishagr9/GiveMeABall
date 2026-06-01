package grisha.tech.bot.data.dto;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public record NotificationContext(
        Long chatId,
        String message,
        ReplyKeyboard keyboard // Опционально, если нужны кнопки
) {}