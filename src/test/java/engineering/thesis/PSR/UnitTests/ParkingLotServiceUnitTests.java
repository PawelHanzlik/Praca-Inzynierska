package engineering.thesis.PSR.UnitTests;

import engineering.thesis.PSR.Entities.ParkingLotEntity;
import engineering.thesis.PSR.Exceptions.Classes.NoSuchParkingLotException;
import engineering.thesis.PSR.Repositories.ParkingLotRepository;
import engineering.thesis.PSR.Services.ParkingLotServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static engineering.thesis.PSR.DataProviders.ParkingLotServiceDataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
public class ParkingLotServiceUnitTests {

    @InjectMocks
    ParkingLotServiceImpl parkingLotService;

    @Mock
    ParkingLotRepository parkingLotRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getParkingLotTest(){
        when(parkingLotRepository.findById(parkingLotId)).thenReturn(java.util.Optional.ofNullable(parkingLotsEntity));
        ParkingLotEntity getParkingLotEntity = this.parkingLotService.getParkingLot(parkingLotId);
        assertNotNull(getParkingLotEntity);
        assertEquals(1, getParkingLotEntity.getParkingLotId());
        assertEquals(1,getParkingLotEntity.getCordX());
        assertEquals(1,getParkingLotEntity.getCordY());
        assertEquals(true,getParkingLotEntity.getIsForHandicapped());
        assertEquals(true,getParkingLotEntity.getIsPaid());
        assertEquals(true,getParkingLotEntity.getIsGuarded());
        assertEquals(20,getParkingLotEntity.getFreeSpaces());
    }

    @Test
    void getParkingLotNoSuchParkingLotExceptionTest(){
        when(parkingLotRepository.findById(parkingLotId)).thenReturn(Optional.empty());
        assertThrows(NoSuchParkingLotException.class, () -> parkingLotService.getParkingLot(parkingLotId));
    }

    @Test
    void getAllParkingLots(){
        when(parkingLotRepository.findAll()).thenReturn(parkingLotsEntities);
        List<ParkingLotEntity> parkingLotsEntityList = this.parkingLotService.getAllParkingLots();
        assertEquals(parkingLotsEntity,parkingLotsEntityList.get(0));
        assertEquals(parkingLotsEntity1,parkingLotsEntityList.get(1));
    }
    @Test
    void addParkingLotTest(){
        when(parkingLotRepository.save(any(ParkingLotEntity.class))).thenReturn(parkingLotsEntity1);
        ParkingLotEntity newParkingLotEntity = this.parkingLotService.addParkingLot(parkingLotsEntity1);
        assertNotNull(newParkingLotEntity);
        assertEquals(1, newParkingLotEntity.getParkingLotId());
        assertEquals(2,newParkingLotEntity.getCordX());
        assertEquals(2,newParkingLotEntity.getCordY());
        assertEquals(false,newParkingLotEntity.getIsForHandicapped());
        assertEquals(false,newParkingLotEntity.getIsPaid());
        assertEquals(false,newParkingLotEntity.getIsGuarded());
        assertEquals(10,newParkingLotEntity.getFreeSpaces());
    }

    @Test
    void deleteParkingLotTest() {
        Optional<ParkingLotEntity> optionalParkingLotEntity = Optional.of(parkingLotsEntity);

        when(parkingLotRepository.findById(parkingLotId)).thenReturn(optionalParkingLotEntity);

        parkingLotService.deleteParkingLot(parkingLotId);

        Mockito.verify(parkingLotRepository, times(1)).delete(optionalParkingLotEntity.get());
    }

    @Test
    void deleteParkingLotNoSuchParkingLotExceptionTest(){
        when(parkingLotRepository.findById(parkingLotId)).thenReturn(Optional.empty());
        assertThrows(NoSuchParkingLotException.class, () -> parkingLotService.deleteParkingLot(parkingLotId));
    }


    @Test
    void  changeParkingLotOccupiedRatioTest(){
        when(parkingLotRepository.findById(2L)).thenReturn(java.util.Optional.of(parkingLotsEntity2));
        when(parkingLotRepository.save(any(ParkingLotEntity.class))).thenReturn(parkingLotsEntity3);
        this.parkingLotService.changeParkingLotOccupancy(2L,30);
        assertEquals(30,parkingLotsEntity3.getFreeSpaces());
    }

}
