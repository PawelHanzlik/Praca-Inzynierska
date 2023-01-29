package engineering.thesis.PSR.Controller;

import engineering.thesis.PSR.Entities.ParkingLotEntity;
import engineering.thesis.PSR.Entities.TripEntity;
import engineering.thesis.PSR.Entities.UserEntity;
import engineering.thesis.PSR.Entities.UserHistoryEntity;
import engineering.thesis.PSR.Entities.ZoneEntity;
import engineering.thesis.PSR.Services.ParkingLotService;
import engineering.thesis.PSR.Services.TripService;
import engineering.thesis.PSR.Services.UserHistoryService;
import engineering.thesis.PSR.Services.UserService;
import engineering.thesis.PSR.Services.ZoneService;
import engineering.thesis.PSR.Solver;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/maxsat")
public class CarSharingController {

    private final ZoneService zoneService;
    private final ParkingLotService parkingService;
    private final TripService tripService;
    private final UserHistoryService userHistoryService;
    private final UserService userService;
    private String pickedCity = "";

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.GERMAN);

    private final ZoneId zoneId = ZoneId.of("Europe/Warsaw");

    private Instant instant = Instant.now();

    @Autowired
    public CarSharingController(
        ZoneService zoneService, 
        ParkingLotService parkingLotService, 
        TripService tripService, 
        UserHistoryService userHistoryService,
        UserService userService
        ){
        this.zoneService = zoneService;
        this.parkingService = parkingLotService;
        this.tripService = tripService;
        this.userHistoryService = userHistoryService;
        this.userService = userService;
    }



    @GetMapping("/sfps")
    public ResponseEntity<String> searchForParkingSpot(@RequestParam(value = "CordX", defaultValue = "0") int x, @RequestParam(value = "CordY", defaultValue = "0") int y,
                                                       @RequestParam()String [] usersChoices) {

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
                for (int i = 0; i <= count; i++) {
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

    @GetMapping("/triphistory")
    public List<TripEntity> tripHistory(@RequestParam Long user) {
        return tripService.getTrips(user);
    }

    @GetMapping("/userhistory")
    public List<UserHistoryEntity> userHistory(@RequestParam Long user) {
        return userHistoryService.getUserHistories(user);
    }

    @GetMapping("/createUser")
    public void createUser(
        @RequestParam String name,
        @RequestParam String surname,
        @RequestParam int age) {
            var user = new UserEntity();

            user.setAge(age);
            user.setSurname(surname);
            user.setName(name);
            user.setTrips(new ArrayList<>());

            this.userService.addUser(user);
        }

    @GetMapping("/users")
    public List<UserEntity> getUsers() {
        return this.userService.getAllUsers();
    }

    @PostMapping("/saveTrip")
    public void saveTrip(
            @RequestParam()Long userId,
            @RequestParam()String time,
            @RequestParam()Integer x,
            @RequestParam()Integer y,
            @RequestParam()String [] usersChoices
    ){
        TripEntity trip = new TripEntity();

        trip.setUserId(this.userService.getUser(userId));
        trip.setTime(Instant.from(instant.atZone(zoneId).with(LocalTime.parse(time))));
        trip.setXCordOfZone(x);
        trip.setYCordOfZone(y);
        trip.setUserChoices(Arrays.asList(usersChoices));

        System.out.println(trip.getXCordOfZone());
        System.out.println(trip.getYCordOfZone());
        System.out.println(trip.getTime());

        tripService.addTrip(trip, userId);
    }
}