package grisha.tech.bot.model.repo;

import grisha.tech.bot.data.enums.GameStatus;
import grisha.tech.bot.model.entities.GameEntity;
import grisha.tech.bot.model.entities.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {

    // Ищем сразу в БД с подгрузкой организатора, чтобы избежать N+1
    @EntityGraph(attributePaths = {"organizer"})
    List<GameEntity> findByStatusAndLocationContainingIgnoreCase(GameStatus status, String location);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // Блокирует строку в БД до конца транзакции
    @Query("SELECT g FROM GameEntity g WHERE g.id = :id")
    Optional<GameEntity> findByIdWithLock(@Param("id") Long id);

    @EntityGraph(attributePaths = {"organizer"})
    List<GameEntity> findByStatusAndOrganizerTelegramIdNot(GameStatus status, Long telegramId);

    @EntityGraph(attributePaths = {"organizer"})
    List<GameEntity> findByOrganizer(UserEntity organizer);

    Optional<GameEntity> findByGameLink(String gameLink);

    // Поиск для уведомления за 24 часа
    List<GameEntity> findByGameTimeBetweenAndNotified24hFalse(LocalDateTime start, LocalDateTime end);

    // Поиск для уведомления за 1 час
    List<GameEntity> findByGameTimeBetweenAndNotified1hFalse(LocalDateTime start, LocalDateTime end);

    @Modifying
    @Transactional
    @Query("UPDATE GameEntity g SET g.status = 'FINISHED' " +
            "WHERE g.gameTime < :now AND g.status IN (grisha.tech.bot.data.enums.GameStatus.OPEN, grisha.tech.bot.data.enums.GameStatus.FULL)")
    int closePastGames(@Param("now") LocalDateTime now);
}