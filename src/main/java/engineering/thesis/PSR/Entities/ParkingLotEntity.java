package engineering.thesis.PSR.Entities;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@Table(name = "Parking_Lots")
public class ParkingLotEntity {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long parkingLotId;

    @NotNull
    private Long zoneId;

    private Boolean isGuarded;

    private Boolean isPaid;

    private Boolean isForHandicapped;

    private Integer freeSpaces;

    private Integer spotSize;
}
