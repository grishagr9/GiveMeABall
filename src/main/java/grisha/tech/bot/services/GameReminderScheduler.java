package grisha.tech.bot.services;

import grisha.tech.bot.data.dto.events.GameReminder1hEvent;
import grisha.tech.bot.data.dto.events.GameReminder24hEvent;
import grisha.tech.bot.model.entities.GameEntity;
import grisha.tech.bot.model.repo.GameParticipantRepository;
import grisha.tech.bot.model.repo.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameReminderScheduler {

    private final GameRepository gameRepository;
    private final GameParticipantRepository participantRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedRate = 60000) // Проверка каждую минуту
    @Transactional
    public void checkReminders() {
        LocalDateTime now = LocalDateTime.now();

        // 1. Проверка уведомлений за 24 часа (окно от 23:50 до 24:10 часов до начала)
        List<GameEntity> upcoming24h = gameRepository.findByGameTimeBetweenAndNotified24hFalse(
                now.plusHours(23).plusMinutes(50),
                now.plusHours(24).plusMinutes(10)
        );

        for (GameEntity game : upcoming24h) {
            List<Long> ids = participantRepository.findAllTelegramIdsByGameId(game.getId());
            eventPublisher.publishEvent(new GameReminder24hEvent(game.getId(), game.getTitle(), ids));
            game.setNotified24h(true); // Помечаем, чтобы не слать повторно
        }

        // 2. Проверка уведомлений за 1 час (окно от 50 до 70 минут до начала)
        List<GameEntity> upcoming1h = gameRepository.findByGameTimeBetweenAndNotified1hFalse(
                now.plusMinutes(50),
                now.plusMinutes(70)
        );

        for (GameEntity game : upcoming1h) {
            List<Long> ids = participantRepository.findAllTelegramIdsByGameId(game.getId());
            eventPublisher.publishEvent(new GameReminder1hEvent(game.getTitle(), ids));
            game.setNotified1h(true);
        }

        gameRepository.saveAll(upcoming24h);
        gameRepository.saveAll(upcoming1h);
    }
}