package grisha.tech.bot.model.repo;

import grisha.tech.bot.model.entities.GameParticipantEntity;
import grisha.tech.bot.model.entities.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameParticipantRepository extends JpaRepository<GameParticipantEntity, Long> {

    int countByGameId(Long gameId);

    boolean existsByGameIdAndUserId(Long gameId, Long userId);

    @EntityGraph(attributePaths = {"game", "game.organizer"})
    List<GameParticipantEntity> findByUser(UserEntity user);

    @EntityGraph(attributePaths = {"game", "game.organizer"})
    List<GameParticipantEntity> findByGameId(Long gameId);

    @EntityGraph(attributePaths = {"user"})
    List<GameParticipantEntity> findUsersByGameId(Long gameId);

    @Modifying
    @Query("UPDATE GameParticipantEntity p SET p.ball = true WHERE p.game.id = :gameId AND p.user.telegramId = :telegramId")
    void addBall(@Param("gameId") Long gameId, @Param("telegramId") Long telegramId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM GameParticipantEntity g WHERE g.game.id = :id")
    void deleteByGameId(@Param("id") Long id);

    @Modifying
    @Query("DELETE FROM GameParticipantEntity g WHERE g.game.id = :gameId AND g.user.telegramId = :telegramId")
    int exitFromGame(@Param("gameId") Long gameId, @Param("telegramId") Long telegramId);

    @Query("SELECT p.user.telegramId FROM GameParticipantEntity p WHERE p.game.id = :gameId")
    List<Long> findAllTelegramIdsByGameId(@Param("gameId") Long gameId);

}