package grisha.tech.bot.model.entities;

import grisha.tech.bot.data.enums.GameStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "games")
@Setter
@Getter
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String location; // район / адрес

    private LocalDateTime gameTime;

    private Integer totalSlots;

    private Integer level; // 1-5 (новички → профи)

    @ManyToOne
    private UserEntity organizer;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    private String gameLink;

    private LocalDateTime createdAt;

    @Column(name = "notified_24h")
    private boolean notified24h = false;

    @Column(name = "notified_1h")
    private boolean notified1h = false;
}