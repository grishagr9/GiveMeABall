package grisha.tech.bot.model.repo;

import grisha.tech.bot.model.entities.UserStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStateRepository
        extends JpaRepository<UserStateEntity, Long> {
}