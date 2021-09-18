package engineering.thesis.PSR.Controller;

import engineering.thesis.PSR.Entities.ParkingLotEntity;
import engineering.thesis.PSR.Entities.UserEntity;
import engineering.thesis.PSR.Entities.ZoneEntity;
import engineering.thesis.PSR.Services.ParkingLotService;
import engineering.thesis.PSR.Services.UserService;
import engineering.thesis.PSR.Services.ZoneService;
import engineering.thesis.PSR.Solver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/maxsat")
public class CarSharingController {

    private final ZoneService zoneService;
    private final ParkingLotService parkingService;
    private final UserService userService;

    private int sfpcTestcounter = 0;

    @Autowired
    public CarSharingController(ZoneService zoneService, ParkingLotService parkingLotService, UserService userService){
        this.zoneService = zoneService;
        this.parkingService = parkingLotService;
        this.userService = userService;
    }



    @GetMapping("/sfps")
    public String searchForParkingSpot(@RequestParam(value = "Lat", defaultValue = "0") int x,@RequestParam(value = "Lon", defaultValue = "0") int y,@RequestParam(value = "user", defaultValue = "-1")int user) {

        List<ZoneEntity> zones = new ArrayList<>();
        //wybierz strefy z bazy danych które przylegają do lokacji
        zoneService.getAllZones().forEach(zone -> {
            if ((zone.getCordX()<= x+1 & zone.getCordX()>= x-1 & zone.getCordY()<= y+1 & zone.getCordY()>= y) ||
                    (zone.getCordX()==x & zone.getCordY()==y-1)){ zones.add(zone);}
        });

        class zonetouple{
            public final long ZoneId;
            public final int Score;
            public zonetouple(long parkingId,int Score){this.ZoneId = parkingId;this.Score=Score;}

            @Override
            public String toString(){
                return String.format("ZoneId: %d     Score: %d \n", ZoneId,Score);
            }
        }
        int NoUser;
        if (user == (-1)) NoUser = this.sfpcTestcounter++;
        else NoUser = user;

        List<zonetouple> results = new ArrayList<>();
        Solver solver = new Solver(zones,userService.getAllUsers().get(NoUser));
        parkingService.getAllParkingLots().forEach(parking ->
                results.add(new zonetouple(parking.getParkingLotId(),solver.test(parking))));

        results.sort(Comparator.comparingInt(obj -> obj.Score));
        return zoneService.getAllZones().size() + "\n" + results;
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
    public String GenerateZones(@RequestParam(value = "amount", defaultValue = "5") int amount) {


        //zones
        for (int x = (-1)*amount; x < amount; x++)
            for (int y = (-1)*amount; y < amount; y++) {
            ZoneEntity zone = new ZoneEntity();
            zone.setZoneType("test");
            zone.setOccupiedRatio(Math.round(Math.random()*100.0)/100.0);
            zone.setAttractivenessRatio(Math.round(Math.random()*100.0)/100.0);
            zone.setRequestRatio(Math.round(Math.random()*100.0)/100.0);
            zone.setCordX(x);
            zone.setCordY(y);
            zoneService.addZone(zone);
        }
        List<ZoneEntity> zones = zoneService.getAllZones();
        //ParkingLots
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

        for (int i = 0; i < amount*amount*10; i++) {
            UserEntity user = new UserEntity();
            user.setCarSize((int) Math.round(Math.random()*10)+1);
            user.setAge((int) (Math.round(Math.random()*50)+20));
            user.setHandicapped(Math.random()>0.75);
            user.setName("Jack");
            user.setSurname("aaa");
            user.setPreferableZone(zones.get((int) Math.round(Math.random()*(zones.size()-1))).getZoneId());

            userService.addUser(user);
        }



        return "ok ";
    }
}

