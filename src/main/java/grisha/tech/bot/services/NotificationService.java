package grisha.tech.bot.services;

import grisha.tech.bot.data.dto.NotificationContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SenderMessageService sender;

    /**
     * Универсальный метод отправки
     */
    public void sendNotification(NotificationContext context) {
        sender.sendMessage(context.chatId(),
                context.message(),
                context.keyboard()
        );
    }

    /**
     * Массовая рассылка (пакетная)
     */
    @Async // Чтобы не блокировать основной поток
    public void sendMassNotification(List<Long> ids, String text) {
        for (Long id : ids) {
            sendNotification(new NotificationContext(id, text, null));
        }
    }
}