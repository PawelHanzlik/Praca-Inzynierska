package engineering.thesis.PSR.Services;

import engineering.thesis.PSR.Entities.TripEntity;
import engineering.thesis.PSR.Repositories.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripServiceImpl implements TripService{
    private final TripRepository tripRepository;

    public TripServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public List<TripEntity> getTrips(Long userId) {
        return tripRepository.findTripEntitiesByUserId(userId);
    }

    @Override
    public void addTrip(TripEntity trip) {
        tripRepository.save(trip);
    }

    @Override
    public void deleteTrip(TripEntity trip) {
        tripRepository.delete(trip);
    }
}
