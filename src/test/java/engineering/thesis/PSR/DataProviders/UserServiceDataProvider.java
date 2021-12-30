package engineering.thesis.PSR.DataProviders;

import engineering.thesis.PSR.Entities.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserServiceDataProvider {

    public static final Long userId = (long)1;
    public static UserEntity usersEntity;
    public static UserEntity usersEntity1;
    public static UserEntity usersEntity2;
    public static List<UserEntity> usersEntities;
    static {
        usersEntity = UserEntity.builder().userId(userId).name("test_name").surname("test_surname").age(40).preferableZone(1L).build();
        usersEntity1 = UserEntity.builder().userId(userId).name("new_name").surname("new_surname").age(20).preferableZone(2L).build();
        usersEntity2 = UserEntity.builder().userId(userId).name("test_name").surname("test_surname").age(30).preferableZone(1L).build();
        usersEntities = new ArrayList<>();
        usersEntities.add(usersEntity);
        usersEntities.add(usersEntity1);
    }
}
