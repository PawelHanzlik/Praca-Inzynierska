package engineering.thesis.PSR.Entities;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@Table(name = "Trip")
public class TripEntity {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @NotNull
    private Long userId;

    private Instant beginTime;

    private Instant endTime;

    @NotNull
    private Long destinationParkingLotId;

    @NotNull
    private Double destinationAttractiveness;

    @NotNull
    private Double destinationOccupiedRatio;
}
