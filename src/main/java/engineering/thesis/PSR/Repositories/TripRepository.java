package engineering.thesis.PSR.Repositories;

import engineering.thesis.PSR.Entities.TripEntity;
import engineering.thesis.PSR.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<TripEntity,Long> {
    List<TripEntity> findTripEntitiesByUserId(Long userId);
}
