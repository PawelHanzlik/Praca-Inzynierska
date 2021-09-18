package engineering.thesis.PSR.Repositories;

import engineering.thesis.PSR.Entities.ParkingLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity,Long> {

}