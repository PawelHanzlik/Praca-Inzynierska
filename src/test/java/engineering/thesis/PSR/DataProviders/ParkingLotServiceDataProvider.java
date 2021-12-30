package engineering.thesis.PSR.DataProviders;

import engineering.thesis.PSR.Entities.ParkingLotEntity;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotServiceDataProvider {

    public static final Long parkingLotId = (long)1;
    public static ParkingLotEntity parkingLotsEntity;
    public static ParkingLotEntity parkingLotsEntity1;
    public static ParkingLotEntity parkingLotsEntity2;
    public static ParkingLotEntity parkingLotsEntity3;
    public static List<ParkingLotEntity> parkingLotsEntities;
    static {
        parkingLotsEntity = ParkingLotEntity.builder().parkingLotId(parkingLotId).CordX(1.0).CordY(1.0).isForHandicapped(true).isPaid(true).isGuarded(true).freeSpaces(20).build();
        parkingLotsEntity1 = ParkingLotEntity.builder().parkingLotId(parkingLotId).CordX(2.0).CordY(2.0).isForHandicapped(false).isPaid(false).isGuarded(false).freeSpaces(10).build();
        parkingLotsEntity2 = ParkingLotEntity.builder().parkingLotId(2L).CordX(1.0).CordY(1.0).isForHandicapped(true).isPaid(true).isGuarded(true).freeSpaces(20).build();
        parkingLotsEntity3 = ParkingLotEntity.builder().parkingLotId(2L).CordX(1.0).CordY(1.0).isForHandicapped(true).isPaid(true).isGuarded(true).freeSpaces(30).build();
        parkingLotsEntities = new ArrayList<>();
        parkingLotsEntities.add(parkingLotsEntity);
        parkingLotsEntities.add(parkingLotsEntity1);
    }
}
