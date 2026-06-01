package grisha.tech.bot.services;

import grisha.tech.bot.model.repo.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameCleanupScheduler {

    private final GameRepository gameRepository;

    /**
     * Запускается каждые 15 минут.
     * cron: "0 0/15 * * * *" (каждые 15 минут в 00 сек)
     */
    @Scheduled(cron = "0 0/15 * * * *")
    public void cleanupFinishedGames() {
        log.info("Starting cleanup of finished games...");

        LocalDateTime now = LocalDateTime.now();
        int updatedCount = gameRepository.closePastGames(now);

        if (updatedCount > 0) {
            log.info("Cleanup finished. {} games were marked as FINISHED.", updatedCount);
        }
    }
}