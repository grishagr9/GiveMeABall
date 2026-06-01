package grisha.tech.bot.services.listeners;

import grisha.tech.bot.data.dto.NotificationContext;
import grisha.tech.bot.data.dto.events.GameReminder1hEvent;
import grisha.tech.bot.data.dto.events.GameReminder24hEvent;
import grisha.tech.bot.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
@RequiredArgsConstructor
public class ReminderEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handle24hReminder(GameReminder24hEvent event) {
        String text = String.format(
                "⏳ <b>Напоминание: завтра игра!</b>\nМатч: %s\nЕсли ваши планы изменились, пожалуйста, освободите место для других.",
                event.gameTitle()
        );

        // Создаем кнопку выхода
        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(
                        InlineKeyboardButton.builder()
                                .text("❌ Покинуть игру")
                                .callbackData("exit_game_" + event.gameId())
                                .build()
                ))
                .build();

        for (Long telegramId : event.participantIds()) {
            notificationService.sendNotification(new NotificationContext(telegramId, text, keyboard));
        }
    }

    @EventListener
    public void handle1hReminder(GameReminder1hEvent event) {
        String text = String.format(
                "⚽️ <b>Игра уже через час!</b>\nМатч: %s\nЖдем вас на поле!",
                event.gameTitle()
        );

        for (Long telegramId : event.participantIds()) {
            notificationService.sendNotification(new NotificationContext(telegramId, text, null));
        }
    }
}