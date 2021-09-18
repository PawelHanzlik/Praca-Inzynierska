package engineering.thesis.PSR.Services;

import engineering.thesis.PSR.Entities.UserEntity;
import engineering.thesis.PSR.Exceptions.Classes.NoSuchUserException;
import engineering.thesis.PSR.Exceptions.Classes.NoSuchZoneException;

import java.util.List;

public interface UserService {

    UserEntity getUser(Long userId) throws NoSuchUserException;
    List<UserEntity> getAllUsers();
    UserEntity addUser(UserEntity user);
    void deleteUser(Long userId) throws NoSuchUserException;
    void changeUserCarSize(Long userId, Integer newCarSize) throws NoSuchUserException;
    void changeUserPreferableZone(Long userId, Long zoneId) throws NoSuchUserException, NoSuchZoneException;
}
