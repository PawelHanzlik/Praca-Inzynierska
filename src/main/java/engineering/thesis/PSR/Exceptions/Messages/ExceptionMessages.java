package engineering.thesis.PSR.Exceptions.Messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ExceptionMessages {

    NO_SUCH_USER("No such user"),
    NO_SUCH_PARKING_LOT("No such parking lot"),
    NO_SUCH_ZONE("No such zone");
    @Getter
    private final String errorMessage;
}
