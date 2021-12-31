package engineering.thesis.PSR.Services;

import engineering.thesis.PSR.Entities.ParkingLotEntity;
import engineering.thesis.PSR.Entities.ZoneEntity;
import engineering.thesis.PSR.Exceptions.Classes.NoSuchParkingLotException;
import engineering.thesis.PSR.Repositories.ParkingLotRepository;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService{

    private final ParkingLotRepository parkingLotRepository;
    @Autowired
    public ParkingLotServiceImpl(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    @Override
    public ParkingLotEntity getParkingLot(Long parkingLotId) throws NoSuchParkingLotException {
        Optional<ParkingLotEntity> parkingLotOptional = this.parkingLotRepository.findById(parkingLotId);
        if (parkingLotOptional.isEmpty()){
            throw new NoSuchParkingLotException();
        }
        return parkingLotOptional.get();
    }

    @Override
    public List<ParkingLotEntity> getAllParkingLots() {
        return this.parkingLotRepository.findAll();
    }

    @Override
    public ParkingLotEntity addParkingLot(ParkingLotEntity parkingLot) {
        return this.parkingLotRepository.save(parkingLot);
    }

    @Override
    public void deleteParkingLot(Long parkingLotId) throws NoSuchParkingLotException{
        Optional<ParkingLotEntity> parkingLotOptional = this.parkingLotRepository.findById(parkingLotId);
        if (parkingLotOptional.isEmpty()){
            throw new NoSuchParkingLotException();
        }
        else{
            ParkingLotEntity parkingLot = parkingLotOptional.get();
            this.parkingLotRepository.delete(parkingLot);
        }
    }

    @Override
    public void changeParkingLotOccupancy(Long parkingLotId, Integer newOccupancy) throws NoSuchParkingLotException{
        Optional<ParkingLotEntity> parkingLotOptional = this.parkingLotRepository.findById(parkingLotId);
        if (parkingLotOptional.isEmpty()){
            throw new NoSuchParkingLotException();
        }
        else{
            ParkingLotEntity parkingLot = parkingLotOptional.get();
            parkingLot.setFreeSpaces(newOccupancy);
            this.parkingLotRepository.save(parkingLot);
        }
    }

    @Override
    public List<Double> generateCords(ZoneEntity zone){
        double radius = 0.5*Math.sqrt(3)/2;
        double theta = 2*Math.PI*Math.random();
        double len = Math.sqrt(Math.random())*radius;
        double x = Precision.round(zone.getCordX() + len * Math.cos(theta),2);
        double y = Precision.round(zone.getCordX() + len * Math.sin(theta),2);
        List<Double> cords = new ArrayList<>();
        cords.add(x);
        cords.add(y);
        return cords;
    }
}
