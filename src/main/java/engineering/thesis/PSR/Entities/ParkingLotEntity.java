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
@Table(name = "Parking_Lot")
public class ParkingLotEntity {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parkingLotId;

    @NotNull
    private Long zoneId;

    @NotNull
    private Double CordX;

    @NotNull
    private Double CordY;

    private Boolean isGuarded;

    private Boolean isPaid;

    private Boolean isForHandicapped;

    private Integer freeSpaces;

}
