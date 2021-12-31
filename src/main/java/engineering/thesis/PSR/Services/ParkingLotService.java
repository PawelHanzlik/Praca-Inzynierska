package engineering.thesis.PSR.Services;

import engineering.thesis.PSR.Entities.ParkingLotEntity;
import engineering.thesis.PSR.Entities.ZoneEntity;
import engineering.thesis.PSR.Exceptions.Classes.NoSuchParkingLotException;

import java.util.List;

public interface ParkingLotService {

    ParkingLotEntity getParkingLot(Long parkingLotId) throws NoSuchParkingLotException;
    List<ParkingLotEntity> getAllParkingLots();
    ParkingLotEntity addParkingLot(ParkingLotEntity parkingLot);
    void deleteParkingLot(Long parkingLotId) throws NoSuchParkingLotException;
    void changeParkingLotOccupancy(Long parkingLotId, Integer newOccupancy) throws NoSuchParkingLotException;
    List<Double> generateCords(ZoneEntity zone);
}
