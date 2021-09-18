package engineering.thesis.PSR.Exceptions.Classes;

import engineering.thesis.PSR.Exceptions.Messages.ExceptionMessages;

import java.io.Serial;

public class NoSuchParkingLotException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 2645315523262426567L;

    public NoSuchParkingLotException() {
        super(ExceptionMessages.NO_SUCH_PARKING_LOT.getErrorMessage());
    }
}
