package engineering.thesis.PSR.Services;

import engineering.thesis.PSR.Entities.TripEntity;
import engineering.thesis.PSR.Entities.UserEntity;
import engineering.thesis.PSR.Repositories.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripServiceImpl implements TripService{
    private final TripRepository tripRepository;
    private final UserService userService;

    public TripServiceImpl(TripRepository tripRepository, UserService userService) {
        this.tripRepository = tripRepository;
        this.userService = userService;
    }

    @Override
    public List<TripEntity> getTrips(Long userId) {
        UserEntity user = userService.getUser(userId);
        return tripRepository.findTripEntitiesByUserId(user);
    }

    @Override
    public void addTrip(TripEntity trip, Long userId) {
        TripEntity savedtrip = tripRepository.save(trip);

        userService.getUser(userId).getTrips().add(savedtrip);
    }

    @Override
    public void deleteTrip(TripEntity trip) {
        tripRepository.delete(trip);
    }
}
