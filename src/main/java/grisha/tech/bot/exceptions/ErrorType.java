package grisha.tech.bot.exceptions;

import lombok.Getter;

@Getter
public enum ErrorType {

    ALREADY_JOINED("Ты уже участвуешь в этой игре.",
            "Просмотри свои игры в главном меню"),
    GAME_FULL("В этой игре больше нет свободных мест.",
            "Обнови список доступных игр"),
    GAME_NOT_FOUND("Игра не найдена.",
            "Обнови список доступных игр"),
    ORGANIZER_CANNOT_EXIT("Организатор не может выйти из своей игры",
            "Чтобы удалить игру зайди в меню Управление играми"),
    NOT_A_PARTICIPANT("Вы не являлись участником матча",
            "Выходить можно только из своих матчей")
    ;

    private final String description;
    private final String action;

    ErrorType(String description, String action) {
        this.description = description;
        this.action = action;
    }
}
