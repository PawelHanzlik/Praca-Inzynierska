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

    private final Instant instant = Instant.now();

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

    @GetMapping("/recommend")
    public ResponseEntity<String> recommend(@RequestParam()Long userId, @RequestParam()String time){
        List<TripEntity> prevTrips = tripService.getTrips(userId);
        LocalTime currentTime = LocalTime.parse(time);
        LocalTime startTime;
        LocalTime endTime;

        LocalTime hourSix = LocalTime.parse("06:00");
        LocalTime hourFourteen = LocalTime.parse("14:00");
        LocalTime hourTwentyTwo = LocalTime.parse("22:00");

        if (currentTime.isAfter(hourSix)){
            if (currentTime.isBefore(hourFourteen)){
                startTime = hourSix;
                endTime = hourFourteen;
            }
            else {
                startTime = hourFourteen;
                endTime = hourTwentyTwo;
            }
        }
        else {
            startTime = hourTwentyTwo;
            endTime = hourSix;
        }



        List<ZoneEntity> zones = new ArrayList<>();
        zoneService.getAllZones().forEach(zone -> {
            if (zone.getCity().equals(pickedCity)) {
                zones.add(zone);
            }});

        @Getter
        class Cords {
            public final int x;
            public final int y;

            public Cords(int x, int y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public int hashCode() {
                return (x+"&"+y).hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                return (obj instanceof Cords) && ((Cords) obj).x == this.x && ((Cords) obj).y == this.y;
            }

            @Override
            public String toString() {
                return String.format("%d %d", x, y);
            }

        }

        Map<Cords, Integer> countingZoneMap = new HashMap<>();
        for (ZoneEntity zone: zones){
            countingZoneMap.put(new Cords(zone.getCordX(), zone.getCordY()), 0);
        }

        @Getter
        class Prefs {
            public final String first;
            public final String second;
            public final String third;
            public final String fourth;

            public Prefs(String first, String second, String third, String fourth) {
                this.first = first;
                this.second = second;
                this.third = third;
                this.fourth = fourth;
            }

            public String[] getAll(){
                return new String[]{this.first, this.second, this.third, this.fourth};
            }

            @Override
            public int hashCode() {
                return (this.first+this.second+this.third+this.fourth).hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                return (obj instanceof Prefs) && ((Prefs) obj).first.equals(this.first)
                        && ((Prefs) obj).second.equals(this.second)
                        && ((Prefs) obj).third.equals(this.third)
                        && ((Prefs) obj).fourth.equals(this.fourth);
            }

            @Override
            public String toString() {
                return String.format("%s, %s, %s, %s", first, second, third, fourth);
            }

        }

        Map<Prefs, Integer> countingPrefMap = new HashMap<>();
        for (int i=0;i<16;i++){
            List<String> prefs = new ArrayList<>();

            int j = i;
            while (j > 0) {
                prefs.add((j % 2 == 0) ? "No" : "Yes");
                j = j/2;
            }
            while (prefs.size() < 4){
                prefs.add("No");
            }

            countingPrefMap.put(new Prefs(prefs.get(0), prefs.get(1), prefs.get(2), prefs.get(3)), 0);
        }

        LocalTime prevTime;

        for (TripEntity trip: prevTrips){
            String hour = String.valueOf(trip.getTime().atZone(zoneId).getHour());
            if (hour.length() == 1)
                hour = "0" + hour;
            String minute = String.valueOf(trip.getTime().atZone(zoneId).getMinute());
            if (minute.length() == 1)
                minute = "0" + minute;
            prevTime = LocalTime.parse(hour + ":" + minute);
            if (startTime.isBefore(endTime)){
                if (prevTime.isAfter(startTime) && prevTime.isBefore(endTime)){
                    Cords cords = new Cords(trip.getXCordOfZone(), trip.getYCordOfZone());
                    countingZoneMap.put(cords, countingZoneMap.get(cords)+1);
                    Prefs prefs = new Prefs(trip.getUserChoices().get(0), trip.getUserChoices().get(1),
                            trip.getUserChoices().get(2), trip.getUserChoices().get(3));
                    countingPrefMap.put(prefs, countingPrefMap.get(prefs)+1);
                }
            }
            else {
                if (prevTime.isAfter(startTime) || prevTime.isBefore(endTime)){
                    Cords cords = new Cords(trip.getXCordOfZone(), trip.getYCordOfZone());
                    countingZoneMap.put(cords, countingZoneMap.get(cords)+1);
                    Prefs prefs = new Prefs(trip.getUserChoices().get(0), trip.getUserChoices().get(1),
                            trip.getUserChoices().get(2), trip.getUserChoices().get(3));
                    countingPrefMap.put(prefs, countingPrefMap.get(prefs)+1);
                }
            }

        }
        System.out.println(countingZoneMap);
        Cords selectedCords = Collections.max(countingZoneMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        System.out.println(selectedCords);

        System.out.println(countingPrefMap);
        Prefs selectedPrefs = Collections.max(countingPrefMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        System.out.println(selectedPrefs);

        if (countingZoneMap.get(selectedCords) == 0 || countingPrefMap.get(selectedPrefs) == 0)
            return new ResponseEntity<>("", HttpStatus.OK);
        return searchForParkingSpot(selectedCords.getX(), selectedCords.getY(), selectedPrefs.getAll());
    }



}