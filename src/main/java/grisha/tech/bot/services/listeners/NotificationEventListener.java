package grisha.tech.bot.services.listeners;

import grisha.tech.bot.data.dto.NotificationContext;
import grisha.tech.bot.data.dto.events.GameCancelledEvent;
import grisha.tech.bot.data.dto.events.GameExitEvent;
import grisha.tech.bot.data.dto.events.GameJoinEvent;
import grisha.tech.bot.services.NotificationService;
import grisha.tech.bot.services.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final MessageUtils utils;

    @EventListener
    public void handleGameCancelled(GameCancelledEvent event) {
        String message = utils.getMessage("deleteGame.notification", event.gameTitle());

        notificationService.sendMassNotification(
                event.participantTelegramIds(),
                message
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGameJoined(GameJoinEvent event) {
        String message = utils.getMessage("joinGame.notification",
                event.gameName(), event.userName());

        notificationService.sendNotification(
                new NotificationContext(event.chatId(), message, null)
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGameExit(GameExitEvent event) {
        String message = utils.getMessage("exitGame.notification",
                event.gameName(), event.userName());

        notificationService.sendNotification(
                new NotificationContext(event.chatId(), message, null)
        );
    }

}