package engineering.thesis.PSR.Services;

import engineering.thesis.PSR.Entities.ZoneEntity;
import engineering.thesis.PSR.Exceptions.Classes.NoSuchZoneException;
import engineering.thesis.PSR.Repositories.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ZoneServiceImpl implements ZoneService{

    private final ZoneRepository zoneRepository;
    @Autowired
    public ZoneServiceImpl(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public ZoneEntity getZone(Long zoneId) throws NoSuchZoneException {
        Optional<ZoneEntity> zoneOptional = this.zoneRepository.findById(zoneId);
        if (zoneOptional.isEmpty()){
            throw new NoSuchZoneException();
        }
        return zoneOptional.get();
    }

    @Override
    public List<ZoneEntity> getAllZones() {
        return this.zoneRepository.findAll();
    }

    @Override
    public ZoneEntity addZone(ZoneEntity zone) {
        return this.zoneRepository.save(zone);
    }

    @Override
    public void deleteZone(Long zoneId) throws NoSuchZoneException{
        Optional<ZoneEntity> zoneOptional = this.zoneRepository.findById(zoneId);
        if (zoneOptional.isEmpty()){
            throw new NoSuchZoneException();
        }
        else{
            ZoneEntity zone = zoneOptional.get();
            this.zoneRepository.delete(zone);
        }
    }

    @Override
    public void changeZoneOccupiedRatio(Long zoneId, Double newOccupiedRatio) throws NoSuchZoneException {
        Optional<ZoneEntity> zoneOptional = this.zoneRepository.findById(zoneId);
        if (zoneOptional.isEmpty()){
            throw new NoSuchZoneException();
        }
        else{
            ZoneEntity zone = zoneOptional.get();
            zone.setOccupiedRatio(newOccupiedRatio);
            this.zoneRepository.save(zone);
        }
    }

    @Override
    public void changeZoneAttractivenessRatio(Long zoneId, Double newAttractivenessRatio) throws NoSuchZoneException {
        Optional<ZoneEntity> zoneOptional = this.zoneRepository.findById(zoneId);
        if (zoneOptional.isEmpty()){
            throw new NoSuchZoneException();
        }
        else{
            ZoneEntity zone = zoneOptional.get();
            zone.setAttractivenessRatio(newAttractivenessRatio);
            this.zoneRepository.save(zone);
        }
    }

    @Override
    public void changeZoneRequestRatio(Long zoneId, Double newRequestRatio) throws NoSuchZoneException {
        Optional<ZoneEntity> zoneOptional = this.zoneRepository.findById(zoneId);
        if (zoneOptional.isEmpty()){
            throw new NoSuchZoneException();
        }
        else{
            ZoneEntity zone = zoneOptional.get();
            zone.setRequestRatio(newRequestRatio);
            this.zoneRepository.save(zone);
        }
    }
}
