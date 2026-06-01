package grisha.tech.bot.mappers;

import grisha.tech.bot.data.dto.CreateGameContext;
import grisha.tech.bot.data.dto.GameResponse;
import grisha.tech.bot.model.entities.GameEntity;
import grisha.tech.bot.model.entities.UserEntity;
import grisha.tech.bot.model.repo.GameParticipantRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Mapper(componentModel = "spring",
        imports = {LocalDateTime.class, java.util.UUID.class})
@Component
public abstract class GameMapper {

    @Autowired
    protected GameParticipantRepository participantRepository;

    @Value("${telegram.bot.link}")
    protected String linkToBot;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, d MMMM, HH:mm", new Locale("ru"));

    public String formatDate(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DATE_FORMATTER);
    }

    @Mapping(target = "freeSlots", expression = "java(getAvailableSlots(game))")
    @Mapping(target = "status", expression = "java(game.getStatus().name())")
    @Mapping(target = "link", expression = "java(linkToBot + \"join_\" + game.getGameLink())")
    @Mapping(target = "organizerUsername", source = "username")
    @Mapping(target = "id", source = "game.id")
    @Mapping(target = "title", source = "game.title")
    @Mapping(target = "location", source = "game.location")
    @Mapping(target = "gameTime", source = "game.gameTime")
    @Mapping(target = "level", source = "game.level")
    public abstract GameResponse toResponse(GameEntity game, String username);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gameTime", expression = "java(request.getGameTime().atDate(request.getGameDate()))")
    @Mapping(target = "status", constant = "OPEN")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "gameLink", expression = "java(UUID.randomUUID().toString().substring(0, 8))")
    @Mapping(target = "organizer", source = "organizer")
    public abstract GameEntity toEntity(CreateGameContext request, UserEntity organizer);


    protected Integer getAvailableSlots(GameEntity game) {
        int occupied = participantRepository.countByGameId(game.getId());
        return game.getTotalSlots() - occupied;
    }
}
