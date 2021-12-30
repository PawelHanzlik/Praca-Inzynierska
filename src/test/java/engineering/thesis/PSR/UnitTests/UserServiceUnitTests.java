package engineering.thesis.PSR.UnitTests;

import engineering.thesis.PSR.Entities.UserEntity;
import engineering.thesis.PSR.Exceptions.Classes.NoSuchUserException;
import engineering.thesis.PSR.Repositories.UserRepository;
import engineering.thesis.PSR.Repositories.ZoneRepository;
import engineering.thesis.PSR.Services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static engineering.thesis.PSR.DataProviders.UserServiceDataProvider.*;
import static engineering.thesis.PSR.DataProviders.ZoneServiceDataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
public class UserServiceUnitTests {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    ZoneRepository zoneRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserTest(){
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.ofNullable(usersEntity));
        UserEntity getUserEntity = this.userService.getUser(userId);
        assertNotNull(getUserEntity);
        assertEquals(1, getUserEntity.getUserId());
        assertEquals("test_name",getUserEntity.getName());
        assertEquals("test_surname",getUserEntity.getSurname());
        assertEquals(40,getUserEntity.getAge());
        assertEquals(1L,getUserEntity.getPreferableZone());
    }

    @Test
    void getUserNoSuchUserExceptionTest(){
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> userService.getUser(userId));
    }

    @Test
    void getAllUsers(){
        when(userRepository.findAll()).thenReturn(usersEntities);
        List<UserEntity> usersEntityList = this.userService.getAllUsers();
        assertEquals(usersEntity,usersEntityList.get(0));
        assertEquals(usersEntity1,usersEntityList.get(1));
    }
    @Test
    void addUserTest(){
        when(userRepository.save(any(UserEntity.class))).thenReturn(usersEntity1);
        UserEntity newUserEntity = this.userService.addUser(usersEntity1);
        assertNotNull(newUserEntity);
        assertEquals(1, newUserEntity.getUserId());
        assertEquals("new_name",newUserEntity.getName());
        assertEquals("new_surname",newUserEntity.getSurname());
        assertEquals(20,newUserEntity.getAge());
        assertEquals(2L,newUserEntity.getPreferableZone());
    }

    @Test
    void deleteUserTest() {
        Optional<UserEntity> optionalUserEntity = Optional.of(usersEntity);

        when(userRepository.findById(userId)).thenReturn(optionalUserEntity);

        userService.deleteUser(userId);

        Mockito.verify(userRepository, times(1)).delete(optionalUserEntity.get());
    }

    @Test
    void deleteUserNoSuchUserExceptionTest(){
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> userService.deleteUser(userId));
    }


    @Test
    void  changeUserPreferableZoneTest(){
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.ofNullable(usersEntity2));
        when(zoneRepository.findById(2L)).thenReturn(java.util.Optional.of(zonesEntity2));
        when(userRepository.save(any(UserEntity.class))).thenReturn(usersEntity1);
        this.userService.changeUserPreferableZone(1L,2L);
        assertEquals(2L,usersEntity2.getPreferableZone());
    }

}
