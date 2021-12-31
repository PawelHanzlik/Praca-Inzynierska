package engineering.thesis.PSR.Controller;

import engineering.thesis.PSR.Entities.ParkingLotEntity;
import engineering.thesis.PSR.Entities.ZoneEntity;
import engineering.thesis.PSR.Services.ParkingLotService;
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
    private String pickedCity = "";

    @Autowired
    public CarSharingController(ZoneService zoneService, ParkingLotService parkingLotService){
        this.zoneService = zoneService;
        this.parkingService = parkingLotService;
    }



    @GetMapping("/sfps")
    public ResponseEntity<String> searchForParkingSpot(@RequestParam(value = "CordX", defaultValue = "0") int x, @RequestParam(value = "CordY", defaultValue = "0") int y,
                                                       @RequestParam()String [] usersChoices) {

        System.out.println(x+" "+y);
        List<ZoneEntity> zones = new ArrayList<>();
        zoneService.getAllZones().forEach(zone -> {
            if (zoneService.isAdjacent(pickedCity,zone, x, y)) {
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
        List<ZoneTuple> topResults = results.subList(0,3);
        System.out.println(topResults);
        return new ResponseEntity<>(topResults.toString(), HttpStatus.OK);
    }

    @GetMapping("/GenerateData")
    public HttpStatus GenerateZones() {

        List<String> cities = new ArrayList<>();
        cities.add("Kraków");
        cities.add("Warszawa");
        cities.add("Wrocław");
        if (zoneService.getAllZones().isEmpty()){
            for (String city : cities) {
                for (int x = -4; x <= 5; x++)
                    for (int y = -3; y <= 4; y++) {
                        ZoneEntity zone = new ZoneEntity();
                        zone.setCity(city);
                        zone.setOccupiedRatio(Math.round(Math.random() * 100.0) / 100.0);
                        zone.setAttractivenessRatio(Math.round(Math.random() * 100.0) / 100.0);
                        zone.setCordX(x);
                        zone.setCordY(y);
                        zoneService.addZone(zone);
                    }
            }
            List<ZoneEntity> zones = zoneService.getAllZones();
            for (ZoneEntity zone: zones){
                int count = Integer.parseInt(String.valueOf(Math.round(Math.random()*4)));
                for (int i = 0; i < count; i++) {
                    ParkingLotEntity parking = new ParkingLotEntity();
                    parking.setZoneId(zone.getZoneId());
                    List<Double> cords = parkingService.generateCords(zone);
                    parking.setCordX(cords.get(0));
                    parking.setCordY(cords.get(1));
                    parking.setFreeSpaces((int) Math.round(Math.random()*100));
                    parking.setIsGuarded(Math.random()>0.5);
                    parking.setIsPaid(Math.random()>0.5);
                    parking.setIsForHandicapped(Math.random()>0.5);

                    parkingService.addParkingLot(parking);
                }
            }
        }
        return HttpStatus.OK;
    }

    @GetMapping("/AssignCity")
    public HttpStatus AssignCity(@RequestParam(defaultValue = "Kraków") String city) {
        this.pickedCity = city;
        return HttpStatus.OK;
    }


}