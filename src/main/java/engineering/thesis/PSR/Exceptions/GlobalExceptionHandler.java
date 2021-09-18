package engineering.thesis.PSR.Exceptions;

import engineering.thesis.PSR.Exceptions.Classes.NoSuchParkingLotException;
import engineering.thesis.PSR.Exceptions.Classes.NoSuchUserException;
import engineering.thesis.PSR.Exceptions.Classes.NoSuchZoneException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Date;

@EnableWebMvc
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String INVALID_REQUEST = "Invalid request";

    @ExceptionHandler(value = {NoSuchUserException.class})
    public ResponseEntity<Object> handleNoSuchUserException() {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), INVALID_REQUEST);
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NoSuchZoneException.class})
    public ResponseEntity<Object> handleNoSuchZoneException() {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), INVALID_REQUEST);
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NoSuchParkingLotException.class})
    public ResponseEntity<Object> handleNoSuchParkingLotException() {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), INVALID_REQUEST);
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}