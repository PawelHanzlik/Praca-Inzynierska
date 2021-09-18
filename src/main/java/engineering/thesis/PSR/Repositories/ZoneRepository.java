package engineering.thesis.PSR.Repositories;

import engineering.thesis.PSR.Entities.ZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<ZoneEntity, Long> {

}
