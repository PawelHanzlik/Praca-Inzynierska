package engineering.thesis.PSR.Repositories;

import engineering.thesis.PSR.Entities.TripEntity;
import engineering.thesis.PSR.Entities.UserHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserHistoryRepository  extends JpaRepository<UserHistoryEntity,Long> {
    List<UserHistoryEntity> findUserHistoryEntitiesByUserId(Long userId);
}
