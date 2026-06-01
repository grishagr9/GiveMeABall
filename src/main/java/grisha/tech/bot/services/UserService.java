package grisha.tech.bot.services;

import grisha.tech.bot.model.entities.UserEntity;
import grisha.tech.bot.model.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean existsByTelegramId(Long telegramId) {
        return userRepository.existsByTelegramId(telegramId);
    }

    public void createInitialUser(
            Long telegramId,
            String username,
            String firstName,
            Long chatId) {

        UserEntity user = new UserEntity();

        user.setTelegramId(telegramId);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setChatId(chatId);

        userRepository.save(user);
    }

    public UserEntity findByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    public void updatePosition(Long telegramId, String position) {

        UserEntity user = findByTelegramId(telegramId);

        user.setPosition(position);

        userRepository.save(user);
    }

    public void updateLevel(Long telegramId, Integer level) {
        UserEntity user = findByTelegramId(telegramId);

        user.setSkillLevel(level);

        userRepository.save(user);
    }
}
