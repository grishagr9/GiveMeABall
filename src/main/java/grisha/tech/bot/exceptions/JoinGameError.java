package grisha.tech.bot.exceptions;

import lombok.Getter;

@Getter
public class JoinGameError extends RuntimeException {

    private final ErrorType errorType;

    public JoinGameError(ErrorType errorType) {
        this.errorType = errorType;
    }

    public JoinGameError(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
}
