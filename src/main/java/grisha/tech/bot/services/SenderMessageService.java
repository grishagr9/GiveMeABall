package grisha.tech.bot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class SenderMessageService {

    private final TelegramClient telegramClient;

    public boolean sendMessage(Long chatId,
                               String text,
                               ReplyKeyboard keyboard) {
        try {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(keyboard)
                    .parseMode("HTML")
                    .build();

            telegramClient.execute(message);
            return true;
        } catch (TelegramApiException ex) {
            log.error("Не удалось отправить клавиатуру {}", ex.getMessage());
            return false;
        }
    }

    public boolean sendMessage(Long chatId, String text) {
        try {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .parseMode("HTML")
                    .build();

            telegramClient.execute(message);
            return true;
        } catch (TelegramApiException ex) {
            log.error("Не удалось отправить cообщение {}", ex.getMessage());
            return false;
        }
    }

    public void answer(String text, String id) {
        try {
            AnswerCallbackQuery answer = AnswerCallbackQuery.builder()
                    .callbackQueryId(id)
                    .text(text)
                    .build();
            telegramClient.execute(answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
