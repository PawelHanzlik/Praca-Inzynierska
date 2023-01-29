package engineering.thesis.PSR.Services;

import engineering.thesis.PSR.Entities.TripEntity;

import java.util.List;

public interface TripService {
    List<TripEntity> getTrips(Long userId);
    void addTrip(TripEntity trip, Long userId);
    void deleteTrip(TripEntity trip);
}
