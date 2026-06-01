package grisha.tech.bot.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_participants")
@Setter
@Getter
public class GameParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private GameEntity game;

    @ManyToOne
    private UserEntity user;

    private Boolean ball;

    private LocalDateTime joinedAt;
}