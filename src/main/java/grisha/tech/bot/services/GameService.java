package grisha.tech.bot.services;

import grisha.tech.bot.data.dto.CreateGameContext;
import grisha.tech.bot.data.dto.GameResponse;
import grisha.tech.bot.data.dto.Player;
import grisha.tech.bot.data.dto.events.GameCancelledEvent;
import grisha.tech.bot.data.dto.events.GameExitEvent;
import grisha.tech.bot.data.dto.events.GameJoinEvent;
import grisha.tech.bot.data.enums.GameStatus;
import grisha.tech.bot.exceptions.EntityNotFoundException;
import grisha.tech.bot.exceptions.ErrorType;
import grisha.tech.bot.exceptions.JoinGameError;
import grisha.tech.bot.mappers.GameMapper;
import grisha.tech.bot.model.entities.GameEntity;
import grisha.tech.bot.model.entities.GameParticipantEntity;
import grisha.tech.bot.model.entities.UserEntity;
import grisha.tech.bot.model.repo.GameParticipantRepository;
import grisha.tech.bot.model.repo.GameRepository;
import grisha.tech.bot.model.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameParticipantRepository participantRepository;
    private final GameMapper mapper;
    private final ApplicationEventPublisher eventPublisher; // Стандартный Spring бин

    @Transactional
    public GameEntity create(CreateGameContext request, Long telegramId) {
        UserEntity organizer = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + telegramId));

        GameEntity game = mapper.toEntity(request, organizer);
        game = gameRepository.save(game);

        joinGameInternal(game, organizer);
        return game;
    }

    @Transactional(readOnly = true)
    public List<GameResponse> findOpened(Long telegramId) {
        return gameRepository.findByStatusAndOrganizerTelegramIdNot(GameStatus.OPEN, telegramId)
                .stream()
                .map(this::mapToResponse)
                .filter(game -> game.freeSlots() > 0)
                .toList();
    }

    @Transactional
    public void joinGame(Long gameId, Long telegramId) {
        GameEntity game = gameRepository.findByIdWithLock(gameId)
                .orElseThrow(() -> new JoinGameError(ErrorType.GAME_NOT_FOUND));
        UserEntity user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (participantRepository.existsByGameIdAndUserId(gameId, user.getId())) {
            throw new JoinGameError(ErrorType.ALREADY_JOINED);
        }

        int currentParticipants = participantRepository.countByGameId(gameId);

        if (currentParticipants >= game.getTotalSlots()) {
            throw new JoinGameError(ErrorType.GAME_FULL);
        }

        joinGameInternal(game, user);

        if (currentParticipants + 1 >= game.getTotalSlots()) {
            game.setStatus(GameStatus.FULL);
        }

        eventPublisher.publishEvent(new GameJoinEvent(
                game.getTitle(),
                game.getOrganizer().getChatId(),
                user.getUsername()
        ));
    }

    @Transactional
    public void addBall(Long gameId, Long telegramId) {
        participantRepository.addBall(gameId, telegramId);
    }

    @Transactional
    public void deleteGame(Long gameId) {
        // 1. Сначала загружаем игру, чтобы получить заголовок и ID организатора
        GameEntity game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        // 2. Получаем ID участников для уведомлений ДО удаления
        List<Long> idsToNotify = participantRepository.findAllTelegramIdsByGameId(gameId);

        // Исключаем организатора
        Long organizerId = game.getOrganizer().getTelegramId();
        idsToNotify.remove(organizerId);

        // 3. Сначала удаляем участников через Bulk Delete
        participantRepository.deleteByGameId(gameId);

        // 4. Удаляем саму игру
        // Используем delete(game), так как объект уже загружен
        gameRepository.delete(game);

        // 5. Отправляем событие
        if (!idsToNotify.isEmpty()) {
            eventPublisher.publishEvent(new GameCancelledEvent(game.getTitle(), idsToNotify));
        }
    }

    @Transactional
    public void exitGame(Long gameId, Long telegramId) {
        // 1. Блокируем игру, чтобы конкурентно обновить статус
        GameEntity game = gameRepository.findByIdWithLock(gameId)
                .orElseThrow(() -> new JoinGameError(ErrorType.GAME_NOT_FOUND));

        // Запрещаем организатору выходить из своей игры через этот метод
        if (game.getOrganizer().getTelegramId().equals(telegramId)) {
            throw new JoinGameError(ErrorType.ORGANIZER_CANNOT_EXIT); // Создайте такой тип ошибки
        }

        UserEntity user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 2. Пытаемся удалить запись и проверяем, удалилась ли она
        int deletedRows = participantRepository.exitFromGame(gameId, telegramId);

        if (deletedRows == 0) {
            throw new JoinGameError(ErrorType.NOT_A_PARTICIPANT);
        }

        // 3. Если игра была FULL, теперь она точно OPEN, так как место освободилось
        if (game.getStatus() == GameStatus.FULL) {
            game.setStatus(GameStatus.OPEN);
            gameRepository.save(game);
        }

        // 4. Публикуем событие
        eventPublisher.publishEvent(new GameExitEvent(
                game.getTitle(),
                game.getOrganizer().getChatId(),
                user.getUsername()
        ));
    }

    @Transactional(readOnly = true)
    public List<Player> getPlayers(Long gameId) {
        return participantRepository.findUsersByGameId(gameId)
                .stream()
                .filter(Objects::nonNull)
                .map(p -> new Player(p.getUser().getUsername(),  p.getBall()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GameResponse> getMyGame(Long telegramId) {
        UserEntity user = userRepository.findByTelegramId(telegramId).orElseThrow();

        return participantRepository.findByUser(user)
                .stream()
                .filter(game -> Set.of(GameStatus.OPEN, GameStatus.FULL).contains(game.getGame().getStatus()))
                .map(participant -> mapToResponse(participant.getGame()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GameResponse> getGamesCreated(Long telegramId) {
        UserEntity user = userRepository.findByTelegramId(telegramId).orElseThrow();

        return gameRepository.findByOrganizer(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void joinGameInternal(GameEntity game, UserEntity user) {
        GameParticipantEntity participant = new GameParticipantEntity();
        participant.setGame(game);
        participant.setUser(user);
        participant.setBall(false);
        participant.setJoinedAt(LocalDateTime.now());
        participantRepository.save(participant);
    }

    private GameResponse mapToResponse(GameEntity game) {
        return mapper.toResponse(game, game.getOrganizer().getUsername());
    }
}