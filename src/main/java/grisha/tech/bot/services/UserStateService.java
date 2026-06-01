package grisha.tech.bot.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import grisha.tech.bot.data.dto.CreateGameContext;
import grisha.tech.bot.data.enums.UserState;
import grisha.tech.bot.model.entities.UserStateEntity;
import grisha.tech.bot.model.repo.UserStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStateService {

    private final UserStateRepository userStateRepository;
    private final ObjectMapper objectMapper;

    public UserState getState(Long telegramId) {

        return userStateRepository.findById(telegramId)
                .map(UserStateEntity::getState)
                .orElse(UserState.NONE);
    }

    public void setState(
            Long telegramId,
            UserState state
    ) {

        UserStateEntity entity = userStateRepository.findById(telegramId)
                .orElse(new UserStateEntity());

        entity.setTelegramId(telegramId);
        entity.setState(state);

        userStateRepository.save(entity);
    }

    public void saveContext(
            Long telegramId,
            CreateGameContext context
    ) {
        UserStateEntity entity = userStateRepository.findById(telegramId).orElseThrow();

        try {
            entity.setPayload(
                    objectMapper.writeValueAsString(context)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        userStateRepository.save(entity);
    }

    public void clearContext(
            Long telegramId
    ) {
        UserStateEntity entity = userStateRepository.findById(telegramId).orElseThrow();

        try {
            entity.setPayload(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        userStateRepository.save(entity);
    }

    public CreateGameContext getContext(Long telegramId) {
        UserStateEntity entity =
                userStateRepository.findById(telegramId)
                        .orElseThrow();
        try {
            return objectMapper.readValue(
                    entity.getPayload(),
                    CreateGameContext.class
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
