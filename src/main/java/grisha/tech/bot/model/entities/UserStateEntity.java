package grisha.tech.bot.model.entities;

import grisha.tech.bot.data.enums.UserState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_states")
@Getter
@Setter
public class UserStateEntity {

    @Id
    private Long telegramId;

    @Enumerated(EnumType.STRING)
    private UserState state;

    @Column(columnDefinition = "TEXT")
    private String payload;
}