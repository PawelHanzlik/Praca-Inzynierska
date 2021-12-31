package engineering.thesis.PSR.Services;

import engineering.thesis.PSR.Entities.ZoneEntity;
import engineering.thesis.PSR.Exceptions.Classes.NoSuchZoneException;

import java.util.List;

public interface ZoneService {

    ZoneEntity getZone(Long zoneId) throws NoSuchZoneException;
    List<ZoneEntity> getAllZones();
    ZoneEntity addZone(ZoneEntity zone);
    void deleteZone(Long zoneId) throws NoSuchZoneException;
    void changeZoneOccupiedRatio(Long zoneId, Double newOccupiedRatio) throws NoSuchZoneException;
    void changeZoneAttractivenessRatio(Long zoneId, Double newAttractivenessRatio) throws NoSuchZoneException;
    boolean isAdjacent(String city,ZoneEntity zone, int x, int y);
}
