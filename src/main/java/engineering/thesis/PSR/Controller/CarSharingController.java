package engineering.thesis.PSR.Controller;

import engineering.thesis.PSR.Entities.ParkingLotEntity;
import engineering.thesis.PSR.Entities.UserEntity;
import engineering.thesis.PSR.Entities.ZoneEntity;
import engineering.thesis.PSR.Services.ParkingLotService;
import engineering.thesis.PSR.Services.UserService;
import engineering.thesis.PSR.Services.ZoneService;
import engineering.thesis.PSR.Solver;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/maxsat")
public class CarSharingController {

    private final ZoneService zoneService;
    private final ParkingLotService parkingService;
    private final UserService userService;

    @Autowired
    public CarSharingController(ZoneService zoneService, ParkingLotService parkingLotService, UserService userService){
        this.zoneService = zoneService;
        this.parkingService = parkingLotService;
        this.userService = userService;
    }



    @GetMapping("/sfps")
    public ResponseEntity<String> searchForParkingSpot(@RequestParam(value = "Lat", defaultValue = "0") int x, @RequestParam(value = "Lon", defaultValue = "0") int y,
                                                       @RequestParam()String [] usersChoices) {

        List<ZoneEntity> zones = new ArrayList<>();
        //wybierz strefy z bazy danych które przylegają do lokacji
        zoneService.getAllZones().forEach(zone -> {
            if (zoneService.isAdjacent(zone, x, y)) {
                zones.add(zone);
            }
        });

        @Getter
        class ZoneTuple {
            public final long ParkingId;
            public final int Score;

            public ZoneTuple(long parkingId, int Score) {
                this.ParkingId = parkingId;
                this.Score = Score;
            }

            @Override
            public String toString() {
                return String.format("ParkingId: %d     Score: %d \n", ParkingId, Score);
            }
        }
        List<ZoneTuple> results = new ArrayList<>();
        Solver solver = new Solver(zones,usersChoices);
        parkingService.getAllParkingLots().forEach(parking ->
                results.add(new ZoneTuple(parking.getParkingLotId(),solver.test(parking))));

        results.sort(Comparator.comparingInt(ZoneTuple::getScore).reversed());
        System.out.println(results);
        return new ResponseEntity<>(zoneService.getAllZones().size() + "\n" + results, HttpStatus.OK);
    }

    @GetMapping("/sfc")
    public String searchForCar(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/parkCar")
    public String park(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/takeCar")
    public String takeCar(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/GenerateData")
    public HttpStatus GenerateZones(@RequestParam(defaultValue = "Kraków") String city) {

        int amount = 5;

        for (int x = (-1)*amount; x < amount; x++)
            for (int y = (-1)*amount; y < amount; y++) {
            ZoneEntity zone = new ZoneEntity();
            zone.setZoneType("test");
            zone.setCity(city);
            zone.setOccupiedRatio(Math.round(Math.random()*100.0)/100.0);
            zone.setAttractivenessRatio(Math.round(Math.random()*100.0)/100.0);
            zone.setRequestRatio(Math.round(Math.random()*100.0)/100.0);
            zone.setCordX(x);
            zone.setCordY(y);
            zoneService.addZone(zone);
        }
        List<ZoneEntity> zones = zoneService.getAllZones();

        for (int i = 0; i < amount*amount*10; i++) {
            ParkingLotEntity parking = new ParkingLotEntity();
            parking.setZoneId(zones.get((int) Math.round(Math.random()*(zones.size()-1))).getZoneId());
            parking.setFreeSpaces((int) Math.round(Math.random()*100));
            parking.setIsGuarded(Math.random()>0.5);
            parking.setIsPaid(Math.random()>0.5);
            parking.setIsForHandicapped(Math.random()>0.5);
            parking.setSpotSize((int) Math.round(Math.random()*5));

            parkingService.addParkingLot(parking);
        }

//        for (int i = 0; i < amount*amount*10; i++) {
//            UserEntity user = new UserEntity();
//            user.setCarSize((int) Math.round(Math.random()*10)+1);
//            user.setAge((int) (Math.round(Math.random()*50)+20));
//            user.setHandicapped(Math.random()>0.75);
//            user.setName("Jack");
//            user.setSurname("aaa");
//            user.setPreferableZone(zones.get((int) Math.round(Math.random()*(zones.size()-1))).getZoneId());
//
//            userService.addUser(user);
//        }
        return HttpStatus.OK;
    }

}