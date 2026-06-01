package grisha.tech.bot.data.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateGameContext {

    private String title;

    private String location;

    private LocalDate gameDate;

    private LocalTime gameTime;

    private Integer totalSlots;

    private Integer level;
}