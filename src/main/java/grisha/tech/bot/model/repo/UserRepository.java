package grisha.tech.bot.model.repo;

import grisha.tech.bot.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByTelegramId(Long telegramId);

    Optional<UserEntity> findByTelegramId(Long telegramId);
}